package com.tokopedia.people.views

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
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
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.di.UserProfileModule
import com.tokopedia.people.itemDecoration.GridSpacingItemDecoration
import com.tokopedia.people.model.Profile
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.views.UserProfileActivity.Companion.EXTRA_USERNAME
import com.tokopedia.unifycomponents.*
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.feedcomponent.bottomsheets.onboarding.FeedUserCompleteOnboardingBottomSheet
import com.tokopedia.feedcomponent.bottomsheets.onboarding.FeedUserTnCOnboardingBottomSheet

class UserProfileFragment : BaseDaggerFragment(),
    View.OnClickListener,
    AdapterCallback,
    ShareBottomsheetListener,
    ScreenShotListener,
    PermissionListener,
    UserPostBaseAdapter.PlayWidgetCallback,
    FeedPlusContainerListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var feedFloatingButtonManager: FeedFloatingButtonManager

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
    var userSession: UserSessionInterface? = null
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
        initInjector()
        userProfileTracker = UserProfileTracker()
        return inflater.inflate(R.layout.up_fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSession = UserSession(context)
        feedFloatingButtonManager.setInitialData(this)

        userId = userSession?.userId?:""
        container = view.findViewById(R.id.container)
        userPostContainer = view.findViewById(R.id.vp_rv_post)
        globalError = view.findViewById(R.id.global_error)
        globalErrorPost = view.findViewById(R.id.global_error_post)
        appBarLayout = view.findViewById(R.id.app_bar_layout)
        swipeRefresh = view.findViewById(R.id.swipe_refresh_layout)
        feedFab = view.findViewById(R.id.up_feed_floating_button)

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
            viewModel.getUserDetails(it, isRefreshPost)
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
        view?.findViewById<View>(R.id.btn_action_follow)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_see_more)?.setOnClickListener(this)
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
        addUserProfileObserver()
        addListObserver()
        addDoFollowedObserver()
        addDoUnFollowedObserver()
        addTheyFollowedObserver()
        addProfileHeaderErrorObserver()
        addSocialFollowErrorObserver()
        addSocialUnFollowErrorObserver()
        addUserPostObserver()
        adduserPostErrorObserver()
        addVideoPostReminderUpdateObserver()
        addPostReminderErrorObserver()

    }

    private fun addPostReminderErrorObserver() {
        viewModel.postReminderErrorMessageLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        view?.let { it1 ->
                            Toaster.build(
                                it1,
                                requireContext().getString(com.tokopedia.people.R.string.up_error_local_error),
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR
                            ).show()
                        }
                    }
                }
            }
        })
    }

    private fun addUserProfileObserver() =
        viewModel.userDetailsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {
                        if (isSwipeRefresh == true) {
                            //TODO show shimmer
                        }
                    }
                    is ErrorMessage -> {

                    }
                    is Success -> {
                        if (isSwipeRefresh == true) {
                            view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.isRefreshing =
                                false
                            isSwipeRefresh = !isSwipeRefresh!!
                        } else {
                            //Hide shimmer
                        }

                        container?.displayedChild = PAGE_CONTENT
                        setMainUi(it.data)
                        viewModel.getFollowingStatus(mutableListOf(profileUserId))
                    }
                }
            }
        })

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


    private fun addDoFollowedObserver() =
        viewModel.profileDoFollowLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {

                    }
                    is Success -> {
                        if (it.data.profileFollowers.errorCode.isBlank()) {
                            updateToFollowUi()
                            refreshLandingPageData()
                        }
                    }
                    is ErrorMessage -> {

                    }
                }
            }
        })

    private fun addDoUnFollowedObserver() =
        viewModel.profileDoUnFollowLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {

                    }
                    is Success -> {
                        if (it.data.profileFollowers.errorCode.isBlank()) {
                            updateToUnFollowUi()
                            refreshLandingPageData()
                        }
                    }

                    is ErrorMessage -> {

                    }
                }
            }
        })

    private fun addTheyFollowedObserver() =
        viewModel.profileTheyFollowLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {

                    }
                    is Success -> {
                        setActionButton(it.data)
                    }
                    is ErrorMessage -> {

                    }
                }
            }
        })

    private fun addProfileHeaderErrorObserver() =
        viewModel.profileHeaderErrorMessageLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        container?.displayedChild = PAGE_ERROR
                        globalError?.setType(NO_CONNECTION)
                        globalError?.show()

                        globalError?.setActionClickListener {
                            container?.displayedChild = PAGE_LOADING
                            refreshLandingPageData()
                        }
                    }
                    is IllegalStateException -> {
                        container?.displayedChild = PAGE_ERROR
                        globalError?.setType(PAGE_FULL)
                        globalError?.show()

                        globalError?.setActionClickListener {
                            container?.displayedChild = PAGE_LOADING
                            refreshLandingPageData()
                        }
                    }
                    is RuntimeException -> {
                        when (it.localizedMessage?.toIntOrNull()) {
                            ReponseStatus.NOT_FOUND -> {
                                container?.displayedChild = PAGE_ERROR
                                globalError?.setType(PAGE_NOT_FOUND)
                                globalError?.show()

                                globalError?.setActionClickListener {
                                    container?.displayedChild = PAGE_LOADING
                                    refreshLandingPageData()
                                }
                            }
                            ReponseStatus.INTERNAL_SERVER_ERROR -> {
                                container?.displayedChild = PAGE_ERROR
                                globalError?.setType(SERVER_ERROR)
                                globalError?.show()

                                globalError?.setActionClickListener {
                                    container?.displayedChild = PAGE_LOADING
                                    refreshLandingPageData()
                                }
                            }
                            else -> {
                                container?.displayedChild = PAGE_ERROR
                                globalError?.setType(SERVER_ERROR)
                                globalError?.show()

                                globalError?.setActionClickListener {
                                    container?.displayedChild = PAGE_LOADING
                                    refreshLandingPageData()
                                }
                            }
                        }
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

    private fun addSocialFollowErrorObserver() =
        viewModel.followErrorMessageLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                val snackBar = Toaster.build(
                    btnAction as View,
                    getString(com.tokopedia.people.R.string.up_error_follow),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                )

                snackBar.show()

                updateToUnFollowUi()
            }
        })

    private fun addSocialUnFollowErrorObserver() =
        viewModel.unFollowErrorMessageLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                val snackBar = Toaster.build(
                    btnAction as View,
                    getString(com.tokopedia.people.R.string.up_error_unfollow),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                )

                snackBar.show()

                updateToFollowUi()
            }
        })

    private fun addVideoPostReminderUpdateObserver() =
        viewModel.postReminderLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {

                    }
                    is Success -> {
                        if (it?.data?.playToggleChannelReminder?.header?.status == SUCCESS_STATUS) {
                            Toaster.build(
                                btnAction as View,
                                it.data.playToggleChannelReminder.header.message,
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_NORMAL
                            ).show()
                        }
                    }
                    is ErrorMessage -> {

                    }
                }
            }
        })

    private fun addLiveClickListener(appLink: String) = View.OnClickListener {
        RouteManager.route(context, appLink)
    }

    private fun addProfileClickListener(appLink: String, userId: String) = View.OnClickListener {
        RouteManager.route(activity, appLink)
    }

    private fun addDoFollowClickListener(userIdEnc: String, isFollowed: Boolean) =
        View.OnClickListener {
            if (userSession?.isLoggedIn == false) {
                startActivityForResult(
                    RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN
                )
                return@OnClickListener
            }

            if (isFollowed) {
                activity?.intent?.putExtra(EXTRA_FOLLOW_UNFOLLOW_STATUS, EXTRA_VALUE_IS_NOT_FOLLOWED)

                userProfileTracker?.clickUnfollow(userId, profileUserId == userId)
                viewModel.doUnFollow(userIdEnc)
                updateToUnFollowUi()
            } else {
                activity?.intent?.putExtra(EXTRA_FOLLOW_UNFOLLOW_STATUS, EXTRA_VALUE_IS_FOLLOWED)
                userProfileTracker?.clickFollow(userId, profileUserId == userId)
                viewModel.doFollow(userIdEnc)
                updateToFollowUi()
            }
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

    private fun setMainUi(data: ProfileHeaderBase) {

        val textBio = view?.findViewById<TextView>(R.id.text_bio)
        val textSeeMore = view?.findViewById<TextView>(R.id.text_see_more)
        val textUserName = view?.findViewById<TextView>(R.id.text_user_name)
        val textDisplayName = view?.findViewById<TextView>(R.id.text_display_name)
        val textContentCount = view?.findViewById<TextView>(R.id.text_content_count)
        val textFollowerCount = view?.findViewById<TextView>(R.id.text_follower_count)
        val textFollowingCount = view?.findViewById<TextView>(R.id.text_following_count)

        btnAction = view?.findViewById<UnifyButton>(R.id.btn_action_follow)

        if (data.profileHeader.profile.username.isNotBlank()) {
            textUserName?.show()
            textUserName?.text = "@" + data.profileHeader.profile.username
        }
        else{
            textUserName?.hide()
        }

        textDisplayName?.text = data.profileHeader.profile.name
        textContentCount?.text =
            UserProfileUtils.getFormattedNumber(data.profileHeader.stats.totalPost)
        textFollowerCount?.text =
            UserProfileUtils.getFormattedNumber(data.profileHeader.stats.totalFollower)
        textFollowingCount?.text =
            UserProfileUtils.getFormattedNumber(data.profileHeader.stats.totalFollowing)

        userProfileTracker?.openUserProfile(screenName = "feed user profile", profileUserId, live = data.profileHeader.profile.liveplaychannel.islive)

        setProfileImg(data.profileHeader.profile)

        displayName = data.profileHeader.profile.name
        mAdapter.displayName = displayName
        userName = data.profileHeader.profile.username
        mAdapter.setUserName(data.profileHeader.profile.username)
        totalFollowers = data.profileHeader.stats.totalFollowerFmt
        totalPosts =  data.profileHeader.stats.totalPostFmt
        totalFollowings = data.profileHeader.stats.totalFollowingFmt
        profileImage = data.profileHeader.profile.imageCover
        profileUserId = data.profileHeader.profile.userID
        userWebLink = data.profileHeader.profile.sharelink.weblink
        textBio?.maxLines = MAX_LINE

        if(data.profileHeader.profile.biography.isNullOrEmpty()){
            textBio?.hide()
        }
        else{
            textBio?.show()
        }

        data.profileHeader.profile.biography = data.profileHeader.profile.biography.replace("\n", "<br />")
        val textBioString = context?.let {
            HtmlLinkHelper(
                it,
                data.profileHeader.profile.biography
            ).spannedString
        } ?: ""

        textBio?.let {
            textBio?.text = context?.let {
                HtmlLinkHelper(
                    it,
                    data.profileHeader.profile.biography
                ).spannedString
            }

            if (textBioString.lines().count() > SEE_ALL_LINE) {
                if (isViewMoreClickedBio == true) {
                    textBio.maxLines = MAX_LINE
                    textSeeMore?.hide()
                } else {
                    textBio.maxLines = SEE_ALL_LINE
                    textSeeMore?.show()
                }
            } else {
                textSeeMore?.hide()
            }
        }


        if (userSession?.isLoggedIn == false) {
            updateToUnFollowUi()
            btnAction?.setOnClickListener {
                startActivityForResult(
                    RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN
                )
            }
        }
        headerProfile?.title = data.profileHeader.profile.name

    }

    private fun isProfileButtonVisible() : Boolean{
        return false
    }

    private fun setActionButton(followProfile: UserProfileIsFollow) {

        if (!userSession?.userId.isNullOrBlank() && followProfile.profileHeader.items[0].userID == userSession?.userId) {
            btnAction?.text = getString(com.tokopedia.people.R.string.up_btn_profile)
            btnAction?.buttonVariant = UnifyButton.Variant.GHOST
            btnAction?.buttonType = UnifyButton.Type.ALTERNATE

            if(isProfileButtonVisible()){
                btnAction?.show()
            } else {
                btnAction?.hide()
            }


            btnAction?.setOnClickListener(
                addProfileClickListener(
                    APPLINK_PROFILE,
                    followProfile.profileHeader.items[0].userID
                )
            )
        } else {
            val isFollowed = followProfile.profileHeader.items[0].status
            if (isFollowed) {
                updateToFollowUi()
            } else {
                updateToUnFollowUi()
            }

            btnAction?.setOnClickListener(
                addDoFollowClickListener(
                    followProfile.profileHeader.items[0].encryptedUserID,
                    isFollowed
                )
            )
        }
    }

    private fun setProfileImg(profile: Profile) {
        mAdapter.activityId = profile.liveplaychannel.liveplaychannelid

        if (profile == null
            || profile.liveplaychannel == null
            || profile.liveplaychannel.liveplaychannellink == null
        ) {
            return
        }

        val textLive = view?.findViewById<TextView>(R.id.text_live)
        val viewLiveRing = view?.findViewById<View>(R.id.view_profile_outer_ring)
        val imgProfile = view?.findViewById<ImageUnify>(R.id.img_profile_image)

        imgProfile?.urlSrc = profile.imageCover

        if (profile.liveplaychannel.islive) {
            viewLiveRing?.show()
            textLive?.show()

            textLive?.setOnClickListener(addLiveClickListener(profile.liveplaychannel.liveplaychannellink.applink))
            textLive?.setOnClickListener(addLiveClickListener(profile.liveplaychannel.liveplaychannellink.applink))
            imgProfile?.setOnClickListener(addLiveClickListener(profile.liveplaychannel.liveplaychannellink.applink))
        } else {
            viewLiveRing?.visibility = View.INVISIBLE
            textLive?.hide()

            textLive?.setOnClickListener(null)
            textLive?.setOnClickListener(null)
            imgProfile?.setOnClickListener{
                userProfileTracker?.clickProfilePicture(userId, profileUserId == userId, profile.liveplaychannel.liveplaychannelid)
            }
        }
    }


    private fun setHeader() {
        headerProfile = view?.findViewById<HeaderUnify>(R.id.header_profile)
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

    override fun initInjector() {
        DaggerUserProfileComponent.builder()
            .baseAppComponent(
                (requireContext().applicationContext as BaseMainApplication).baseAppComponent
            )
            .userProfileModule(UserProfileModule(requireContext().applicationContext))
            .build()
            .inject(this)
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
            userSession?.userId?.ifEmpty { "0" }?.let {
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
            setOgImageUrl(profileImage ?: "")
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

        fun newInstance(extras: Bundle): Fragment {
            val fragment = UserProfileFragment()
            fragment.arguments = extras
            return fragment
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
                userSession?.userId?.let { UserProfileTracker().clickCloseScreenshotShareBottomsheet(it, profileUserId == it) }
            }
            UniversalShareBottomSheet.CUSTOM_SHARE_SHEET ->{
                userSession?.userId?.let { UserProfileTracker().clickCloseShareButton(it, profileUserId == it) }
            }
        }
    }

    override fun updatePostReminderStatus(channelId: String, isActive: Boolean, pos: Int) {
        if(userSession?.isLoggedIn == false){
            startActivityForResult(
                RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN
            )
        }
        else{

            viewModel.updatePostReminderStatus(channelId, isActive)
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