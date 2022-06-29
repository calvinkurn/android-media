package com.tokopedia.people.views.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.util.manager.FeedFloatingButtonManager
import com.tokopedia.feedcomponent.view.base.FeedPlusContainerListener
import com.tokopedia.feedcomponent.view.custom.FeedFloatingButton
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_FULL
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_NOT_FOUND
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.header.HeaderUnify
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
import com.tokopedia.people.utils.UserProfileUtils
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.utils.showToast
import com.tokopedia.people.utils.withCache
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.ProfileType
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import com.tokopedia.abstraction.R as abstractionR

class UserProfileFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val userSession: UserSessionInterface,
    private val feedFloatingButtonManager: FeedFloatingButtonManager,
) : BaseDaggerFragment(),
    View.OnClickListener,
    AdapterCallback,
    ShareBottomsheetListener,
    ScreenShotListener,
    PermissionListener,
    UserPostBaseAdapter.PlayWidgetCallback,
    FeedPlusContainerListener {

    private val gridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(activity, 2)
    }

    var landedUserName: String? = null
    var displayName: String = ""
    var userName: String = ""
    var userWebLink: String = ""
    var profileUserId: String = ""
    var profileImage: String = ""
    var totalFollowings: String = ""
    var totalFollowers: String = ""
    var totalPosts: String = ""
    var btnAction: UnifyButton? = null
    var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var recyclerviewPost: RecyclerView? = null
    private var headerProfile: HeaderUnify? = null
    private var appBarLayout: AppBarLayout? = null
    private var userId = ""
    private var container: ViewFlipper? = null
    private var userPostContainer: ViewFlipper? = null
    private var globalError: GlobalError? = null
    private var globalErrorPost: LocalLoad? = null
    private var isSwipeRefresh: Boolean? = null
    private var isNewlyCreated: Boolean? = false
    private var shouldRefreshRecyclerView: Boolean? = false
    private var isViewMoreClickedBio: Boolean? = false
    private var userProfileTracker: UserProfileTracker? = null
    private var screenShotDetector: ScreenshotDetector? = null
    private lateinit var swipeRefresh: SwipeToRefresh
    private lateinit var feedFab: FeedFloatingButton

    private lateinit var textBio: Typography
    private lateinit var textSeeMore: Typography
    private lateinit var textUserName: Typography
    private lateinit var textDisplayName: Typography
    private lateinit var textContentCount: Typography
    private lateinit var textFollowerCount: Typography
    private lateinit var textFollowingCount: Typography

    private lateinit var textLive: Typography
    private lateinit var viewLiveRing: View
    private lateinit var imgProfile: ImageUnify

    override fun initInjector() {
        /** No need since we alr have constructor injection */
    }

    private val viewModel: UserProfileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)
    }

    private val mAdapter: UserPostBaseAdapter by lazy {
        UserPostBaseAdapter(
            viewModel,
            this,
            userName,
            userProfileTracker,
            profileUserId,
            userId,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userProfileTracker = UserProfileTracker()
        return inflater.inflate(R.layout.up_fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedFloatingButtonManager.setInitialData(this)

        userId = userSession.userId
        container = view.findViewById(R.id.container)
        userPostContainer = view.findViewById(R.id.vp_rv_post)
        globalError = view.findViewById(R.id.global_error)
        globalErrorPost = view.findViewById(R.id.global_error_post)
        appBarLayout = view.findViewById(R.id.app_bar_layout)
        swipeRefresh = view.findViewById(R.id.swipe_refresh_layout)
        feedFab = view.findViewById(R.id.up_feed_floating_button)

        headerProfile = view.findViewById(R.id.header_profile)
        textBio = view.findViewById(R.id.text_bio)
        textSeeMore = view.findViewById(R.id.text_see_more)
        textUserName = view.findViewById(R.id.text_user_name)
        textDisplayName = view.findViewById(R.id.text_display_name)
        textContentCount = view.findViewById(R.id.text_content_count)
        textFollowerCount = view.findViewById(R.id.text_follower_count)
        textFollowingCount = view.findViewById(R.id.text_following_count)
        btnAction = view.findViewById(R.id.btn_action_follow)

        textLive = view.findViewById(R.id.text_live)
        viewLiveRing = view.findViewById(R.id.view_profile_outer_ring)
        imgProfile = view.findViewById(R.id.img_profile_image)

        initObserver()
        initListener()
        setHeader()

        if (arguments == null || requireArguments().getString(EXTRA_USERNAME).isNullOrBlank()) {
            //TODO show error page
            activity?.finish()
        }



        swipeRefresh.setOnRefreshListener {
            isSwipeRefresh = true
            refreshLandingPageData(true)
        }

        //Get landing page, profile header page
        isNewlyCreated = true;
        landedUserName = requireArguments().getString(EXTRA_USERNAME)
        refreshLandingPageData(true)
        container?.displayedChild = PAGE_LOADING

        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener {appBarLayout, verticalOffset ->
            shouldRefreshRecyclerView = verticalOffset == 0
        })

        view.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)
            ?.setOnChildScrollUpCallback(object : SwipeRefreshLayout.OnChildScrollUpCallback {
                override fun canChildScrollUp(parent: SwipeRefreshLayout, child: View?): Boolean {
                    if (recyclerviewPost != null) {
                        return recyclerviewPost!!.canScrollVertically(-1) || !shouldRefreshRecyclerView!!
                    }
                    return false
                }
            })
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
        recyclerviewPost?.removeOnScrollListener(feedFloatingButtonManager.scrollListener)
        feedFloatingButtonManager.cancel()
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
        landedUserName?.let {
            viewModel.submitAction(UserProfileAction.LoadProfile(it, isRefreshPost))
        }
    }

    private fun initListener() {
        view?.findViewById<View>(R.id.text_following_label)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_following_count)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_follower_label)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_follower_count)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.img_profile_image)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_live)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.view_profile_outer_ring)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_see_more)?.setOnClickListener(this)
        btnAction?.setOnClickListener {
            if(viewModel.isFollow) userProfileTracker?.clickUnfollow(userId, viewModel.isSelfProfile)
            else userProfileTracker?.clickFollow(userId, viewModel.isSelfProfile)

            viewModel.submitAction(UserProfileAction.ClickFollowButton)
        }
        feedFab.setOnClickListener {
            FeedUserTnCOnboardingBottomSheet.getFragment(
                childFragmentManager,
                requireActivity().classLoader
            ).showNow(childFragmentManager)
//            FeedUserCompleteOnboardingBottomSheet.getFragment(
//                childFragmentManager,
//                requireActivity().classLoader
//            ).showNow(childFragmentManager)
        }

        recyclerviewPost?.addOnScrollListener(feedFloatingButtonManager.scrollListener)
//        recyclerviewPost?.let { feedFloatingButtonManager.setDelayForExpandFab(it) }
    }

    private fun initUserPost(userId: String) {
        recyclerviewPost = view?.findViewById(R.id.recycler_view)
        gridLayoutManager.spanSizeLookup = getSpanSizeLookUp()


        recyclerviewPost?.layoutManager = gridLayoutManager
        if (recyclerviewPost?.itemDecorationCount == 0) {
            context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1)
                ?.let {
                    recyclerviewPost?.addItemDecoration(GridSpacingItemDecoration(2, it, false))
                }
        }
        recyclerviewPost?.adapter = mAdapter
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

    private fun addUserPostObserver() =
        viewModel.userPostLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    initUserPost(profileUserId)
                }
            }
        })

    private fun addListObserver() =
        viewModel.playPostContentLiveData.observe(viewLifecycleOwner, Observer {
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
        })

    private fun adduserPostErrorObserver() =
        viewModel.userPostErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        userPostContainer?.displayedChild = PAGE_ERROR

                        globalErrorPost?.refreshBtn?.setOnClickListener {
                            userPostContainer?.displayedChild = PAGE_LOADING
                            profileUserId?.let { it1 -> initUserPost(it1) }
                        }
                    }
                    is IllegalStateException -> {
                        userPostContainer?.displayedChild = PAGE_ERROR

                        globalErrorPost?.refreshBtn?.setOnClickListener {
                            userPostContainer?.displayedChild = PAGE_LOADING
                            profileUserId?.let { it1 -> initUserPost(it1) }
                        }
                    }
                    is RuntimeException -> {
                        when (it.localizedMessage?.toIntOrNull()) {
                            ReponseStatus.NOT_FOUND -> {
                                userPostContainer?.displayedChild = PAGE_ERROR

                                globalErrorPost?.refreshBtn?.setOnClickListener {
                                    userPostContainer?.displayedChild = PAGE_LOADING
                                    profileUserId?.let { it1 -> initUserPost(it1) }
                                }
                            }
                            ReponseStatus.INTERNAL_SERVER_ERROR -> {
                                userPostContainer?.displayedChild = PAGE_ERROR

                                globalErrorPost?.refreshBtn?.setOnClickListener {
                                    userPostContainer?.displayedChild = PAGE_LOADING
                                    profileUserId?.let { it1 -> initUserPost(it1) }
                                }
                            }
                            else -> {
                                userPostContainer?.displayedChild = PAGE_ERROR

                                globalErrorPost?.refreshBtn?.setOnClickListener {
                                    userPostContainer?.displayedChild = PAGE_LOADING
                                    profileUserId?.let { it1 -> initUserPost(it1) }
                                }
                            }
                        }
                    }
                }
            }
        })

    private fun addLiveClickListener(appLink: String) = View.OnClickListener {
        RouteManager.route(context, appLink)
    }

    /** Render UI */
    private fun renderProfileInfo(
        prev: ProfileUiModel?,
        curr: ProfileUiModel,
    ) {
        if(prev == curr || curr == ProfileUiModel.Empty) return

        userProfileTracker?.openUserProfile(
            screenName = "feed user profile",
            profileUserId,
            live = curr.liveInfo.isLive
        )

        if (isSwipeRefresh == true) {
            swipeRefresh.isRefreshing = false
            isSwipeRefresh = !isSwipeRefresh!!
        }

        container?.displayedChild = PAGE_CONTENT


        /** Setup Profile Info */
        setProfileImg(curr)

        textUserName.shouldShowWithAction(curr.username.isNotBlank()) {
            textUserName.text = getString(R.string.up_username_template, curr.username)
        }
        textDisplayName.text = curr.name
        textContentCount.text = UserProfileUtils.getFormattedNumber(curr.stats.totalPost)
        textFollowerCount.text = UserProfileUtils.getFormattedNumber(curr.stats.totalFollower)
        textFollowingCount.text = UserProfileUtils.getFormattedNumber(curr.stats.totalFollowing)

        displayName = curr.name
        mAdapter.displayName = displayName
        userName = curr.username
        mAdapter.setUserName(curr.username)

        /** TODO: will be removed soon */
        totalFollowers = curr.stats.totalFollowerFmt
        totalPosts =  curr.stats.totalPostFmt
        totalFollowings = curr.stats.totalFollowingFmt
        profileImage = curr.imageCover
        profileUserId = curr.userID
        userWebLink = curr.shareLink.webLink

        /** Setup Bio */
        val displayBioText = HtmlLinkHelper(requireContext(), curr.biography).spannedString
        textBio.text = displayBioText

        if (displayBioText?.lines()?.count().orZero() > SEE_ALL_LINE) {
            if (isViewMoreClickedBio == true) {
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

        headerProfile?.title = curr.name
    }

    private fun renderFollowInfo(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {

        if(prev?.followInfo == value.followInfo &&
            prev.profileType == value.profileType
        ) return

        when(value.profileType) {
            ProfileType.NotLoggedIn -> btnAction?.show()
            ProfileType.OtherUser -> {
                with(value) {
                    btnAction?.show()
                    if (followInfo.status)
                        updateToFollowUi()
                    else
                        updateToUnFollowUi()
                }
            }
            ProfileType.Unknown,
            ProfileType.Self -> btnAction?.hide()
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

        feedFab.showWithCondition(
            value.profileType == ProfileType.Self && value.profileWhitelist.isWhitelist
        )
    }

    private fun updateToFollowUi() {
        btnAction?.text =  getString(R.string.up_btn_text_following)
        btnAction?.buttonVariant = UnifyButton.Variant.GHOST
        btnAction?.buttonType = UnifyButton.Type.ALTERNATE
    }

    private fun updateToUnFollowUi() {
        btnAction?.text = getString(R.string.up_btn_text_follow)
        btnAction?.buttonVariant = UnifyButton.Variant.FILLED
        btnAction?.buttonType = UnifyButton.Type.MAIN
    }

    private fun setProfileImg(profile: ProfileUiModel) {
        mAdapter.activityId = profile.liveInfo.channelId

        imgProfile.urlSrc = profile.imageCover

        if (profile.liveInfo.isLive) {
            viewLiveRing.show()
            textLive.show()

            textLive.setOnClickListener(addLiveClickListener(profile.liveInfo.channelLink.appLink))
            imgProfile.setOnClickListener(addLiveClickListener(profile.liveInfo.channelLink.appLink))
        } else {
            viewLiveRing.visibility = View.INVISIBLE
            textLive.hide()

            textLive.setOnClickListener(null)
            textLive.setOnClickListener(null)
            imgProfile.setOnClickListener{
                userProfileTracker?.clickProfilePicture(userId, profileUserId == userId, profile.liveInfo.channelId)
            }
        }
    }

    private fun setHeader() {
        headerProfile?.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
                userProfileTracker?.clickBack(userId, self = profileUserId == userId)
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
                userProfileTracker?.clickShare(userId, self = profileUserId == userId)
                userProfileTracker?.clickShareButton(userId, self = profileUserId == userId)
                userProfileTracker?.viewShareChannel(userId, profileUserId == userId)
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
                userProfileTracker?.clickBurgerMenu(userId, self = profileUserId == userId)
                RouteManager.route(activity, APPLINK_MENU)
            }
        }
    }

    private fun showGlobalError(type: Int) {
        container?.displayedChild = PAGE_ERROR
        globalError?.setType(type)
        globalError?.show()

        globalError?.setActionClickListener {
            container?.displayedChild = PAGE_LOADING
            refreshLandingPageData()
        }
    }

    private fun getDefaultErrorMessage(): String {
        return requireContext().getString(abstractionR.string.default_request_error_unknown)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onResume() {
        super.onResume()

        if (isNewlyCreated == true) {
            isNewlyCreated = false
        } else {
            refreshLandingPageData()
        }
    }

    override fun onClick(source: View) {
        when (source.id) {
            R.id.text_following_count, R.id.text_following_label -> {
                userProfileTracker?.clickFollowing(userId, profileUserId == userId)
                startActivity(activity?.let {
                    FollowerFollowingListingActivity.getCallingIntent(
                        it,
                        getFollowersBundle(false)
                    )
                })
            }

            R.id.text_follower_count, R.id.text_follower_label -> {
                userProfileTracker?.clickFollowers(userId, profileUserId == userId)
                UserProfileTracker().openFollowersTab(
                    profileUserId
                )
                startActivity(activity?.let {
                    FollowerFollowingListingActivity.getCallingIntent(
                        it,
                        getFollowersBundle(true)
                    )
                })
            }

            R.id.text_see_more -> {
                userProfileTracker?.clickSelengkapnya(userId, profileUserId == userId)
                val textBio = view?.findViewById<TextView>(R.id.text_bio)
                val btnSeeAll = view?.findViewById<TextView>(R.id.text_see_more)
                textBio?.maxLines = MAX_LINE
                btnSeeAll?.hide()
                isViewMoreClickedBio = true;
            }
        }
    }

    private fun getFollowersBundle(isFollowers: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString(EXTRA_DISPLAY_NAME, displayName)
        bundle.putString(EXTRA_USER_NAME, userName)
        bundle.putString(EXTRA_USER_ID, profileUserId)
        bundle.putString(EXTRA_PROFILE_USER_ID, profileUserId)
        bundle.putString(EXTRA_TOTAL_FOLLOWINGS, totalFollowings)
        bundle.putString(EXTRA_TOTAL_FOLLOWERS, totalFollowers)
        bundle.putBoolean(EXTRA_IS_FOLLOWERS, isFollowers)
        return bundle
    }

    override fun onRetryPageLoad(pageNumber: Int) {

    }

    override fun onEmptyList(rawObject: Any?) {
        userPostContainer?.displayedChild = PAGE_EMPTY
    }

    override fun onStartFirstPageLoad() {
        userPostContainer?.displayedChild = PAGE_LOADING
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        userPostContainer?.displayedChild = PAGE_CONTENT
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
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            refreshLandingPageData()
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
                    profileUserId,
                    FEATURE_SHARE
                )
            }
            setMetaData(
                tnTitle = displayName,
                tnImage = profileImage
            )
            setOgImageUrl(profileImage)
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
        const val REQUEST_CODE_LOGIN = 1
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
        var desc = userName

        desc = if (desc.isBlank()) {
            "Lihat foto & video menarik dari Tokopedia $displayName, yuk! 😍"
        } else {
            "Lihat foto & video menarik dari Tokopedia $displayName (@$userName), yuk! 😍"
        }

        val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
            type = LinkerData.USER_PROFILE_SOCIAL
            uri = userWebLink
            id = this@UserProfileFragment.userName
            //set and share in the Linker Data
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            ogTitle = if (userName.isBlank()) {
                displayName
            } else {
                "$displayName (@$userName)"
            }
            ogDescription = "$totalFollowers Follower $totalFollowings Following $totalPosts Post"
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
                                userProfileTracker?.clickChannelScreenshotShareBottomsheet(userId, profileUserId == userId)
                            }
                            UniversalShareBottomSheet.CUSTOM_SHARE_SHEET ->{
                                shareModel.channel?.let { it1 ->
                                    userProfileTracker?.clickShareChannel(userId, profileUserId == userId, it1)
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
        userProfileTracker?.viewScreenshotShareBottomsheet(userId, profileUserId == userId)
        //add tracking for the screenshot bottom sheet
    }

    override fun permissionAction(action: String, label: String) {
        //add tracking for the permission dialog for screenshot sharing
        userProfileTracker?.clickAccessMedia(userId, profileUserId == userId, label)
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
                userSession.userId.let { UserProfileTracker().clickCloseScreenshotShareBottomsheet(it, profileUserId == it) }
            }
            UniversalShareBottomSheet.CUSTOM_SHARE_SHEET ->{
                userSession.userId.let { UserProfileTracker().clickCloseShareButton(it, profileUserId == it) }
            }
        }
    }

    override fun updatePostReminderStatus(channelId: String, isActive: Boolean, pos: Int) {
        if(userSession.isLoggedIn.not()){
            startActivityForResult(
                RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN
            )
        }
        else{
            viewModel.submitAction(UserProfileAction.ClickUpdateReminder(channelId, isActive))
            mAdapter.notifyItemChanged(pos)
        }
    }

    override fun onPlayWidgetLargeClick(appLink: String) {
        val intent = RouteManager.getIntent(context, appLink)
        startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM)
    }

    override fun expandFab() {
        feedFab.expand()
    }

    override fun shrinkFab() {
        feedFab.shrink()
    }
}