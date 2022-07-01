package com.tokopedia.people.views.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.util.manager.FeedFloatingButtonManager
import com.tokopedia.feedcomponent.view.base.FeedPlusContainerListener
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_FULL
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_NOT_FOUND
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.people.*
import com.tokopedia.people.R
import com.tokopedia.people.views.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.views.activity.UserProfileActivity.Companion.EXTRA_USERNAME
import com.tokopedia.unifycomponents.*
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.feedcomponent.bottomsheets.onboarding.FeedUserCompleteOnboardingBottomSheet
import com.tokopedia.feedcomponent.bottomsheets.onboarding.FeedUserTnCOnboardingBottomSheet
import com.tokopedia.people.views.activity.FollowerFollowingListingActivity
import com.tokopedia.people.views.adapter.UserPostBaseAdapter
import com.tokopedia.people.analytic.UserProfileTracker
import com.tokopedia.people.databinding.UpFragmentUserProfileBinding
import com.tokopedia.people.databinding.UpLayoutUserProfileHeaderBinding
import com.tokopedia.people.utils.UserProfileUtils
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.utils.showToast
import com.tokopedia.people.utils.withCache
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.ProfileType
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import com.tokopedia.abstraction.R as abstractionR
import com.tokopedia.unifyprinciples.R as unifyR

class UserProfileFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private var userProfileTracker: UserProfileTracker,
    private val userSession: UserSessionInterface,
    private val feedFloatingButtonManager: FeedFloatingButtonManager,
) : BaseDaggerFragment(),
    AdapterCallback,
    ShareBottomsheetListener,
    ScreenShotListener,
    PermissionListener,
    UserPostBaseAdapter.PlayWidgetCallback,
    FeedPlusContainerListener {

    private val gridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(activity, 2)
    }

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenShotDetector: ScreenshotDetector? = null

    private var shouldRefreshRecyclerView: Boolean = false
    private var isViewMoreClickedBio: Boolean = false

    private var _binding: UpFragmentUserProfileBinding? = null

    private val binding: UpFragmentUserProfileBinding
        get() = _binding!!

    private val mainBinding: UpLayoutUserProfileHeaderBinding
        get() = _binding!!.mainLayout

    override fun initInjector() {
        /** No need since we alr have constructor injection */
    }

    private lateinit var viewModel: UserProfileViewModel

    private val mAdapter: UserPostBaseAdapter by lazy {
        UserPostBaseAdapter(
            viewModel,
            this,
            userSession.userId,
            this
        ) { cursor ->
            submitAction(UserProfileAction.LoadPlayVideo(cursor))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            viewModelFactoryCreator.create(
                this,
                requireArguments().getString(EXTRA_USERNAME) ?: ""
            )
        )[UserProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UpFragmentUserProfileBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedFloatingButtonManager.setInitialData(this)

        initObserver()
        initListener()
        setHeader()

        if (arguments == null || requireArguments().getString(EXTRA_USERNAME).isNullOrBlank()) {
            //TODO show error page
            activity?.finish()
        }

        mainBinding.swipeRefreshLayout.setOnRefreshListener {
            refreshLandingPageData(true)
        }

        refreshLandingPageData(true)
        binding.rootContainer.displayedChild = PAGE_LOADING

        mainBinding.appBarUserProfile.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            shouldRefreshRecyclerView = verticalOffset == 0
        })

        mainBinding.swipeRefreshLayout.setOnChildScrollUpCallback { _, _ ->
            mainBinding.rvPost.canScrollVertically(-1) || !shouldRefreshRecyclerView
        }

        context?.let {
            screenShotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
                it,
                this,
                this,
                permissionListener = this
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainBinding.rvPost.removeOnScrollListener(feedFloatingButtonManager.scrollListener)
        feedFloatingButtonManager.cancel()

        _binding = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when(childFragment) {
            is FeedUserCompleteOnboardingBottomSheet -> {
                /** TODO: set listener */
            }
            is FeedUserTnCOnboardingBottomSheet -> {
                /** TODO: set listener */
            }
        }
    }

    private fun refreshLandingPageData(isRefreshPost: Boolean = false) {
        submitAction(UserProfileAction.LoadProfile(isRefreshPost))
    }

    private fun initListener() {
        mainBinding.apply {
            textFollowingLabel.setOnClickListener { goToFollowingFollowerPage(false) }
            textFollowingCount.setOnClickListener { goToFollowingFollowerPage(false) }
            textFollowerLabel.setOnClickListener { goToFollowingFollowerPage(true) }
            textFollowerCount.setOnClickListener { goToFollowingFollowerPage(true) }

            textSeeMore.setOnClickListener {
                userProfileTracker.clickSelengkapnya(userSession.userId, self = viewModel.isSelfProfile)
                textBio.maxLines = MAX_LINE
                textSeeMore.hide()
                isViewMoreClickedBio = true
            }

            btnActionFollow.setOnClickListener {
                if(!userSession.isLoggedIn) {
                    startActivityForResult(
                        RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN_TO_FOLLOW
                    )
                }
                else doFollowUnfollow(isFromLogin = false)
            }

            fabUserProfile.setOnClickListener {
                FeedUserTnCOnboardingBottomSheet.getFragment(
                    childFragmentManager,
                    requireActivity().classLoader
                ).showNow(childFragmentManager)
//            FeedUserCompleteOnboardingBottomSheet.getFragment(
//                childFragmentManager,
//                requireActivity().classLoader
//            ).showNow(childFragmentManager)
            }

            mainBinding.rvPost.addOnScrollListener(feedFloatingButtonManager.scrollListener)
//        recyclerviewPost?.let { feedFloatingButtonManager.setDelayForExpandFab(it) }
        }
    }

    private fun initUserPost(userId: String) {
        gridLayoutManager.spanSizeLookup = getSpanSizeLookUp()


        mainBinding.rvPost.layoutManager = gridLayoutManager
        if (mainBinding.rvPost.itemDecorationCount == 0) {
            val spacing = requireContext().resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl1)
            mainBinding.rvPost.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))
        }
        mainBinding.rvPost.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.cursor = ""
        mAdapter.startDataLoading(userId)
    }

    private fun getSpanSizeLookUp(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mAdapter.getItemViewType(position)) {
                    LOADING -> 2
                    else -> 1
                }
            }
        }
    }

    private fun initObserver() {
        observeUiState()
        observeUiEvent()

        addListObserver()
        addUserPostObserver()
        adduserPostErrorObserver()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderProfileInfo(it.prevValue?.profileInfo, it.value.profileInfo)
                renderFollowInfo(it.prevValue, it.value)
                renderCreatePostButton(it.prevValue, it.value)
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is UserProfileUiEvent.ErrorFollowUnfollow -> {
                        val message = if(event.message.isNotEmpty()) event.message else getDefaultErrorMessage()
                        view?.showErrorToast(message)
                    }
                    is UserProfileUiEvent.SuccessUpdateReminder -> {
                        mAdapter.notifyItemChanged(event.position)
                        view?.showToast(event.message)
                    }
                    is UserProfileUiEvent.ErrorUpdateReminder -> {
                        val message = when (event.throwable) {
                            is UnknownHostException, is SocketTimeoutException -> {
                                requireContext().getString(R.string.up_error_local_error)
                            }
                            else -> {
                                event.throwable.message ?: getDefaultErrorMessage()
                            }
                        }

                        view?.showErrorToast(message)
                    }
                    is UserProfileUiEvent.ErrorLoadProfile -> {
                        showGlobalError(
                            when (event.throwable) {
                                is UnknownHostException, is SocketTimeoutException -> NO_CONNECTION
                                is IllegalStateException -> PAGE_FULL
                                is RuntimeException -> {
                                    when (event.throwable.localizedMessage?.toIntOrNull()) {
                                        ReponseStatus.NOT_FOUND -> PAGE_NOT_FOUND
                                        else -> SERVER_ERROR
                                    }
                                }
                                else -> SERVER_ERROR
                            }
                        )
                    }
                }
            }
        }
    }

    private fun addUserPostObserver() {
        viewModel.userPostLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    initUserPost(viewModel.profileUserID)
                }
            }
        }
    }

    private fun addListObserver() {
        viewModel.playPostContentLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Loading -> {
                        mAdapter.resetAdapter()
                        mAdapter.notifyDataSetChanged()
                    }
                    is Success -> {
                        mAdapter.onSuccess(it.data)
                    }
                    is ErrorMessage -> {
                        mAdapter.onError()
                    }
                }
            }
        }
    }

    private fun adduserPostErrorObserver() {
        viewModel.userPostErrorLiveData.observe(viewLifecycleOwner) {
            with(mainBinding) {
                userPostContainer.displayedChild = PAGE_ERROR

                globalErrorPost.refreshBtn?.setOnClickListener {
                    userPostContainer.displayedChild = PAGE_LOADING
                    initUserPost(viewModel.profileUserID)
                }
            }
        }
    }

    private fun addLiveClickListener(appLink: String) {
        RouteManager.route(context, appLink)
    }

    /** Render UI */
    private fun renderProfileInfo(
        prev: ProfileUiModel?,
        curr: ProfileUiModel,
    ) {
        if(prev == curr || curr == ProfileUiModel.Empty) return

        userProfileTracker.openUserProfile(
            viewModel.profileUserID,
            live = curr.liveInfo.isLive
        )

        if(mainBinding.swipeRefreshLayout.isRefreshing) {
            mainBinding.swipeRefreshLayout.isRefreshing = false
        }

        binding.rootContainer.displayedChild = PAGE_CONTENT


        /** Setup Profile Info */
        setProfileImg(curr)

        with(mainBinding) {
            textUserName.shouldShowWithAction(curr.username.isNotBlank()) {
                textUserName.text = getString(R.string.up_username_template, curr.username)
            }
            textDisplayName.text = curr.name
            textContentCount.text = UserProfileUtils.getFormattedNumber(curr.stats.totalPost)
            textFollowerCount.text = UserProfileUtils.getFormattedNumber(curr.stats.totalFollower)
            textFollowingCount.text = UserProfileUtils.getFormattedNumber(curr.stats.totalFollowing)

            /** Setup Bio */
            val displayBioText = HtmlLinkHelper(requireContext(), curr.biography).spannedString
            textBio.text = displayBioText

            if (displayBioText?.lines()?.count().orZero() > SEE_ALL_LINE) {
                if (isViewMoreClickedBio) {
                    textBio.maxLines = MAX_LINE
                    textSeeMore.hide()
                } else {
                    textBio.maxLines = SEE_ALL_LINE
                    textSeeMore.show()
                }
            } else {
                textSeeMore.hide()
            }


            if (!userSession.isLoggedIn)
                updateToUnFollowUi()

            headerProfile.title = curr.name
        }
    }

    private fun renderFollowInfo(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {

        if(prev?.followInfo == value.followInfo &&
            prev.profileType == value.profileType
        ) return

        with(mainBinding) {
            when(value.profileType) {
                ProfileType.NotLoggedIn -> btnActionFollow.show()
                ProfileType.OtherUser -> {
                    with(value) {
                        btnActionFollow.show()
                        if (followInfo.status)
                            updateToFollowUi()
                        else
                            updateToUnFollowUi()
                    }
                }
                ProfileType.Unknown,
                ProfileType.Self -> btnActionFollow.hide()
            }
        }
    }

    private fun renderCreatePostButton(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {
        if(prev?.followInfo == value.followInfo &&
            prev.profileType == value.profileType &&
            prev.profileWhitelist == value.profileWhitelist
        ) return

        mainBinding.fabUserProfile.showWithCondition(
            value.profileType == ProfileType.Self && value.profileWhitelist.isWhitelist
        )
    }

    private fun updateToFollowUi() {
        mainBinding.btnActionFollow.text =  getString(R.string.up_btn_text_following)
        mainBinding.btnActionFollow.buttonVariant = UnifyButton.Variant.GHOST
        mainBinding.btnActionFollow.buttonType = UnifyButton.Type.ALTERNATE
    }

    private fun updateToUnFollowUi() {
        mainBinding.btnActionFollow.text = getString(R.string.up_btn_text_follow)
        mainBinding.btnActionFollow.buttonVariant = UnifyButton.Variant.FILLED
        mainBinding.btnActionFollow.buttonType = UnifyButton.Type.MAIN
    }

    private fun setProfileImg(profile: ProfileUiModel) {
        with(mainBinding) {
            imgProfileImage.urlSrc = profile.imageCover

            if (profile.liveInfo.isLive) {
                viewProfileOuterRing.show()
                textLive.show()

                val appLink = profile.liveInfo.channelLink.appLink
                textLive.setOnClickListener { addLiveClickListener(appLink) }
                imgProfileImage.setOnClickListener { addLiveClickListener(appLink) }
            } else {
                viewProfileOuterRing.visibility = View.INVISIBLE
                textLive.hide()

                textLive.setOnClickListener(null)
                imgProfileImage.setOnClickListener{
                    userProfileTracker.clickProfilePicture(userSession.userId, self = viewModel.isSelfProfile, profile.liveInfo.channelId)
                }
            }
        }
    }

    private fun setHeader() {
        mainBinding.headerProfile.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
                userProfileTracker.clickBack(userSession.userId, self = viewModel.isSelfProfile)
            }

            val imgShare = addRightIcon(0)

            imgShare.clearImage()
            imgShare.setImageDrawable(getIconUnifyDrawable(context, IconUnify.SHARE_MOBILE))
            imgShare.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
                ),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            imgShare.setOnClickListener {
                showUniversalShareBottomSheet()
                userProfileTracker.clickShare(userSession.userId, self = viewModel.isSelfProfile)
                userProfileTracker.clickShareButton(userSession.userId, self = viewModel.isSelfProfile)
                userProfileTracker.viewShareChannel(userSession.userId, self = viewModel.isSelfProfile)
            }

            val imgMenu = addRightIcon(0)

            imgMenu.clearImage()
            imgMenu.setImageDrawable(getIconUnifyDrawable(context, IconUnify.MENU_HAMBURGER))

            imgMenu.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
                ),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            imgMenu.setOnClickListener {
                userProfileTracker.clickBurgerMenu(userSession.userId, self = viewModel.isSelfProfile)
                RouteManager.route(activity, APPLINK_MENU)
            }
        }
    }

    private fun doFollowUnfollow(isFromLogin: Boolean) {
        if(viewModel.isFollowed.not() || isFromLogin)
            userProfileTracker.clickFollow(userSession.userId, viewModel.isSelfProfile)
        else userProfileTracker.clickUnfollow(userSession.userId, viewModel.isSelfProfile)

        submitAction(UserProfileAction.ClickFollowButton(isFromLogin))
    }

    private fun showGlobalError(type: Int) {
        with(binding) {
            rootContainer.displayedChild = PAGE_ERROR
            globalError.setType(type)
            globalError.show()

            globalError.setActionClickListener {
                binding.rootContainer.displayedChild = PAGE_LOADING
                refreshLandingPageData()
            }
        }
    }

    private fun getDefaultErrorMessage(): String {
        return requireContext().getString(abstractionR.string.default_request_error_unknown)
    }

    private fun getUsernameWithAdd(): String {
        return requireContext().getString(R.string.up_username_template, viewModel.profileUsername)
    }

    override fun getScreenName(): String {
        return ""
    }

    private fun submitAction(action: UserProfileAction) {
        viewModel.submitAction(action)
    }

    private fun goToFollowingFollowerPage(isFollowers: Boolean) {
        if(isFollowers) userProfileTracker.clickFollowers(userSession.userId, self = viewModel.isSelfProfile)
        else userProfileTracker.clickFollowing(userSession.userId, self = viewModel.isSelfProfile)

        startActivity(activity?.let {
            FollowerFollowingListingActivity.getCallingIntent(
                it,
                getFollowersBundle(isFollowers)
            )
        })
    }

    private fun getFollowersBundle(isFollowers: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString(EXTRA_DISPLAY_NAME, viewModel.displayName)
        bundle.putString(EXTRA_USER_NAME, viewModel.profileUsername)
        bundle.putString(EXTRA_USER_ID, viewModel.profileUserID)
        bundle.putString(EXTRA_PROFILE_USER_ID, viewModel.profileUserID)
        bundle.putString(EXTRA_TOTAL_FOLLOWINGS, viewModel.totalFollowing)
        bundle.putString(EXTRA_TOTAL_FOLLOWERS, viewModel.totalFollower)
        bundle.putBoolean(EXTRA_IS_FOLLOWERS, isFollowers)
        return bundle
    }

    override fun onRetryPageLoad(pageNumber: Int) {

    }

    override fun onEmptyList(rawObject: Any?) {
        mainBinding.userPostContainer.displayedChild = PAGE_EMPTY
    }

    override fun onStartFirstPageLoad() {
        mainBinding.userPostContainer.displayedChild = PAGE_LOADING
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        mainBinding.userPostContainer.displayedChild = PAGE_CONTENT
    }

    override fun onStartPageLoad(pageNumber: Int) {

    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
    }

    override fun onError(pageNumber: Int) {
    }

    /**
     * OFFSET_USERINFO = 64dp(Profile) + 10dp(PaddingTop) + 16dp (margin top username) + 2dp (margin top userid) +
     *  24dp(user name line height) + 20dp(userid line height)
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN_TO_FOLLOW && resultCode == Activity.RESULT_OK) {
            doFollowUnfollow(isFromLogin = true)
        }
        else if(requestCode == REQUEST_CODE_LOGIN_TO_SET_REMINDER && resultCode == Activity.RESULT_OK) {
            submitAction(UserProfileAction.ClickUpdateReminder(true))
        }
        else if(requestCode == REQUEST_CODE_PLAY_ROOM && resultCode == Activity.RESULT_OK) {
            val channelId = data?.extras?.getString(EXTRA_CHANNEL_ID) ?: return
            val totalView = data.extras?.getString(EXTRA_TOTAL_VIEW)
            val isReminderSet = data.extras?.getBoolean(EXTRA_IS_REMINDER, false)

            mAdapter.updatePlayWidgetLatestData(channelId, totalView, isReminderSet)
        }
    }

    private fun showUniversalShareBottomSheet() {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@UserProfileFragment)
            userSession.userId.ifEmpty { "0" }.let {
                setUtmCampaignData(
                    PAGE_NAME_PROFILE,
                    it,
                    viewModel.profileUserID,
                    FEATURE_SHARE
                )
            }
            setMetaData(
                tnTitle = viewModel.displayName,
                tnImage = viewModel.profileCover,
            )
            setOgImageUrl(viewModel.profileCover)
        }
        universalShareBottomSheet?.show(fragmentManager, this, screenShotDetector)
    }

    companion object {
        const val PAGE_NAME_PROFILE = "UserProfile"
        const val FEATURE_SHARE = "share"
        const val VAL_FEEDS_PROFILE = "feeds-profile"
        const val VAL_SOURCE_BUYER = "buyer"
        const val EXTRA_DISPLAY_NAME = "display_name"
        const val EXTRA_TOTAL_FOLLOWERS = "total_followers"
        const val EXTRA_TOTAL_FOLLOWINGS = "total_following"
        const val EXTRA_USER_NAME = "user_name"
        const val EXTRA_USER_ID = "userid"
        const val EXTRA_PROFILE_USER_ID = "profileUserid"
        const val EXTRA_IS_FOLLOWERS = "is_followers"
        const val APPLINK_MENU = "tokopedia://navigation/main"
        const val APPLINK_PROFILE = "tokopedia://setting/profile"
        const val OFFSET_USERINFO = 136F
        const val REQUEST_CODE_LOGIN_TO_FOLLOW = 1
        const val REQUEST_CODE_LOGIN_TO_SET_REMINDER = 2
        const val REQUEST_CODE_USER_PROFILE = 99
        const val EXTRA_POSITION_OF_PROFILE = "profile_position"
        const val EXTRA_FOLLOW_UNFOLLOW_STATUS = "follow_unfollow_status"
        const val EXTRA_VALUE_IS_FOLLOWED = "is_followed"
        const val EXTRA_VALUE_IS_NOT_FOLLOWED = "is_not_followed"
        private const val LOADING = -94567

        const val PAGE_CONTENT = 0
        const val PAGE_ERROR = 2
        const val PAGE_LOADING = 1
        const val PAGE_EMPTY = 3
        const val SEE_ALL_LINE = 3
        const val MAX_LINE = 20
        const val SUCCESS_STATUS = 200
        
        private const val REQUEST_CODE_PLAY_ROOM = 123
        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_IS_REMINDER = "EXTRA_IS_REMINDER"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"

        private const val TAG = "UserProfileFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): UserProfileFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UserProfileFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UserProfileFragment::class.java.name
            ).apply {
                arguments = bundle
            } as UserProfileFragment
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val desc = buildString {
            append("Lihat foto & video menarik dari Tokopedia ${viewModel.displayName}")
            if(viewModel.profileUsername.isBlank()) append(" (${getUsernameWithAdd()})")
            append(", yuk! \uD83D\uDE0D")
        }

        val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
            type = LinkerData.USER_PROFILE_SOCIAL
            uri = viewModel.profileWebLink
            id = viewModel.profileUsername
            //set and share in the Linker Data
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            ogTitle = if (viewModel.profileUsername.isBlank()) {
                viewModel.displayName
            } else {
                "${viewModel.displayName} (${getUsernameWithAdd()})"
            }
            ogDescription = "${viewModel.totalFollower} Follower ${viewModel.totalFollowing} Following ${viewModel.totalPost} Post"
            if (shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        })
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    context?.let {

                        var shareString = desc + "\n" + linkerShareData?.shareUri
                        SharingUtil.executeShareIntent(
                            shareModel,
                            linkerShareData,
                            activity,
                            view,
                            shareString
                        )
                        // send gtm trackers if you want to

                        when(UniversalShareBottomSheet.getShareBottomSheetType()){
                            UniversalShareBottomSheet.SCREENSHOT_SHARE_SHEET ->{
                                userProfileTracker.clickChannelScreenshotShareBottomsheet(userSession.userId, self = viewModel.isSelfProfile)
                            }
                            UniversalShareBottomSheet.CUSTOM_SHARE_SHEET ->{
                                shareModel.channel?.let { it1 ->
                                    userProfileTracker.clickShareChannel(userSession.userId, self = viewModel.isSelfProfile, it1)
                                }
                            }
                        }
                        universalShareBottomSheet?.dismiss()
                    }
                }

                override fun onError(linkerError: LinkerError?) {
                    //Most of the error cases are already handled for you. Let me know if you want to add your own error handling.
                }
            })
        )
    }

    override fun screenShotTaken() {
        showUniversalShareBottomSheet()
        userProfileTracker.viewScreenshotShareBottomsheet(userSession.userId, self = viewModel.isSelfProfile)
        //add tracking for the screenshot bottom sheet
    }

    override fun permissionAction(action: String, label: String) {
        //add tracking for the permission dialog for screenshot sharing
        userProfileTracker.clickAccessMedia(userSession.userId, self = viewModel.isSelfProfile, label)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        screenShotDetector?.onRequestPermissionsResult(requestCode, grantResults, this)
    }

    override fun onCloseOptionClicked() {
//        TODO gtm tracking
        //This method will be mostly used for GTM Tracking stuff. So add the tracking accordingly
        //this will give you the bottomsheet type : if it's screenshot or general
        when(UniversalShareBottomSheet.getShareBottomSheetType()){
            UniversalShareBottomSheet.SCREENSHOT_SHARE_SHEET ->{
                userSession.userId.let { UserProfileTracker().clickCloseScreenshotShareBottomsheet(it, self = viewModel.isSelfProfile) }
            }
            UniversalShareBottomSheet.CUSTOM_SHARE_SHEET ->{
                userSession.userId.let { UserProfileTracker().clickCloseShareButton(it, self = viewModel.isSelfProfile) }
            }
        }
    }

    override fun updatePostReminderStatus(channelId: String, isActive: Boolean, pos: Int) {
        submitAction(UserProfileAction.SaveReminderActivityResult(channelId, pos, isActive))

        if(userSession.isLoggedIn.not()){
            startActivityForResult(
                RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN_TO_SET_REMINDER,
            )
        }
        else{
            submitAction(UserProfileAction.ClickUpdateReminder(false))
        }
    }

    override fun onPlayWidgetLargeClick(appLink: String) {
        val intent = RouteManager.getIntent(context, appLink)
        startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM)
    }

    override fun expandFab() {
        mainBinding.fabUserProfile.expand()
    }

    override fun shrinkFab() {
        mainBinding.fabUserProfile.shrink()
    }
}