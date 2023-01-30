package com.tokopedia.feedplus.view.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.explore.view.fragment.ContentExploreFragment
import com.tokopedia.feedcomponent.view.base.FeedPlusContainerListener
import com.tokopedia.feedcomponent.view.base.FeedPlusTabParentFragment
import com.tokopedia.feedcomponent.view.custom.FeedFloatingButton
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.feedplus.view.adapter.FeedPlusTabAdapter
import com.tokopedia.feedplus.view.analytics.FeedToolBarAnalytics
import com.tokopedia.feedplus.view.analytics.entrypoint.FeedEntryPointAnalytic
import com.tokopedia.feedplus.view.analytics.shorts.PlayShortsInFeedAnalytic
import com.tokopedia.feedplus.view.coachmark.FeedOnboardingCoachmark
import com.tokopedia.feedplus.view.customview.PostProgressUpdateView
import com.tokopedia.feedplus.view.di.FeedInjector
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.analytic.PlayShortsUploadAnalytic
import com.tokopedia.play_common.shortsuploader.const.PlayShortsUploadConst
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.view.VideoTabFragment
import kotlinx.android.synthetic.main.fragment_feed_plus_container.*
import kotlinx.android.synthetic.main.partial_feed_error.*
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.feedcomponent.R as feedComponentR

/**
 * @author by milhamj on 25/07/18.
 */
@Keep
class FeedPlusContainerFragment : BaseDaggerFragment(), FragmentListener, AllNotificationListener,
    PostProgressUpdateView.PostUpdateSwipe, FeedPlusContainerListener,FeedOnboardingCoachmark.Listener {

    private var showOldToolbar: Boolean = false
    private var shouldHitFeedTracker: Boolean = false
    private var isTrackerOnBroadcastRecieveAlreadyHit: Boolean = false
    private var isFeedSelectedFromBottomNavigation: Boolean = false
    private var feedToolbar: Toolbar? = null
    private val authorList: MutableList<GetCheckWhitelistResponse.Author> = mutableListOf()
    private lateinit var newFeedReceiver: BroadcastReceiver
    private var postProgressUpdateView: PostProgressUpdateView? = null
    private var viewPager: ViewPager? = null
    private var tabLayout: TabsUnify? = null
    private var mInProgress = false
    private var coachMarkOverlay: View? = null

    companion object {
        const val PARAM_SHOW_PROGRESS_BAR = "show_posting_progress_bar"
        const val PARAM_IS_EDIT_STATE = "is_edit_state"
        const val PARAM_MEDIA_PREVIEW = "media_preview"
        const val FEED_FRAGMENT_INDEX = 0

        const val ARGS_FEED_TAB_POSITION = "FEED_TAB_POSITION"
        const val ARGS_FEED_VIDEO_TAB_SELECT_CHIP = "tab"

        private const val BROADCAST_FEED = "BROADCAST_FEED"
        const val FEED_IS_VISIBLE = "FEED_IS_VISIBLE"

        const val PARAM_FEED_TAB_POSITION = "FEED_TAB_POSITION"
        const val UPDATE_TAB_POSITION = "1"
        const val EXPLORE_TAB_POSITION = "2"
        const val VIDEO_TAB_POSITION = "3"

        private const val FEED_PAGE = "feed"
        private const val BROADCAST_VISIBLITY = "BROADCAST_VISIBILITY"

        @JvmStatic
        fun newInstance(bundle: Bundle?) = FeedPlusContainerFragment().apply { arguments = bundle }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var userSession: UserSessionInterface

    @Inject
    internal lateinit var affiliatePreference: AffiliatePreference

    @Inject
    lateinit var toolBarAnalytics: FeedToolBarAnalytics

    @Inject
    lateinit var entryPointAnalytic: FeedEntryPointAnalytic

    @Inject
    lateinit var playShortsUploader: PlayShortsUploader

    @Inject
    lateinit var playShortsInFeedAnalytic: PlayShortsInFeedAnalytic

    @Inject
    lateinit var playShortsUploadAnalytic: PlayShortsUploadAnalytic

    @JvmField @Inject
    var coachMarkManager: ContentCoachMarkManager? = null

    @Inject
    lateinit var onboardingCoachmark: FeedOnboardingCoachmark


    /** View */
    private lateinit var fabFeed: FloatingButtonUnify
    private lateinit var feedFloatingButton: FeedFloatingButton
    private var ivFeedUser: ImageUnify? = null

    private val keyIsLightThemeStatusBar = "is_light_theme_status_bar"
    private var mainParentStatusBarListener: MainParentStatusBarListener? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FeedPlusContainerViewModel::class.java]
    }

    private val pagerAdapter: FeedPlusTabAdapter by lazy {
        FeedPlusTabAdapter(childFragmentManager, emptyList(), arguments)
    }

    private val coachMark: CoachMark by lazy {
        CoachMarkBuilder()
            .allowPreviousButton(false)
            .build()
            .also {
                it.overlayOnClickListener = {
                    coachMark.close()
                    feedFloatingButton.expand()
                }
                it.onFinishListener = {
                    feedFloatingButton.expand()
                }
            }
    }

    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0
    private var badgeNumberCart: Int = 0
    private var isLightThemeStatusBar = false
    private var isSeller = false

    private lateinit var coachMarkItem: CoachMarkItem

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainParentStatusBarListener = context as MainParentStatusBarListener
        requestStatusBarDark()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.tabResp.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onSuccessGetTab(it.data)
                    is Fail -> onErrorGetTab(it.throwable)
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_plus_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isSeller = userSession.hasShop() || userSession.isAffiliate
        activity?.let {
            status_bar_bg.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
            status_bar_bg2.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        setupView(view)
        initNavRevampAbTest()
        initToolbar()
        initView()
        requestFeedTab()
        initFab()
        setupObserver()
    }

    private fun addDataToArgument() {
        activity?.intent?.let {
            val args = Bundle()
            args.putString(
                ARGS_FEED_TAB_POSITION,
                it.data?.getQueryParameter(ARGS_FEED_TAB_POSITION)
            )
            args.putString(
                ARGS_FEED_VIDEO_TAB_SELECT_CHIP,
                it.data?.getQueryParameter(ARGS_FEED_VIDEO_TAB_SELECT_CHIP)
            )
            arguments = args
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when (childFragment) {
            is FeedPlusTabParentFragment -> childFragment.setContainerListener(this)
        }
    }

    private fun setupView(view: View) {
        fabFeed = view.findViewById(R.id.fab_feed)
        feedFloatingButton = view.findViewById(R.id.feed_floating_button)
        ivFeedUser = view.findViewById(R.id.iv_feed_user)
    }

    private fun initNavRevampAbTest() {
        showOldToolbar = false
    }

    private fun getInboxIcon(): Int {
        return IconList.ID_MESSAGE
    }

    private fun initToolbar() {
        status_bar_bg.visibility = when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> View.VISIBLE
            else -> View.INVISIBLE
        }
        status_bar_bg2.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> View.INVISIBLE
            else -> View.INVISIBLE
        }
        toolbarParent.removeAllViews()
        initNewToolBar()
        toolbarParent.addView(feedToolbar)
    }

    private fun initNewToolBar() {
        feedToolbar = context?.let { NavToolbar(it) }
        (feedToolbar as? NavToolbar)?.let {
            it.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
            it.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)
            it.switchToLightToolbar()
            it.setContentInsetsAbsolute(0, 0)
            it.setToolbarPageName(FEED_PAGE)
            viewLifecycleOwner.lifecycle.addObserver(it)
            it.setIcon(getToolbarIcons())
            it.setupSearchbar(hints = listOf(HintData()), searchbarClickCallback = ::onImageSearchClick)
        }
    }

    private fun getToolbarIcons(): IconBuilder {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
            .addIcon(getInboxIcon()) { onInboxButtonClick() }

        icons.addIcon(IconList.ID_NOTIFICATION) { onNotificationClick() }
        icons.apply {
            addIcon(IconList.ID_CART) {}
        }
        if (!showOldToolbar) {
            icons.addIcon(IconList.ID_NAV_GLOBAL) {}
        }
        return icons
    }

    override fun onPause() {
        super.onPause()
        hideAllFab()
        shouldHitFeedTracker = true
        unRegisterNewFeedReceiver()
        feedFloatingButton.stopTimer()
    }

    override fun onResume() {
        super.onResume()
        addDataToArgument()
        registerNewFeedReceiver()
        if (hasFeedTabParam()) {
            openTabAsPerParamValue()
        }
        feedFloatingButton.checkFabMenuStatusWithTimer {
            fabFeed.menuOpen
        }

        if (shouldHitFeedTracker && isFeedSelectedFromBottomNavigation) {
            toolBarAnalytics.createAnalyticsForOpenScreen(
                viewPager?.currentItem ?: 0,
                userSession.isLoggedIn.toString(),
                userSession.userId
            )
            toolBarAnalytics.userVisitsFeed(userSession.isLoggedIn.toString(), userSession.userId)
        }

        if (activity?.intent?.getBooleanExtra(PARAM_SHOW_PROGRESS_BAR, false) == true) {
            if (!mInProgress) {
                val isEditPost = activity?.intent?.getBooleanExtra(
                    PARAM_IS_EDIT_STATE,
                    false
                ) ?: false
                postProgressUpdateView?.resetProgressBarState(isEditPost)
                if (!isEditPost) {
                    val mediaPath = activity?.intent?.getStringExtra(
                        PARAM_MEDIA_PREVIEW
                    ) ?: ""
                    postProgressUpdateView?.setFirstIcon(mediaPath)
                }
                updateVisibility(true)
                mInProgress = true
                postProgressUpdateView?.registerBroadcastReceiver()
                postProgressUpdateView?.registerBroadcastReceiverProgress()
            }
        } else {
            updateVisibility(false)
        }
    }

    override fun onDestroy() {
        viewModel.tabResp.removeObservers(this)
        viewModel.whitelistResp.removeObservers(this)
        viewModel.flush()
        postProgressUpdateView?.unregisterBroadcastReceiver()
        postProgressUpdateView?.unregisterBroadcastReceiverProgress()
        coachMarkManager?.dismissAllCoachMark()
        coachMarkManager = null
        onboardingCoachmark.dismissCoachmark()
        super.onDestroy()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        FeedInjector.get(requireContext())
            .inject(this)
    }

    override fun onScrollToTop() {
        try {
            val fragment = pagerAdapter.getRegisteredFragment(viewPager?.currentItem ?: 0)
            if (fragment is FeedPlusFragment) {
                fragment.scrollToTop()
            } else if (fragment is ContentExploreFragment) {
                fragment.scrollToTop()
            }
        } catch (e: IllegalStateException) {
            // no op
        }
    }

    private fun requestStatusBarDark() {
        isLightThemeStatusBar = false
        mainParentStatusBarListener?.requestStatusBarDark()
    }

    private fun requestStatusBarLight() {
        isLightThemeStatusBar = true
        mainParentStatusBarListener?.requestStatusBarLight()
    }

    override fun isLightThemeStatusBar(): Boolean {
        return isLightThemeStatusBar
    }

    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int, cartCount: Int) {
        (feedToolbar as? NavToolbar)?.run {
            setBadgeCounter(IconList.ID_NOTIFICATION, notificationCount)
            setBadgeCounter(getInboxIcon(), inboxCount)
            setBadgeCounter(IconList.ID_CART, cartCount)
        }
        this.badgeNumberNotification = notificationCount
        this.badgeNumberInbox = inboxCount
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser) {
            hideAllFab()
            coachMarkManager?.dismissAllCoachMark()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        childFragmentManager.fragments
            .filterIsInstance<FeedPlusFragment>()
            .forEach { it.onParentFragmentHiddenChanged(hidden) }
    }

    private fun goToExplore(shouldResetCategory: Boolean = false) {
        if (canGoToExplore()) {
            viewPager?.currentItem = pagerAdapter.getContentExploreIndex()

            if (shouldResetCategory) {
                pagerAdapter.getContentExplore()?.onCategoryReset()
            }
        }
    }
    private fun goToVideo() {
        if (canGoToVideo()) {
            view_pager.currentItem = pagerAdapter.getVideoTabIndex()
        }
    }

    private fun initView() {
        postProgressUpdateView = view?.findViewById(R.id.postUpdateView)
        viewPager = view?.findViewById(R.id.view_pager)
        tabLayout = view?.findViewById(R.id.tab_layout)
        coachMarkOverlay = view?.findViewById(R.id.transparent_overlay_coachmark)
        postProgressUpdateView?.setCreatePostData(CreatePostViewModel())
        postProgressUpdateView?.setPostUpdateListener(this)
        hideAllFab()
        setAdapter()
        setViewPager()
        // initial value when feed opened for very first time
        isFeedSelectedFromBottomNavigation = true

        newFeedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent != null && intent.action != null) {
                    if (intent.action == BROADCAST_FEED) {
                        if (intent.hasExtra(FEED_IS_VISIBLE)) {
                            isFeedSelectedFromBottomNavigation = intent.extras?.getBoolean(
                                FEED_IS_VISIBLE
                            ) ?: isFeedSelectedFromBottomNavigation
                        }
                        if (!isTrackerOnBroadcastRecieveAlreadyHit && isFeedSelectedFromBottomNavigation) {
                            isTrackerOnBroadcastRecieveAlreadyHit = true
                            toolBarAnalytics.createAnalyticsForOpenScreen(
                                viewPager?.currentItem ?: 0,
                                userSession.isLoggedIn.toString(),
                                userSession.userId
                            )
                        }
                    } else if (intent.action == BROADCAST_VISIBLITY) {
                        // When some other tab in selected instead of feed from bottom navigation
                        isFeedSelectedFromBottomNavigation = false
                        isTrackerOnBroadcastRecieveAlreadyHit = false
                    }
                }
            }
        }
        registerNewFeedReceiver()
        onNotificationChanged(
            badgeNumberNotification,
            badgeNumberInbox,
            badgeNumberCart
        ) // notify badge after toolbar created
    }
    private fun registerNewFeedReceiver() {
        if (activity != null && requireActivity().applicationContext != null) {
            val intentFilter = IntentFilter()

            intentFilter.addAction(BROADCAST_FEED)
            intentFilter.addAction(BROADCAST_VISIBLITY)

            LocalBroadcastManager
                .getInstance(requireActivity().applicationContext)
                .registerReceiver(newFeedReceiver, intentFilter)
        }
    }

    private fun unRegisterNewFeedReceiver() {
        if (activity != null && requireActivity().applicationContext != null) {
            LocalBroadcastManager
                .getInstance(requireActivity().applicationContext)
                .unregisterReceiver(newFeedReceiver)
        }
    }

    private fun setupObserver() {
        viewModel.whitelistResp.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> handleWhitelistData(it.data)
                    is Fail -> onErrorGetWhitelist(it.throwable)
                }
            }
        )

        playShortsUploader.observe(viewLifecycleOwner) { progress, uploadData ->
            when(progress) {
                PlayShortsUploadConst.PROGRESS_COMPLETED -> {
                    postProgressUpdateView?.hide()
                    Toaster.build(
                        view = requireView(),
                        text = getString(R.string.feed_upload_shorts_success),
                        duration = Toaster.LENGTH_LONG,
                        type = Toaster.TYPE_NORMAL,
                        actionText = getString(R.string.feed_upload_shorts_see_video),
                        clickListener = View.OnClickListener {
                            playShortsUploadAnalytic.clickRedirectToChannelRoom(
                                uploadData.authorId,
                                uploadData.authorType,
                                uploadData.shortsId
                            )
                            RouteManager.route(requireContext(), ApplinkConst.PLAY_DETAIL, uploadData.shortsId)
                        }
                    ).show()
                }
                PlayShortsUploadConst.PROGRESS_FAILED -> {
                    postProgressUpdateView?.show()
                    postProgressUpdateView?.handleShortsUploadFailed(
                        uploadData,
                        playShortsUploader,
                        playShortsInFeedAnalytic
                    )
                }
                else -> {
                    postProgressUpdateView?.show()
                    postProgressUpdateView?.setIcon(uploadData.coverUri.ifEmpty { uploadData.mediaUri })
                    postProgressUpdateView?.setProgress(progress)
                }
            }
        }
    }

    private fun initFab() {
        fabFeed.type = FloatingButtonUnify.BASIC
        fabFeed.color = FloatingButtonUnify.COLOR_GREEN
        fabFeed.circleMainMenu.visibility = View.INVISIBLE

        feedFloatingButton.setOnClickListener {
            coachMarkManager?.hasBeenShown(feedFloatingButton)
            fabFeed.menuOpen = !fabFeed.menuOpen
            if (fabFeed.menuOpen) {
                entryPointAnalytic.clickMainEntryPoint()

                if(viewModel.isShowShortsButton)
                    playShortsInFeedAnalytic.viewShortsEntryPoint()
            }
        }
    }

    override fun expandFab() {
        if (!fabFeed.menuOpen && !coachMark.isVisible) {
            feedFloatingButton.expand()
        }
    }

    override fun shrinkFab() {
        feedFloatingButton.shrink()
    }

    override fun onChildRefresh() {
        viewModel.getWhitelist()
    }

    override fun onStop() {
        super.onStop()
        activity?.run {
            unRegisterNewFeedReceiver()
        }
    }

    private fun setViewPager() {
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                toolBarAnalytics.clickOnVideoTabOnFeedPage(position, userSession.userId)
                toolBarAnalytics.createAnalyticsForOpenScreen(position, userSession.isLoggedIn.toString(), userSession.userId)

                updateFeedUpdateVisibility(position)

                if (position == 1 || position == 2) {
                    postProgressUpdateView?.hide()
                    if (position == 2) {
                        videoTabAutoPlayJumboWidget()
                    }
                } else if (position == 0 && mInProgress) {
                    postProgressUpdateView?.show()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun requestFeedTab() {
        showLoading()
        viewModel.getDynamicTabs()
    }

    private fun showLoading() {
        feed_loading.visibility = View.VISIBLE
        feed_error.visibility = View.GONE
        tab_layout.visibility = View.INVISIBLE
        viewPager?.visibility = View.INVISIBLE
    }

    private fun onErrorGetTab(throwable: Throwable) {
        message_retry.text = ErrorHandler.getErrorMessage(context, throwable)
        button_retry.setOnClickListener { requestFeedTab() }

        feed_loading.visibility = View.GONE
        feed_error.visibility = View.VISIBLE
        tab_layout.visibility = View.INVISIBLE
        viewPager?.visibility = View.INVISIBLE
    }

    private fun onSuccessGetTab(data: FeedTabs) {
        val feedData = data.feedData.filter { it.type == FeedTabs.TYPE_FEEDS || it.type == FeedTabs.TYPE_EXPLORE || it.type == FeedTabs.TYPE_CUSTOM || it.type == FeedTabs.TYPE_VIDEO }
        tab_layout?.getUnifyTabLayout()?.removeAllTabs()
        feedData.forEach {
            tab_layout?.addNewTab(it.title)
        }

        pagerAdapter.setItemList(feedData)
        viewPager?.currentItem = if (data.meta.selectedIndex < feedData.size) data.meta.selectedIndex else 0
        viewPager?.offscreenPageLimit = pagerAdapter.count
        feed_loading.visibility = View.GONE
        feed_error.visibility = View.GONE
        tab_layout.visibility = View.VISIBLE
        viewPager?.visibility = View.VISIBLE

        if (hasCategoryIdParam()) {
            goToExplore()
        }
        if (hasFeedTabParam()) {
            openTabAsPerParamValue()
        }

        viewModel.getWhitelist()
        if(!userSession.isLoggedIn)
            showOnboardingStepsCoachmark(
                shouldShowUserProfileCoachmark = false,
                shouldShowShortVideoCoachmark = false
            )
    }
    private fun openTabAsPerParamValue() {
        when (arguments?.getString(PARAM_FEED_TAB_POSITION) ?: UPDATE_TAB_POSITION) {
            EXPLORE_TAB_POSITION -> goToExplore()
            VIDEO_TAB_POSITION -> goToVideo()
        }
    }

    private fun handleWhitelistData(whitelistDomain: WhitelistDomain) {
        authorList.clear()
        authorList.addAll(whitelistDomain.authors)

        renderCompleteFab()
        renderUserProfileEntryPoint(whitelistDomain.userAccount)
    }

    private fun renderCompleteFab() {
        hideAllFab()

        try {
            val items = arrayListOf<FloatingButtonItem>()

            if (viewModel.isShowLiveButton) items.add(createLiveFab())
            if (viewModel.isShowPostButton) items.add(createPostFab())
            if (viewModel.isShowShortsButton) items.add(createShortsFab())

            if (items.isNotEmpty() && userSession.isLoggedIn) {
                fabFeed.addItem(items)
                feedFloatingButton.show()

            } else {
                feedFloatingButton.hide()
            }
        } catch (e: Exception) { }
    }
    private fun canGoToVideo(): Boolean {
        return pagerAdapter.isVideoTabExist()
    }

    private fun createLiveFab(): FloatingButtonItem {
        return FloatingButtonItem(
            iconDrawable = getIconUnifyDrawable(requireContext(), IconUnify.VIDEO),
            title = getString(R.string.feed_fab_create_live),
            listener = {
                fabFeed.menuOpen = false
                entryPointAnalytic.clickCreateLiveEntryPoint()

                RouteManager.route(requireContext(), ApplinkConst.PLAY_BROADCASTER)
            }
        )
    }

    private fun createPostFab(): FloatingButtonItem {
        return FloatingButtonItem(
            iconDrawable = getIconUnifyDrawable(requireContext(), IconUnify.IMAGE),
            title = getString(R.string.feed_fab_create_post),
            listener = {
                fabFeed.menuOpen = false
                entryPointAnalytic.clickCreatePostEntryPoint()

                val intent = RouteManager.getIntent(context, ApplinkConst.IMAGE_PICKER_V2)
                intent.putExtra(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
                intent.putExtra(BundleData.MAX_MULTI_SELECT_ALLOWED, BundleData.VALUE_MAX_MULTI_SELECT_ALLOWED)
                intent.putExtra(BundleData.TITLE, getString(feedComponentR.string.feed_post_sebagai))
                intent.putExtra(BundleData.APPLINK_FOR_GALLERY_PROCEED, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
                startActivity(intent)
                TrackerProvider.attachTracker(FeedTrackerImagePickerInsta(userSession.shopId))
            }
        )
    }

    private fun createShortsFab(): FloatingButtonItem {
        return FloatingButtonItem(
            iconDrawable = getIconUnifyDrawable(requireContext(), IconUnify.SHORT_VIDEO),
            title = getString(R.string.feed_fab_create_shorts_video),
            listener = {
                fabFeed.menuOpen = false
                playShortsInFeedAnalytic.clickCreateShortsEntryPoint()

                RouteManager.route(requireContext(), ApplinkConst.PLAY_SHORTS)
            }
        )
    }

    private fun renderUserProfileEntryPoint(userAccount: GetCheckWhitelistResponse.Author?) {
        ivFeedUser ?: onCoachmarkFinish()
        ivFeedUser?.let { ivFeedUser ->
            if (userAccount == null) {
                ivFeedUser.setOnClickListener(null)
                ivFeedUser.hide()
                showOnboardingStepsCoachmark(
                    shouldShowShortVideoCoachmark = userSession.isLoggedIn && feedFloatingButton.isVisible && viewModel.isShowShortsButton,
                    shouldShowUserProfileCoachmark = false
                )
                return
            }

            ivFeedUser.onUrlLoaded = { isSuccess ->
                if (!isSuccess) {
                    ivFeedUser.post {
                        ivFeedUser.setImageDrawable(MethodChecker.getDrawable(requireContext(), R.drawable.ic_user_profile_default))
                    }
                }
            }
            ivFeedUser.setImageUrl(userAccount.thumbnail)
            ivFeedUser.setOnClickListener {
                toolBarAnalytics.clickUserProfileIcon(userSession.userId)
                dismissUserProfileCoachMark()

                RouteManager.route(requireContext(), ApplinkConst.PROFILE, userAccount.id)
            }
            ivFeedUser.show()

            showOnboardingStepsCoachmark(
                shouldShowShortVideoCoachmark = userSession.isLoggedIn && feedFloatingButton.isVisible && viewModel.isShowShortsButton,
                shouldShowUserProfileCoachmark = true
            )

        }
    }

    private fun dismissUserProfileCoachMark() {
        affiliatePreference.setUserProfileEntryPointCoachMarkShown(userSession.userId)
        ivFeedUser?.let { coachMarkManager?.dismissCoachMark(it) }
    }

    private fun onErrorGetWhitelist(throwable: Throwable) {
        view?.let {
            Toaster.build(
                view = it,
                text = ErrorHandler.getErrorMessage(context, throwable),
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR,
                actionText = getString(com.tokopedia.abstraction.R.string.title_try_again),
                clickListener = View.OnClickListener {
                    viewModel.getWhitelist()
                }
            ).show()
        }

        renderCompleteFab()
    }

    private fun setAdapter() {
        viewPager?.adapter = pagerAdapter
        viewPager?.let { tab_layout.setupWithViewPager(it) }
    }

    private fun hasCategoryIdParam(): Boolean {
        return !arguments?.getString(ContentExploreFragment.PARAM_CATEGORY_ID).isNullOrBlank()
    }

    private fun hasFeedTabParam(): Boolean {
        return !arguments?.getString(PARAM_FEED_TAB_POSITION).isNullOrBlank()
    }

    private fun canGoToExplore(): Boolean {
        return pagerAdapter.isContextExploreExist()
    }

    fun hideAllFab() {
        if (activity == null) {
            return
        }

        fabFeed.menuOpen = false
    }

    private fun showCreatePostOnBoarding() {
        feedFloatingButton.addOneTimeGlobalLayoutListener {
            val location = IntArray(2)
            feedFloatingButton.getLocationOnScreen(location)

            val x1 = location[0]
            val y1 = location[1]
            val x2 = x1 + feedFloatingButton.width
            val y2 = y1 + feedFloatingButton.height

            coachMarkItem = CoachMarkItem(
                feedFloatingButton,
                activity?.getString(R.string.feed_onboarding_create_post_title),
                activity?.getString(R.string.feed_onboarding_create_post_detail) ?: ""
            ).withCustomTarget(intArrayOf(x1, y1, x2, y2))

        }
    }

    private fun showFabCoachMark() {
        if (::coachMarkItem.isInitialized &&
            !affiliatePreference.isCreatePostEntryOnBoardingShown(userSession.userId) &&
            feedFloatingButton.visibility == View.VISIBLE
        ) {
            coachMark.show(activity = activity, tag = null, tutorList = arrayListOf(coachMarkItem))
            affiliatePreference.setCreatePostEntryOnBoardingShown(userSession.userId)
        }
    }

    private fun showOnboardingStepsCoachmark(shouldShowShortVideoCoachmark: Boolean, shouldShowUserProfileCoachmark: Boolean){
        val tab = tabLayout?.tabLayout?.getTabAt(2)
        val anchorMap = mapOf<String, View>(
            Pair(FeedOnboardingCoachmark.USER_PROFILE_COACH_MARK_ANCHOR, ivFeedUser!!),
            Pair(FeedOnboardingCoachmark.VIDEO_TAB_COACH_MARK_ANCHOR, tab?.view!!),
            Pair(FeedOnboardingCoachmark.SHORT_VIDEO_COACH_MARK_ANCHOR, feedFloatingButton),
        )
        onboardingCoachmark.showFeedOnboardingCoachmark(
            anchorMap,
            this,
            shouldShowShortVideoCoachmark,
            shouldShowUserProfileCoachmark
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(keyIsLightThemeStatusBar, isLightThemeStatusBar)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        isLightThemeStatusBar = savedInstanceState?.getBoolean(keyIsLightThemeStatusBar)
            ?: false
    }

    private fun onImageSearchClick(hint: String) {
        RouteManager.route(context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
        onImageSearchClick()
    }

    private fun onImageSearchClick() {
        toolBarAnalytics.eventClickSearch()
    }

    private fun onInboxButtonClick() {
        toolBarAnalytics.eventClickInbox()
    }

    private fun onNotificationClick() {
        toolBarAnalytics.eventClickNotification()
    }

    override fun swipeOnPostUpdate() {
        Toaster.build(
            requireView(),
            getString(R.string.feed_post_successful_toaster),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_NORMAL
        ).show()
        mInProgress = false
        postProgressUpdateView?.unregisterBroadcastReceiver()
        postProgressUpdateView?.unregisterBroadcastReceiverProgress()
        activity?.intent?.putExtra("show_posting_progress_bar", false)
        try {
            val fragment = pagerAdapter.getRegisteredFragment(viewPager?.currentItem ?: 0)
            if (fragment is FeedPlusFragment) {
                fragment.onRefreshForNewPostUpdated()
            }
        } catch (e: IllegalStateException) {
            // no op
        }
        updateVisibility(false)
    }
    fun videoTabAutoPlayJumboWidget() {
        try {
            val fragment = pagerAdapter.getRegisteredFragment(viewPager?.currentItem ?: 0)
            if (fragment is VideoTabFragment) {
                fragment.autoplayJumboWidget()
            }
        } catch (e: IllegalStateException) {
            Timber.e(e)
        }
    }
    fun updateFeedUpdateVisibility(position: Int) {
        try {
            val feedFragment = pagerAdapter.getRegisteredFragment(FEED_FRAGMENT_INDEX)
            (feedFragment as FeedPlusFragment).updateFeedVisibilityVariable(position == FEED_FRAGMENT_INDEX)
        } catch (e: IllegalStateException) {
            Timber.e(e)
        }
    }

    override fun onRetryCLicked() {
        toolBarAnalytics.eventClickRetryToPostOnProgressBar(userSession.shopId)
    }

    override fun updateVisibility(flag: Boolean) {
        if (flag) {
            postProgressUpdateView?.show()
        } else {
            postProgressUpdateView?.hide()
        }
    }

    override fun onCoachmarkFinish() {
        coachMarkOverlay?.isClickable = false
        coachMarkOverlay?.gone()
    }
}
