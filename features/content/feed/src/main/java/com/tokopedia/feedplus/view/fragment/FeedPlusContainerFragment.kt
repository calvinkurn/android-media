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
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.tokopedia.explore.view.fragment.ContentExploreFragment
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain
import com.tokopedia.feedplus.view.adapter.FeedPlusTabAdapter
import com.tokopedia.feedplus.view.analytics.FeedToolBarAnalytics
import com.tokopedia.feedplus.view.customview.FeedMainToolbar
import com.tokopedia.feedplus.view.di.DaggerFeedContainerComponent
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.seller_migration_common.presentation.util.setupBottomSheetFeedSellerMigration
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_feed_plus_container.*
import kotlinx.android.synthetic.main.partial_feed_error.*
import javax.inject.Inject

/**
 * @author by milhamj on 25/07/18.
 */

private const val EXP_NAME = AbTestPlatform.NAVIGATION_EXP_TOP_NAV
private const val VARIANT_OLD = AbTestPlatform.NAVIGATION_VARIANT_OLD
private const val VARIANT_REVAMP = AbTestPlatform.NAVIGATION_VARIANT_REVAMP
private const val FEED_PAGE = "feed"

class FeedPlusContainerFragment : BaseDaggerFragment(), FragmentListener, AllNotificationListener, FeedMainToolbar.OnToolBarClickListener {

    private var showOldToolbar: Boolean = false
    private var feedToolbar: Toolbar? = null

    companion object {
        const val TOOLBAR_GRADIENT = 1
        const val TOOLBAR_WHITE = 2

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
    private var isFabExpanded = false
    private var toolbarType = TOOLBAR_GRADIENT
    private var startToTransitionOffset = 0
    private var searchBarTransitionRange = 0
    private var isLightThemeStatusBar = false
    private var useNewInbox = false

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
        activity?.let {
            status_bar_bg.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
            status_bar_bg2.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(it)
        }
        initNavRevampAbTest()
        initInboxAbTest()
        initToolbar()
        initView()
        requestFeedTab()
    }

    private fun initNavRevampAbTest() {
        showOldToolbar = !RemoteConfigInstance.getInstance()
                .abTestPlatform
                .getString(EXP_NAME, VARIANT_OLD)
                .equals(VARIANT_REVAMP, true)
    }

    private fun initInboxAbTest() {
        useNewInbox = RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AbTestPlatform.KEY_AB_INBOX_REVAMP, AbTestPlatform.VARIANT_OLD_INBOX
        ) == AbTestPlatform.VARIANT_NEW_INBOX && !showOldToolbar
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
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.VISIBLE
            else -> View.GONE
        }
        status_bar_bg2.visibility = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.INVISIBLE
            else -> View.GONE
        }
        toolbarParent.removeAllViews()
//        if (showOldToolbar) {
//            initOldToolBar()
//        } else {
//            initNewToolBar()
//        }
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
        hideAllFab(false)
    }

    override fun onDestroy() {
        viewModel.tabResp.removeObservers(this)
        viewModel.whitelistResp.removeObservers(this)
        viewModel.flush()
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
        if (!isVisibleToUser) {
            hideAllFab(false)
        }
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
        hideAllFab(true)
        if (!userSession.isLoggedIn) {
            fab_feed.show()
            fab_feed.setOnClickListener { onGoToLogin() }
        }
        setAdapter()
        onNotificationChanged(badgeNumberNotification, badgeNumberInbox, badgeNumberCart) // notify badge after toolbar created
    }

    private fun requestFeedTab() {
        showLoading()
        viewModel.getDynamicTabs()
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
        val feedData = data.feedData.filter { it.type == FeedTabs.TYPE_FEEDS || it.type == FeedTabs.TYPE_EXPLORE || it.type == FeedTabs.TYPE_CUSTOM }
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
            viewModel.getWhitelist()
        }
    }

    private fun renderFab(whitelistDomain: WhitelistDomain) {
        if (userSession.isLoggedIn && whitelistDomain.authors.isNotEmpty()) {
            showFeedFab(whitelistDomain)
        }
    }

    private fun onErrorGetWhitelist(throwable: Throwable) {
        view?.let {
            Toaster.make(it, ErrorHandler.getErrorMessage(context, throwable), Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                if (userSession.isLoggedIn) {
                    viewModel.getWhitelist()
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
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> View.INVISIBLE
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.VISIBLE
                else -> View.GONE
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
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> View.INVISIBLE
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

    private fun showFeedFab(whitelistDomain: WhitelistDomain) {
        fab_feed.show()
        isFabExpanded = true
        when {
            isSellerMigrationEnabled(context) -> {
                val shopAppLink = UriUtil.buildUri(ApplinkConst.SHOP, userSession.shopId)
                val createPostAppLink = ApplinkConst.CONTENT_CREATE_POST
                fab_feed.setOnClickListener {
                    val intent = SellerMigrationActivity.createIntent(
                            context = requireContext(),
                            featureName = SellerMigrationFeatureName.FEATURE_POST_FEED,
                            screenName = FeedPlusContainerFragment::class.simpleName.orEmpty(),
                            appLinks = arrayListOf(ApplinkConstInternalSellerapp.SELLER_HOME, shopAppLink, createPostAppLink))
                    setupBottomSheetFeedSellerMigration(::goToCreateAffiliate, intent)
                    toolBarAnalytics.sendClickBuatFeedPostEvent()
                }
            }
            else -> {
                if (whitelistDomain.authors.size > 1) {
                    fab_feed.setOnClickListener(fabClickListener(whitelistDomain))
                } else if (whitelistDomain.authors.size == 1) {
                    val author = whitelistDomain.authors.first()
                    fab_feed.setOnClickListener { onGoToLink(author.link) }
                }
                toolBarAnalytics.sendClickBuatFeedPostEvent()
            }
        }
    }

    private fun fabClickListener(whitelistDomain: WhitelistDomain): View.OnClickListener {
        return View.OnClickListener {
            if (isFabExpanded) {
                hideAllFab(false)
            } else {
                fab_feed.animation = AnimationUtils.loadAnimation(activity, com.tokopedia.feedcomponent.R.anim.rotate_forward)
                layout_grey_popup.visibility = View.VISIBLE
                for (author in whitelistDomain.authors) {
                    if (author.type.equals(Author.TYPE_AFFILIATE, ignoreCase = true)) {
                        fab_feed_byme.show()
                        text_fab_byme.visibility = View.VISIBLE
                        text_fab_byme.text = author.title
                        fab_feed_byme.setOnClickListener { goToCreateAffiliate() }
                    } else {
                        fab_feed_shop.show()
                        text_fab_shop.visibility = View.VISIBLE
                        text_fab_shop.text = author.title
                        fab_feed_shop.setOnClickListener { onGoToLink(author.link) }
                    }
                }
                layout_grey_popup.setOnClickListener { hideAllFab(false) }
                isFabExpanded = true
            }
        }
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

    fun hideAllFab(isInitial: Boolean) {
        if (activity == null) {
            return
        }

        if (isInitial) {
            fab_feed.hide()
        } else {
            fab_feed.animation = AnimationUtils.loadAnimation(activity, com.tokopedia.feedcomponent.R.anim.rotate_backward)
        }
        fab_feed_byme.hide()
        fab_feed_shop.hide()
        text_fab_byme.visibility = View.GONE
        text_fab_shop.visibility = View.GONE
        layout_grey_popup.visibility = View.GONE
        isFabExpanded = false
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
        fab_feed.addOneTimeGlobalLayoutListener {
            val x1: Int = fab_feed.x.toInt()
            val y1: Int = fab_feed.y.toInt()
            val x2: Int = x1 + fab_feed.width
            val y2: Int = y1 + fab_feed.height

            coachMarkItem = CoachMarkItem(
                    fab_feed,
                    getString(R.string.feed_onboarding_create_post_title),
                    getString(R.string.feed_onboarding_create_post_detail)
            ).withCustomTarget(intArrayOf(x1, y1, x2, y2))

            showFabCoachMark()
        }
    }

    private fun showFabCoachMark() {
        if (::coachMarkItem.isInitialized && !affiliatePreference.isCreatePostEntryOnBoardingShown(userSession.userId) && !fab_feed.isOrWillBeHidden) {
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
}
