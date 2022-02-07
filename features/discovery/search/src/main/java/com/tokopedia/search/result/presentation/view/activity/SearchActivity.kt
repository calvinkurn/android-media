package com.tokopedia.search.result.presentation.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionPagerAdapter
import com.tokopedia.search.result.presentation.view.listener.QuickFilterElevation
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.presentation.view.listener.SearchPerformanceMonitoringListener
import com.tokopedia.search.result.presentation.viewmodel.SearchViewModel
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModel
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModelFactoryModule
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Named

class SearchActivity : BaseActivity(),
        RedirectionListener,
        SearchNavigationListener,
        SearchPerformanceMonitoringListener,
        HasComponent<BaseAppComponent> {

    private var searchNavigationToolbar: NavToolbar? = null
    private var container: MotionLayout? = null
    private var loadingView: LoaderUnify? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var tabShadow: View? = null
    private var quickFilterTopPadding: View? = null
    private var searchSectionPagerAdapter: SearchSectionPagerAdapter? = null

    private var productTabTitle = ""
    private var shopTabTitle = ""
    private var autocompleteApplink = ""
    private var searchNavigationClickListener: SearchNavigationListener.ClickListener? = null
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    @Named(SearchConstant.Cart.CART_LOCAL_CACHE)
    lateinit var localCacheHandler: LocalCacheHandler

    @Inject
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_VIEW_MODEL_FACTORY)
    lateinit var searchShopViewModelFactory: ViewModelProvider.Factory

    @Inject
    @Named(SearchConstant.SEARCH_VIEW_MODEL_FACTORY)
    lateinit var searchViewModelFactory: ViewModelProvider.Factory

    private lateinit var searchViewModel: SearchViewModel // initialized in initViewModel
    private lateinit var searchShopViewModel: SearchShopViewModel // initialized in initViewModel
    private lateinit var searchParameter: SearchParameter // initialized in getExtrasFromIntent

    val pltPerformanceResultData
        get() = pageLoadTimePerformanceMonitoring?.getPltPerformanceData()

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity_search)

        setStatusBarColor()
        getExtrasFromIntent(intent)
        initActivityOnCreate()
        proceed()
        handleIntent()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                SearchConstant.SEARCH_RESULT_PLT_PREPARE_METRICS,
                SearchConstant.SEARCH_RESULT_PLT_NETWORK_METRICS,
                SearchConstant.SEARCH_RESULT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(SearchConstant.SEARCH_RESULT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDarkMode()) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }

    private fun getExtrasFromIntent(intent: Intent) {
        searchParameter = getSearchParameterFromIntentUri(intent)

        if (searchParameter.getSearchQuery().isEmpty()) {
            @Suppress("UNCHECKED_CAST")
            // Should be safe to cast non-null type to nullable type
            val map = searchParameter.getSearchParameterMap() as Map<String?, Any>
            SearchLogger().logAnomalyNoKeyword(UrlParamUtils.generateUrlParamString(map))
        }
    }

    private fun getSearchParameterFromIntentUri(intent: Intent): SearchParameter {
        val uri = intent.data
        val searchParameter = if (uri == null) SearchParameter() else SearchParameter(uri.toString())
        searchParameter.cleanUpNullValuesInMap()
        return searchParameter
    }

    private fun initActivityOnCreate() {
        initInjector()
    }

    private fun initInjector() {
        DaggerSearchViewComponent
                .builder()
                .baseAppComponent(component)
                .searchShopViewModelFactoryModule(SearchShopViewModelFactoryModule(searchParameter.getSearchParameterMap()))
                .build()
                .inject(this)
    }

    private fun proceed() {
        findViews()
        prepareView()
    }

    private fun findViews() {
        searchNavigationToolbar = findViewById(R.id.searchNavigationToolbar)
        container = findViewById(R.id.container)
        loadingView = findViewById(R.id.progressBar)
        tabLayout = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.pager)
        tabShadow = findViewById(R.id.search_top_bar_shadow)
        quickFilterTopPadding = findViewById(R.id.search_quick_filter_top_padding)
    }

    private fun prepareView() {
        initToolbar()
        initViewPager()
        configureTabLayout()
    }

    private fun initToolbar() {
        configureSearchNavigationToolbar()
        configureToolbarVisibility()
    }

    private fun configureSearchNavigationToolbar() {
        setSearchNavigationToolbar()
    }

    private fun setSearchNavigationToolbar() {
        searchNavigationToolbar?.let {
            this.lifecycle.addObserver(it)
            it.bringToFront()
            it.setToolbarPageName(SearchConstant.SEARCH_RESULT_PAGE)
            it.setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_CART, disableRouteManager = false, disableDefaultGtmTracker = false) { }
                            .addIcon(IconList.ID_NAV_GLOBAL, disableRouteManager = false, disableDefaultGtmTracker = false) { }
            )
        }
    }

    private fun moveToAutoCompleteActivity() {
        val query = URLEncoder.encode(searchParameter.getSearchQuery()).replace("+", " ")
        val currentAutoCompleteApplink = getAutoCompleteApplink(query)

        val autoCompleteParams = URLParser(currentAutoCompleteApplink).paramKeyValueMap
        autoCompleteParams[SearchApiConst.PREVIOUS_KEYWORD] = query

        startActivityWithApplink(
                ApplinkConstInternalDiscovery.AUTOCOMPLETE
                        + "?"
                        + UrlParamUtils.generateUrlParamString(autoCompleteParams)
        )
    }

    override fun startActivityWithApplink(applink: String?, vararg parameter: String?) {
        RouteManager.route(this, applink, *parameter)
    }

    private fun getAutoCompleteApplink(query: String) =
            if (autocompleteApplink.isNotEmpty()) autocompleteApplink
            else ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + query


    private fun configureToolbarVisibility() {
        if (!isLandingPage()) return

        tabLayout?.gone()
        tabShadow?.gone()
        quickFilterTopPadding?.gone()
    }

    private fun isLandingPage() =
            searchParameter.getBoolean(SearchApiConst.LANDING_PAGE)

    private fun initViewPager() {
        viewPager?.offscreenPageLimit = 2
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                this@SearchActivity.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun onPageSelected(position: Int) {
        when (position) {
            SearchTabPosition.TAB_FIRST_POSITION -> {
                SearchTracking.eventSearchResultTabClick(productTabTitle)
                searchSectionPagerAdapter?.getProductListFragment()?.onViewPagerChanged()
            }
            SearchTabPosition.TAB_SECOND_POSITION -> {
                SearchTracking.eventSearchResultTabClick(shopTabTitle)
                searchSectionPagerAdapter?.getShopListFragment()?.onViewPagerChanged()
            }
        }
    }

    private fun configureTabLayout() {
        if (isLandingPage()
                || Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) return

        container?.let {
            it.loadLayoutDescription(R.xml.search_tab_layout_scene)
            it.setTransition(R.id.searchMotionTabStart, R.id.searchMotionTabEnd)
            it.setTransitionListener(getContainerTransitionListener())
        }
    }

    private fun getContainerTransitionListener(): MotionLayout.TransitionListener? {
        return object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, i: Int, i1: Int) {}
            override fun onTransitionChange(motionLayout: MotionLayout, i: Int, i1: Int, v: Float) {}
            override fun onTransitionTrigger(motionLayout: MotionLayout, i: Int, b: Boolean, v: Float) {}
            override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
                onContainerTransitionCompleted(i)
            }
        }
    }

    private fun onContainerTransitionCompleted(id: Int) {
        val viewPager = viewPager ?: return
        val fragmentItem = searchSectionPagerAdapter?.getRegisteredFragmentAtPosition(viewPager.currentItem)

        if (fragmentItem !is QuickFilterElevation) return

        when (id) {
            R.id.searchMotionTabStart -> fragmentItem.configure(true)
            R.id.searchMotionTabEnd -> fragmentItem.configure(false)
        }
    }

    private fun handleIntent() {
        initResources()
        initViewModel()
        observeViewModel()
        performProductSearch()
        setToolbarTitle()
    }

    private fun initResources() {
        productTabTitle = getString(R.string.product_tab_title)
        shopTabTitle = getString(R.string.shop_tab_title)
    }

    private fun initViewModel() {
        searchViewModel = ViewModelProvider(this, searchViewModelFactory).get(SearchViewModel::class.java)
        searchShopViewModel = ViewModelProvider(this, searchShopViewModelFactory).get(SearchShopViewModel::class.java)
    }

    private fun observeViewModel() {
        observeAutoCompleteEvent()
        observeHideLoadingEvent()
    }

    private fun observeAutoCompleteEvent() {
        searchViewModel.getShowAutoCompleteViewEventLiveData().observe(this, EventObserver {
            showSearchInputView()
        })
    }

    private fun observeHideLoadingEvent() {
        searchViewModel.getHideLoadingEventLiveData().observe(this, EventObserver {
            removeSearchPageLoading()
        })
    }

    private fun performProductSearch() {
        setSearchParameterDefaultActiveTab()
        onSearchingStart()
        loadSection()
    }

    private fun setSearchParameterDefaultActiveTab() {
        val activeTab = searchParameter.get(SearchApiConst.ACTIVE_TAB)
        if (shouldSetActiveTabToDefault(activeTab)) {
            searchParameter.set(SearchApiConst.ACTIVE_TAB, SearchConstant.ActiveTab.PRODUCT)
        }
    }

    private fun shouldSetActiveTabToDefault(activeTab: String): Boolean {
        return activeTab !in listOf(
                SearchConstant.ActiveTab.PRODUCT,
                SearchConstant.ActiveTab.SHOP,
        )
    }

    private fun onSearchingStart() {
        showLoadingView(true)
        showContainer(false)
    }

    private fun showLoadingView(visible: Boolean) {
        loadingView?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showContainer(visible: Boolean) {
        container?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }


    private fun loadSection() {
        val searchFragmentTitles = mutableListOf<String>()
        addFragmentTitlesToList(searchFragmentTitles)
        initTabLayout()

        searchSectionPagerAdapter = SearchSectionPagerAdapter(supportFragmentManager, searchParameter)
        searchSectionPagerAdapter?.updateData(searchFragmentTitles)
        viewPager?.adapter = searchSectionPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)

        setActiveTab()
    }

    private fun addFragmentTitlesToList(searchSectionItemList: MutableList<String>) {
        searchSectionItemList.add(productTabTitle)

        if (!isLandingPage()) searchSectionItemList.add(shopTabTitle)
    }


    private fun initTabLayout() {
        tabLayout?.clearOnTabSelectedListeners()
        tabLayout?.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            override fun onTabReselected(tab: TabLayout.Tab) {
                this@SearchActivity.onTabReselected(tab.position)
            }
        })
    }

    private fun onTabReselected(tabPosition: Int) {
        when (tabPosition) {
            SearchTabPosition.TAB_FIRST_POSITION -> productListFragmentExecuteBackToTop()
            SearchTabPosition.TAB_SECOND_POSITION -> shopListFragmentExecuteBackToTop()
        }
    }

    private fun productListFragmentExecuteBackToTop() {
        searchSectionPagerAdapter?.getProductListFragment()?.backToTop()
    }

    private fun shopListFragmentExecuteBackToTop() {
        searchSectionPagerAdapter?.getShopListFragment()?.backToTop()
    }

    private fun setActiveTab() {
        viewPager?.viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewPager?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                viewPager?.currentItem = getViewPagerCurrentItem()
            }
        })
    }

    private fun getViewPagerCurrentItem() =
            when (searchParameter.get(SearchApiConst.ACTIVE_TAB)) {
                SearchConstant.ActiveTab.SHOP -> SearchTabPosition.TAB_SECOND_POSITION
                else -> SearchTabPosition.TAB_FIRST_POSITION
            }

    private fun setToolbarTitle() {
        configureSearchNavigationSearchBar()
    }

    private fun configureSearchNavigationSearchBar() {
        val query = searchParameter.getSearchQuery()

        searchNavigationToolbar?.setupSearchbar(
                hints = listOf(HintData(query, query)),
                applink = "",
                searchbarClickCallback = this::onSearchNavigationSearchBarClicked,
                searchbarImpressionCallback = null,
                durationAutoTransition = 0,
                shouldShowTransition = true,
                disableDefaultGtmTracker = false,
        )
    }

    private fun onSearchNavigationSearchBarClicked(ignored: String) {
        moveToAutoCompleteActivity()
    }

    override fun onResume() {
        super.onResume()
        setSearchNavigationCartButton()
    }

    private fun setSearchNavigationCartButton() {
        if (userSession.isLoggedIn)
            setSearchNavigationCartButtonCount()
    }

    private fun setSearchNavigationCartButtonCount() {
        searchNavigationToolbar?.setBadgeCounter(IconList.ID_CART, getCartCount())
    }

    private fun getCartCount() =
            localCacheHandler.getInt(SearchConstant.Cart.CACHE_TOTAL_CART, 0)

    override fun isAllowShake() = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(SearchConstant.EXTRA_SEARCH_PARAMETER_MODEL, searchParameter)
    }

    override fun removeSearchPageLoading() {
        showLoadingView(false)
        showContainer(true)
    }

    override fun showSearchInputView() {
        moveToAutoCompleteActivity()
    }

    override fun setAutocompleteApplink(autocompleteApplink: String?) {
        this.autocompleteApplink = autocompleteApplink ?: ""
    }

    override fun setupSearchNavigation(clickListener: SearchNavigationListener.ClickListener?) {
        searchNavigationClickListener = clickListener
    }

    override fun refreshMenuItemGridIcon(titleResId: Int, iconResId: Int) {
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun startActivityWithUrl(url: String?, vararg parameter: String?) {
        RouteManager.route(this, url, *parameter)
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
    }

    override fun updateSearchParameter(searchParameter: SearchParameter?) {
        if (searchParameter == null) return

        this.searchParameter = searchParameter

        updateKeyword()
    }

    private fun updateKeyword() {
        setToolbarTitle()
    }
}