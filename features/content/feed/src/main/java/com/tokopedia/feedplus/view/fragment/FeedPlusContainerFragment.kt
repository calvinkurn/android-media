package com.tokopedia.feedplus.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.DISCOVERY_BY_ME
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.createpost.common.analyics.FeedTrackerImagePickerInsta
import com.tokopedia.createpost.common.view.customview.PostProgressUpdateView
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.explore.view.fragment.ContentExploreFragment
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.feedplus.view.adapter.FeedPlusTabAdapter
import com.tokopedia.feedplus.view.analytics.FeedToolBarAnalytics
import com.tokopedia.feedplus.view.analytics.entrypoint.FeedEntryPointAnalytic
import com.tokopedia.feedplus.view.customview.FeedMainToolbar
import com.tokopedia.feedplus.view.di.DaggerFeedContainerComponent
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import com.tokopedia.remoteconfig.*
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.seller_migration_common.presentation.util.setupBottomSheetFeedSellerMigration
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_feed_plus_container.*
import kotlinx.android.synthetic.main.partial_feed_error.*
import timber.log.Timber
import javax.inject.Inject

/**
 * @author by milhamj on 25/07/18.
 */

private const val FEED_PAGE = "feed"
private const val BROADCAST_VISIBLITY = "BROADCAST_VISIBILITY"

class FeedPlusContainerFragment : BaseDaggerFragment(), FragmentListener, AllNotificationListener, FeedMainToolbar.OnToolBarClickListener,PostProgressUpdateView.PostUpdateSwipe {

    private var showOldToolbar: Boolean = false
    private var feedToolbar: Toolbar? = null
    private var authorList: List<Author>? = null
    private var postProgressUpdateView: PostProgressUpdateView? = null
    private var mInProgress = false

    companion object {
        const val TOOLBAR_GRADIENT = 1
        const val TOOLBAR_WHITE = 2
        const val PARAM_SHOW_PROGRESS_BAR = "show_posting_progress_bar"
        const val PARAM_IS_EDIT_STATE = "is_edit_state"
        const val PARAM_MEDIA_PREVIEW = "media_preview"
        val MAX_MULTI_SELECT_ALLOWED_VALUE = 5

        val TITLE = "title"
        val SUB_TITLE = "subtitle"
        val TOOLBAR_ICON_RES = "icon_res"
        val TOOLBAR_ICON_URL = "icon_url"
        val MENU_TITLE = "menu_title"
        val MAX_MULTI_SELECT_ALLOWED = "max_multi_select"
        val APPLINK_AFTER_CAMERA_CAPTURE = "link_cam"
        val APPLINK_FOR_GALLERY_PROCEED = "link_gall"
        val APPLINK_FOR_BACK_NAVIGATION = "link_back"
        val URIS = "ip_uris"

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

    val KEY_IS_LIGHT_THEME_STATUS_BAR = "is_light_theme_status_bar"
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
    }

    private var badgeNumberNotification: Int = 0
    private var badgeNumberInbox: Int = 0
    private var badgeNumberCart: Int = 0
    private var toolbarType = TOOLBAR_GRADIENT
    private var startToTransitionOffset = 0
    private var searchBarTransitionRange = 0
    private var isLightThemeStatusBar = false
    private var useNewInbox = false
    private var isSeller = false

    private lateinit var coachMarkItem: CoachMarkItem
    private lateinit var feedBackgroundCrossfader: TransitionDrawable

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainParentStatusBarListener = context as MainParentStatusBarListener
        requestStatusBarDark()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.tabResp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetTab(it.data)
                is Fail -> onErrorGetTab(it.throwable)
            }
        })
        viewModel.whitelistResp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderFab(it.data)
                is Fail -> onErrorGetWhitelist(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_plus_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isSeller = userSession.hasShop() || userSession.isAffiliate
        activity?.let {
            status_bar_bg.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
            status_bar_bg2.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        initNavRevampAbTest()
        initInboxAbTest()
        initToolbar()
        initView()
        requestFeedTab()
        initFab()
    }

    private fun initNavRevampAbTest() {
        showOldToolbar = false
    }

    private fun initInboxAbTest() {
        useNewInbox = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.KEY_AB_INBOX_REVAMP, RollenceKey.VARIANT_OLD_INBOX
        ) == RollenceKey.VARIANT_NEW_INBOX && !showOldToolbar
    }

    private fun getInboxIcon(): Int {
        return if (useNewInbox) {
            IconList.ID_INBOX
        } else {
            IconList.ID_MESSAGE
        }
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
        feed_background_frame.hide()
        feedToolbar = context?.let { NavToolbar(it) }
        (feedToolbar as? NavToolbar)?.let {
            it.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
            it.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)
            it.switchToLightToolbar()
            it.setContentInsetsAbsolute(0,0)
            it.setToolbarPageName(FEED_PAGE)
            viewLifecycleOwner.lifecycle.addObserver(it)
            it.setIcon(getToolbarIcons())
            it.setupSearchbar(hints = listOf(HintData()), searchbarClickCallback = ::onImageSearchClick)
        }
    }

    private fun initOldToolBar() {
        feed_background_frame.show()
        feedToolbar = context?.let { FeedMainToolbar(it) }
        setFeedBackgroundCrossfader()
        feed_appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset + (feedToolbar?.height ?: 0) < 0) {
                showNormalTextWhiteToolbar()
            } else {
                showWhiteTextTransparentToolbar()
            }
        })
        (feedToolbar as? FeedMainToolbar)?.setToolBarClickListener(this)
    }

    private fun getToolbarIcons(): IconBuilder {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(getInboxIcon()) { onInboxButtonClick() }

        if (!useNewInbox) {
            icons.addIcon(IconList.ID_NOTIFICATION) { onNotificationClick() }
        }
        icons.apply {
            addIcon(IconList.ID_CART) {}
        }
        if(!showOldToolbar){
            icons.addIcon(IconList.ID_NAV_GLOBAL) {}
        }
        return icons
    }

    override fun onPause() {
        super.onPause()
        hideAllFab()
    }

    override fun onResume() {
        super.onResume()
        if (!userSession.isLoggedIn || !isSeller)
            fab_feed.visibility = View.GONE
        else
            fab_feed.visibility = View.VISIBLE

        if (activity?.intent?.getBooleanExtra(PARAM_SHOW_PROGRESS_BAR, false) == true) {
            if (!mInProgress) {
                val isEditPost = activity?.intent?.getBooleanExtra(
                    PARAM_IS_EDIT_STATE, false) ?: false
                postProgressUpdateView?.resetProgressBarState(isEditPost ?: false)
                if (!isEditPost) {
                    val mediaPath = activity?.intent?.getStringExtra(
                        PARAM_MEDIA_PREVIEW) ?: ""
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
        super.onDestroy()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerFeedContainerComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .build().inject(this)
    }

    override fun onScrollToTop() {
        try {
            val fragment = pagerAdapter.getRegisteredFragment(view_pager.currentItem)
            if (fragment is FeedPlusFragment) {
                fragment.scrollToTop()
            } else if (fragment is ContentExploreFragment) {
                fragment.scrollToTop()
            }
        } catch (e: IllegalStateException) {
            //no op
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
        (feedToolbar as? FeedMainToolbar)?.run {
            setNotificationNumber(notificationCount)
            setInboxNumber(inboxCount)
        }
        (feedToolbar as? NavToolbar)?.run {
            if (!useNewInbox) {
                setBadgeCounter(IconList.ID_NOTIFICATION, notificationCount)
            }
            setBadgeCounter(getInboxIcon(), inboxCount)
            setBadgeCounter(IconList.ID_CART, cartCount)
        }
        this.badgeNumberNotification = notificationCount
        this.badgeNumberInbox = inboxCount
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser) hideAllFab()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        childFragmentManager.fragments
                .filterIsInstance<FeedPlusFragment>()
                .forEach { it.onParentFragmentHiddenChanged(hidden) }
    }

    @JvmOverloads
    fun goToExplore(shouldResetCategory: Boolean = false) {
        if (canGoToExplore()) {
            view_pager.currentItem = pagerAdapter.contentExploreIndex

            if (shouldResetCategory) {
                pagerAdapter.contentExplore?.onCategoryReset()
            }
        }
    }

    private fun initView() {
        postProgressUpdateView = view?.findViewById(R.id.postUpdateView)
        postProgressUpdateView?.setCreatePostData(CreatePostViewModel())
        postProgressUpdateView?.setPostUpdateListener(this)
        hideAllFab()
        setAdapter()
        setViewPager()
        onNotificationChanged(
            badgeNumberNotification,
            badgeNumberInbox,
            badgeNumberCart
        ) // notify badge after toolbar created
    }

    private fun initFab() {
        fab_feed.type = FloatingButtonUnify.BASIC
        fab_feed.color = FloatingButtonUnify.COLOR_GREEN
        fab_feed.circleMainMenu.setOnClickListener {
            fab_feed.menuOpen = !fab_feed.menuOpen
            if (fab_feed.menuOpen) entryPointAnalytic.clickMainEntryPoint()
        }

        val items = arrayListOf<FloatingButtonItem>()

        if (userSession.hasShop() && userSession.isLoggedIn) {
            items.add(
                FloatingButtonItem(
                    iconDrawable = getIconUnifyDrawable(requireContext(), IconUnify.VIDEO),
                    title = getString(R.string.feed_fab_create_live),
                    listener = {
                        fab_feed.menuOpen = false
                        entryPointAnalytic.clickCreateLiveEntryPoint()

                        RouteManager.route(requireContext(), ApplinkConst.PLAY_BROADCASTER)
                    }
                )
            )
        }

        if (isSeller && userSession.isLoggedIn) {
            items.add(
                    FloatingButtonItem(
                            iconDrawable = getIconUnifyDrawable(requireContext(), IconUnify.IMAGE),
                            title = getString(R.string.feed_fab_create_post),
                            listener = {
                                try {
                                    fab_feed.menuOpen = false
                                    entryPointAnalytic.clickCreatePostEntryPoint()
                                    val shouldShowNewContentCreationFlow = enableContentCreationNewFlow()
                                    if (shouldShowNewContentCreationFlow) {
                                        val authors = viewModel.feedContentForm.authors
                                        val intent = RouteManager.getIntent(context, ApplinkConst.IMAGE_PICKER_V2)
                                        intent.putExtra(APPLINK_AFTER_CAMERA_CAPTURE,
                                                ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
                                        intent.putExtra(MAX_MULTI_SELECT_ALLOWED,
                                                MAX_MULTI_SELECT_ALLOWED_VALUE)
                                        intent.putExtra(TITLE,
                                                getString(com.tokopedia.feedplus.R.string.feed_post_sebagai))
                                        val name: String = MethodChecker.fromHtml(authors.first().name).toString()
                                        intent.putExtra(SUB_TITLE, name)
                                        intent.putExtra(TOOLBAR_ICON_URL,
                                                authors.first().thumbnail
                                        )
                                        intent.putExtra(APPLINK_FOR_GALLERY_PROCEED,
                                                ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
                                        startActivity(intent)
                                        TrackerProvider.attachTracker(FeedTrackerImagePickerInsta(userSession.shopId))
                                    } else {
                                        openBottomSheetToFollowOldFlow()
                                    }
                                } catch (e: Exception) {
                                    Timber.e(e)
                                }
                            }
                    )
            )
        }

        if (items.isNotEmpty()) {
            fab_feed.addItem(items)
            fab_feed.show()
        } else {
            fab_feed.hide()
        }
    }

    private fun enableContentCreationNewFlow(): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        return config.getBoolean(RemoteConfigKey.ENABLE_NEW_CONTENT_CREATION_FLOW, true)
    }


    private fun setViewPager() {
        view_pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                    if (position == 1) {
                        context?.let {
                            val intent = Intent(BROADCAST_VISIBLITY)
                            LocalBroadcastManager.getInstance(it.applicationContext)
                                .sendBroadcast(intent)
                        }
                        postProgressUpdateView?.hide()
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
        viewModel.getContentForm()
    }

    private fun showLoading() {
        feed_loading.visibility = View.VISIBLE
        feed_error.visibility = View.GONE
        tab_layout.visibility = View.INVISIBLE
        view_pager.visibility = View.INVISIBLE
    }

    private fun onErrorGetTab(throwable: Throwable) {
        message_retry.text = ErrorHandler.getErrorMessage(context, throwable)
        button_retry.setOnClickListener { requestFeedTab() }

        feed_loading.visibility = View.GONE
        feed_error.visibility = View.VISIBLE
        tab_layout.visibility = View.INVISIBLE
        view_pager.visibility = View.INVISIBLE
    }

    private fun onSuccessGetTab(data: FeedTabs) {
        //TODO rollback this change , only for testing
//        val feedDatæfeedData.filter { it.type == FeedTabs.TYPE_FEEDS || it.type == FeedTabs.TYPE_EXPLORE || it.type == FeedTabs.TYPE_CUSTOM || it.type == FeedTabs.TYPE_VIDEO }
//
        var feedData = mutableListOf<FeedTabs.FeedData>()
         feedData.addAll(data.feedData.filter { it.type == FeedTabs.TYPE_FEEDS || it.type == FeedTabs.TYPE_EXPLORE || it.type == FeedTabs.TYPE_CUSTOM || it.type == FeedTabs.TYPE_VIDEO })
        feedData.add(FeedTabs.FeedData(title = "Video", key = "video", type = "custom",position = 4,isActive = true))

        pagerAdapter.setItemList(feedData)
        view_pager.currentItem = if (data.meta.selectedIndex < feedData.size) data.meta.selectedIndex else 0
        view_pager.offscreenPageLimit = pagerAdapter.count
        feed_loading.visibility = View.GONE
        feed_error.visibility = View.GONE
        tab_layout.visibility = View.VISIBLE
        view_pager.visibility = View.VISIBLE

        if (hasCategoryIdParam()) {
            goToExplore()
        }
        if (userSession.isLoggedIn) {
            viewModel.getWhitelist(authorList?.isEmpty()?:false)
        }
    }

    private fun renderFab(whitelistDomain: WhitelistDomain) {
        authorList = whitelistDomain.authors
    }

    private fun onErrorGetWhitelist(throwable: Throwable) {
        view?.let {
            Toaster.make(it, ErrorHandler.getErrorMessage(context, throwable), Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                if (userSession.isLoggedIn) {
                    viewModel.getWhitelist(authorList?.isEmpty()?:false)
                }
            })
        }
    }

    private fun setFeedBackgroundCrossfader() {
        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.searchbar_tansition_range)
        startToTransitionOffset = status_bar_bg.layoutParams.height + resources.getDimensionPixelSize(R.dimen.searchbar_start_tansition_offsite)
        activity?.let {
            val feedBackgroundGradient = MethodChecker.getDrawable(it, R.drawable.gradient_feed)
            val feedBackgroundWhite = MethodChecker.getDrawable(it, R.drawable.gradient_feed_white)
            feedBackgroundCrossfader = TransitionDrawable(arrayOf<Drawable>(feedBackgroundGradient, feedBackgroundWhite))
        }
        feed_background_image.setImageDrawable(feedBackgroundCrossfader)
        feedBackgroundCrossfader.startTransition(0)
    }


    private fun showNormalTextWhiteToolbar() {
        if (toolbarType != TOOLBAR_GRADIENT) {
            feedBackgroundCrossfader.reverseTransition(200)
            toolbarType = TOOLBAR_GRADIENT
            status_bar_bg2.visibility = when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> View.VISIBLE
                else -> View.INVISIBLE
            }
            activity?.let {
                tab_layout.setSelectedTabIndicatorColor(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_G400))
                tab_layout.setTabTextColors(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N700_32), MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_G400))
            }
            requestStatusBarDark()
        }
    }

    private fun showWhiteTextTransparentToolbar() {
        if (toolbarType != TOOLBAR_WHITE) {
            feedBackgroundCrossfader.reverseTransition(200)
            toolbarType = TOOLBAR_WHITE
            status_bar_bg2.visibility = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> View.INVISIBLE
                else -> View.GONE
            }
            activity?.let {
                tab_layout.setSelectedTabIndicatorColor(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                tab_layout.setTabTextColors(MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N0), MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            }
            requestStatusBarLight()
        }
    }

    private fun setAdapter() {
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun hasCategoryIdParam(): Boolean {
        return !arguments?.getString(ContentExploreFragment.PARAM_CATEGORY_ID).isNullOrBlank()
    }

    private fun canGoToExplore(): Boolean {
        return pagerAdapter.isContextExploreExist
    }

    private fun goToCreateAffiliate() {
        if (context != null) {
            if (affiliatePreference.isFirstTimeEducation(userSession.userId)) {

                val intent = RouteManager.getIntent(
                        context,
                        ApplinkConst.DISCOVERY_PAGE.replace("{page_id}", DISCOVERY_BY_ME)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                affiliatePreference.setFirstTimeEducation(userSession.userId)

            } else {
                RouteManager.route(context, ApplinkConst.AFFILIATE_CREATE_POST, "-1", "-1")
            }
        }
    }

    fun hideAllFab() {
        if (activity == null) {
            return
        }

        fab_feed.menuOpen = false
    }

    private fun onGoToLink(link: String) {
        if (!TextUtils.isEmpty(link)) {
            RouteManager.route(activity, link)
        }
    }

    private fun onGoToLogin() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            it.startActivityForResult(intent, FeedPlusFragment.REQUEST_LOGIN)
        }
    }

    fun showCreatePostOnBoarding() {
        val fabCircle = fab_feed.circleMainMenu
        fabCircle.addOneTimeGlobalLayoutListener {
            val location = IntArray(2)
            fabCircle.getLocationOnScreen(location)

            val x1 = location[0]
            val y1 = location[1]
            val x2 = x1 + fabCircle.width
            val y2 = y1 + fabCircle.height

            coachMarkItem = CoachMarkItem(
                fabCircle,
                getString(R.string.feed_onboarding_create_post_title),
                getString(R.string.feed_onboarding_create_post_detail)
            ).withCustomTarget(intArrayOf(x1, y1, x2, y2))

            showFabCoachMark()
        }
    }

    private fun showFabCoachMark() {
        if (::coachMarkItem.isInitialized
            && !affiliatePreference.isCreatePostEntryOnBoardingShown(userSession.userId)
            && !fab_feed.circleMainMenu.isOrWillBeHidden) {
            coachMark.show(activity = activity, tag = null, tutorList = arrayListOf(coachMarkItem))
            affiliatePreference.setCreatePostEntryOnBoardingShown(userSession.userId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LIGHT_THEME_STATUS_BAR, isLightThemeStatusBar)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        isLightThemeStatusBar = savedInstanceState?.getBoolean(KEY_IS_LIGHT_THEME_STATUS_BAR)
                ?: false
    }

    private fun onImageSearchClick(hint: String) {
        RouteManager.route(context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
        onImageSearchClick()
    }

    override fun onImageSearchClick() {
        toolBarAnalytics.eventClickSearch()
    }

    override fun onInboxButtonClick() {
        toolBarAnalytics.eventClickInbox()
    }

    override fun onNotificationClick() {
        toolBarAnalytics.eventClickNotification()
    }

    override fun swipeOnPostUpdate() {
        Toaster.build(requireView(),
            getString(com.tokopedia.feedplus.R.string.feed_post_successful_toaster),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_NORMAL).show()
        mInProgress = false
        postProgressUpdateView?.unregisterBroadcastReceiver()
        postProgressUpdateView?.unregisterBroadcastReceiverProgress()
        activity?.intent?.putExtra("show_posting_progress_bar", false)
        try {
            val fragment = pagerAdapter.getRegisteredFragment(view_pager.currentItem)
            if (fragment is FeedPlusFragment) {
                fragment.onRefreshForNewPostUpdated()
            }
        } catch (e: IllegalStateException) {
            //no op
        }
        updateVisibility(false)
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

    private fun openBottomSheetToFollowOldFlow() {
        when {
            isSellerMigrationEnabled(context) -> {
                val shopAppLink = UriUtil.buildUri(ApplinkConst.SHOP, userSession.shopId)
                val createPostAppLink = ApplinkConst.CONTENT_CREATE_POST
                val intent = SellerMigrationActivity.createIntent(
                        context = requireContext(),
                        featureName = SellerMigrationFeatureName.FEATURE_POST_FEED,
                        screenName = FeedPlusContainerFragment::class.simpleName.orEmpty(),
                        appLinks = arrayListOf(ApplinkConstInternalSellerapp.SELLER_HOME, shopAppLink, createPostAppLink))
                setupBottomSheetFeedSellerMigration(::goToCreateAffiliate, intent)

            }
        }
    }


}
