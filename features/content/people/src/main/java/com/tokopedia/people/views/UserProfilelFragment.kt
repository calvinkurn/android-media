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
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_FULL
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_NOT_FOUND
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.people.ErrorMessage
import com.tokopedia.people.Loading
import com.tokopedia.people.R
import com.tokopedia.people.Success
import com.tokopedia.people.UserProfileUtils
import com.tokopedia.people.di.DaggerUserProfileComponent
import com.tokopedia.people.di.UserProfileModule
import com.tokopedia.people.model.Profile
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.views.UserProfileActivity.Companion.EXTRA_USERNAME
import com.tokopedia.unifycomponents.*
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.abs

class UserProfileFragment : BaseDaggerFragment(), View.OnClickListener, AdapterCallback,
    ShareBottomsheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var landedUserName: String? = null
    var displayName: String = ""
    var userName: String = ""
    var userWebLink: String = ""
    var profileUserId: String = ""
    var profileImage: String = ""
    var totalFollowings: String = ""
    var totalFollowers: String = ""
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
    private var isViewMoreClickedBio: Boolean? = false
    private var userProfileTracker: UserProfileTracker? = null

    private val mPresenter: UserProfileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)
    }

    private val mAdapter: UserPostBaseAdapter by lazy {
        UserPostBaseAdapter(
            mPresenter,
            this,
            userName
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initInjector()
        return inflater.inflate(R.layout.up_fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view.findViewById(R.id.container)
        userPostContainer = view.findViewById(R.id.vp_rv_post)
        globalError = view.findViewById(R.id.global_error)
        globalErrorPost = view.findViewById(R.id.global_error_post)
        initObserver()
        initListener()
        setHeader()

        if (arguments == null || requireArguments().getString(EXTRA_USERNAME).isNullOrBlank()) {
            //TODO show error page
            activity?.finish()
        }

        userSession = UserSession(context)

        view.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.setOnRefreshListener {
            isSwipeRefresh = true
            refreshLandingPageData(true)
        }

        //Get landing page, profile header page
        isNewlyCreated = true;
        landedUserName = requireArguments().getString(EXTRA_USERNAME)
        refreshLandingPageData(true)
        container?.displayedChild = PAGE_LOADING

        view.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)
            ?.setOnChildScrollUpCallback(object : SwipeRefreshLayout.OnChildScrollUpCallback {
                override fun canChildScrollUp(parent: SwipeRefreshLayout, child: View?): Boolean {
                    if (recyclerviewPost != null) {
                        return recyclerviewPost!!.canScrollVertically(-1)
                    }
                    return false
                }
            })
    }

    private fun refreshLandingPageData(isRefreshPost: Boolean = false) {
        landedUserName?.let {
            mPresenter.getUserDetails(it, isRefreshPost)
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
    }

    private fun initUserPost(userId: String) {
        recyclerviewPost = view?.findViewById(R.id.recycler_view)
        recyclerviewPost?.layoutManager = GridLayoutManager(activity, 2)
        if (recyclerviewPost?.itemDecorationCount == 0) {
            context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
                ?.let {
                    recyclerviewPost?.addItemDecoration(PostItemDecoration(it))
                }
        }
        recyclerviewPost?.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.startDataLoading(userId)
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
    }

    private fun addUserProfileObserver() =
        mPresenter.userDetailsLiveData.observe(viewLifecycleOwner, Observer {
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
                        mPresenter.getFollowingStatus(mutableListOf(profileUserId))
                    }
                }
            }
        })

    private fun addUserPostObserver() =
        mPresenter.userPostLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    initUserPost(userId)
                }
            }
        })

    private fun addListObserver() =
        mPresenter.playPostContentLiveData.observe(viewLifecycleOwner, Observer {
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
        mPresenter.profileDoFollowLiveData.observe(viewLifecycleOwner, Observer {
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
        mPresenter.profileDoUnFollowLiveData.observe(viewLifecycleOwner, Observer {
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
        mPresenter.profileTheyFollowLiveData.observe(viewLifecycleOwner, Observer {
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
        mPresenter.profileHeaderErrorMessageLiveData.observe(viewLifecycleOwner, Observer {
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
        mPresenter.userPostErrorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        userPostContainer?.displayedChild = PAGE_ERROR

                        globalErrorPost?.refreshBtn?.setOnClickListener {
                            userPostContainer?.displayedChild = PAGE_LOADING
                            landedUserName?.let { it1 -> initUserPost(it1) }
                        }
                    }
                    is IllegalStateException -> {
                        userPostContainer?.displayedChild = PAGE_ERROR

                        globalErrorPost?.refreshBtn?.setOnClickListener {
                            userPostContainer?.displayedChild = PAGE_LOADING
                            landedUserName?.let { it1 -> initUserPost(it1) }
                        }
                    }
                    is RuntimeException -> {
                        when (it.localizedMessage?.toIntOrNull()) {
                            ReponseStatus.NOT_FOUND -> {
                                userPostContainer?.displayedChild = PAGE_ERROR

                                globalErrorPost?.refreshBtn?.setOnClickListener {
                                    userPostContainer?.displayedChild = PAGE_LOADING
                                    landedUserName?.let { it1 -> initUserPost(it1) }
                                }
                            }
                            ReponseStatus.INTERNAL_SERVER_ERROR -> {
                                userPostContainer?.displayedChild = PAGE_ERROR

                                globalErrorPost?.refreshBtn?.setOnClickListener {
                                    userPostContainer?.displayedChild = PAGE_LOADING
                                    landedUserName?.let { it1 -> initUserPost(it1) }
                                }
                            }
                            else -> {
                                userPostContainer?.displayedChild = PAGE_ERROR

                                globalErrorPost?.refreshBtn?.setOnClickListener {
                                    userPostContainer?.displayedChild = PAGE_LOADING
                                    landedUserName?.let { it1 -> initUserPost(it1) }
                                }
                            }
                        }
                    }
                }
            }
        })

    private fun addSocialFollowErrorObserver() =
        mPresenter.followErrorMessageLiveData.observe(viewLifecycleOwner, Observer {
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
        mPresenter.unFollowErrorMessageLiveData.observe(viewLifecycleOwner, Observer {
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
        mPresenter.postReminderLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {

                    }
                    is Success -> {
                        if (it?.data?.playToggleChannelReminder?.header?.status == 200) {
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
                userProfileTracker?.clickUnfollow(userId, profileUserId == userId)
                mPresenter.doUnFollow(userIdEnc)
                updateToUnFollowUi()
            } else {
                userProfileTracker?.clickFollow(userId, profileUserId == userId)
                mPresenter.doFollow(userIdEnc)
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
        userId = data.profileHeader.profile.userID

        val textBio = view?.findViewById<TextView>(R.id.text_bio)
        val textSeeMore = view?.findViewById<TextView>(R.id.text_see_more)
        val textUserName = view?.findViewById<TextView>(R.id.text_user_name)
        val textDisplayName = view?.findViewById<TextView>(R.id.text_display_name)
        val textContentCount = view?.findViewById<TextView>(R.id.text_content_count)
        val textFollowerCount = view?.findViewById<TextView>(R.id.text_follower_count)
        val textFollowingCount = view?.findViewById<TextView>(R.id.text_following_count)

        btnAction = view?.findViewById<UnifyButton>(R.id.btn_action_follow)
        appBarLayout = view?.findViewById(R.id.app_bar_layout)

        if (data.profileHeader.profile.username.isNotBlank()) {
            textUserName?.text = "@" + data.profileHeader.profile.username
        }

        textDisplayName?.text = data.profileHeader.profile.name
        textContentCount?.text =
            UserProfileUtils.getFormattedNumber(data.profileHeader.stats.totalPost)
        textFollowerCount?.text =
            UserProfileUtils.getFormattedNumber(data.profileHeader.stats.totalFollower)
        textFollowingCount?.text =
            UserProfileUtils.getFormattedNumber(data.profileHeader.stats.totalFollowing)

        userProfileTracker?.openUserProfile(screenName = screenName, userId, live = data.profileHeader.profile.liveplaychannel.islive)

        setProfileImg(data.profileHeader.profile)

        displayName = data.profileHeader.profile.name
        userName = data.profileHeader.profile.username
        mAdapter.setUserName(data.profileHeader.profile.username)
        totalFollowers = data.profileHeader.stats.totalFollowerFmt
        totalFollowings = data.profileHeader.stats.totalFollowingFmt
        profileImage = data.profileHeader.profile.imageCover
        profileUserId = data.profileHeader.profile.userID
        userWebLink = data.profileHeader.profile.sharelink.weblink
        textBio?.maxLines = MAX_LINE

        textBio?.postDelayed({
            textBio?.text = context?.let {
                HtmlLinkHelper(
                    it,
                    data.profileHeader.profile.biography
                ).spannedString
            }

            if (textBio?.lineCount > SEE_ALL_LINE) {
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

        }, 50)

        if (userSession?.isLoggedIn == false) {
            updateToUnFollowUi()
            btnAction?.setOnClickListener {
                startActivityForResult(
                    RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN
                )
            }
        }

        appBarLayout?.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val offset =
                context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
            if ((abs(verticalOffset) > offset!!)) {
                headerProfile?.title = data.profileHeader.profile.name
                headerProfile?.subtitle = data.profileHeader.profile.username
            } else {
                headerProfile?.title = ""
                headerProfile?.subtitle = ""
            }
        })
    }

    private fun setActionButton(followProfile: UserProfileIsFollow) {

        if (!userSession?.userId.isNullOrBlank() && followProfile.profileHeader.items[0].userID == userSession?.userId) {
            btnAction?.text = getString(com.tokopedia.people.R.string.up_btn_profile)
            btnAction?.buttonVariant = UnifyButton.Variant.GHOST
            btnAction?.buttonType = UnifyButton.Type.ALTERNATE

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

    private fun showLoader() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
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
        bundle.putString(EXTRA_USER_ID, userId)
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
    }

    private fun showUniversalShareBottomSheet() {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@UserProfileFragment)
            userSession?.userId?.ifEmpty { "0" }?.let {
                setUtmCampaignData(
                    "Profile",
                    it,
                    userName,
                    "share"
                )
            }
            setMetaData(
                tnTitle = displayName,
                tnImage = profileImage
            )
            setOgImageUrl(profileImage ?: "")
        }
        universalShareBottomSheet?.show(fragmentManager, this)
    }

    companion object {
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

        const val PAGE_CONTENT = 0
        const val PAGE_ERROR = 2
        const val PAGE_LOADING = 1
        const val PAGE_EMPTY = 3
        const val SEE_ALL_LINE = 3
        const val MAX_LINE = 20

        fun newInstance(extras: Bundle): Fragment {
            val fragment = UserProfileFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        var desc = userName

        if (desc.isBlank()) {
            desc = "Lihat foto & video menarik dari Tokopedia Merchandise, yuk! \uD83D\uDE0D"
        } else {
            desc = "Lihat foto & video menarik dari Tokopedia $displayName ($userName), yuk! 😍"
        }

        val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
            type = LinkerData.USER_PROFILE_SOCIAL
            uri = userWebLink
            id = userId //todo lavekush
            //set and share in the Linker Data
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            ogTitle = displayName
            ogDescription = desc
            if (shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        })
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    context?.let {

                        var shareString = desc + " " + linkerShareData?.shareUri
                        SharingUtil.executeShareIntent(
                            shareModel,
                            linkerShareData,
                            activity,
                            view,
                            shareString
                        )
                        // send gtm trackers if you want to
                        shareModel.channel?.let { it1 ->
                            userProfileTracker?.clickShareChannel(userId, profileUserId == userId, it1)
                        }
                        userProfileTracker?.viewShareChannel(userId, profileUserId == userId)
                        universalShareBottomSheet?.dismiss()
                    }
                }

                override fun onError(linkerError: LinkerError?) {
                    //Most of the error cases are already handled for you. Let me know if you want to add your own error handling.
                }
            })
        )
    }

    override fun onCloseOptionClicked() {
//        TODO gtm tracking
        //This method will be mostly used for GTM Tracking stuff. So add the tracking accordingly
        userSession?.userId?.let { UserProfileTracker().clickCloseShareButton(it, profileUserId == it) }
    }
}

