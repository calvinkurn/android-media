package com.tokopedia.search.result.presentation.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.bytedance.android.btm.api.BtmSDK
import com.bytedance.android.btm.api.model.PageShowParams
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamKey.SEARCH_ENTRANCE
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.CLICK_SEARCH_BAR
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.GOODS_SEARCH
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.STORE_SEARCH
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.discovery.common.analytics.SearchEntrance
import com.tokopedia.discovery.common.analytics.SearchSessionId
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ACTIVE_TAB
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.MPS
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PREVIOUS_KEYWORD
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.PAGE_NAME
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.home_component.usecase.thematic.ThematicModel
import com.tokopedia.home_component.util.ImageLoaderStateListener
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.databinding.SearchActivitySearchBinding
import com.tokopedia.search.di.module.SearchContextModule
import com.tokopedia.search.result.SearchParameterModule
import com.tokopedia.search.result.SearchState
import com.tokopedia.search.result.SearchViewModel
import com.tokopedia.search.result.presentation.view.adapter.MPSPagerAdapter
import com.tokopedia.search.result.presentation.view.adapter.SearchSectionPagerAdapter
import com.tokopedia.search.result.presentation.view.adapter.SearchViewPagerAdapter
import com.tokopedia.search.result.presentation.view.listener.QuickFilterElevation
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_TRACE
import com.tokopedia.search.result.product.performancemonitoring.searchProductPerformanceMonitoring
import com.tokopedia.search.utils.BackToTopView
import com.tokopedia.search.utils.SearchLogger
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.search.utils.enterMethodMap
import com.tokopedia.search.utils.mvvm.SearchView
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.SearchRollenceController
import com.tokopedia.telemetry.ITelemetryActivity
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.android.synthetic.main.search_activity_search.*
import kotlinx.android.synthetic.main.search_activity_search.view.*
import kotlinx.coroutines.flow.collectLatest
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Named
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class SearchActivity :
    BaseActivity(),
    SearchView,
    RedirectionListener,
    SearchNavigationListener,
    PageLoadTimePerformanceInterface by searchProductPerformanceMonitoring(),
    HasComponent<BaseAppComponent>,
    ITelemetryActivity,
    AppLogInterface {

    private var searchNavigationToolbar: NavToolbar? = null
    private var container: MotionLayout? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var tabShadow: View? = null
    private var quickFilterTopPadding: View? = null
    private var viewPagerAdapter: SearchViewPagerAdapter? = null

    private var productTabTitle = ""
    private var shopTabTitle = ""
    private var autocompleteApplink = ""

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    @Named(SearchConstant.Cart.CART_LOCAL_CACHE)
    lateinit var localCacheHandler: LocalCacheHandler

    @Inject
    @Suppress("LateinitUsage")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @Suppress("LateinitUsage")
    lateinit var fragmentFactory: FragmentFactory

    private val searchViewModel: SearchViewModel? by viewModels { viewModelFactory }
    private var searchComponent: SearchComponent? = null
    private lateinit var searchParameter: SearchParameter // initialized in getExtrasFromIntent

    private val binding: SearchActivitySearchBinding? by viewBinding()

    override fun getPageName(): String = AppLogSearch.ParamValue.SEARCH_RESULT

    override fun isEnterFromWhitelisted(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        startMonitoring(SEARCH_RESULT_TRACE)
        startPreparePagePerformanceMonitoring()

        getExtrasFromIntent(intent)
        initActivityOnCreate()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity_search)
        fetchRollence()

        setStatusBarColor()
        proceed()
        handleIntent()
        observeSearchState()

        SearchSessionId.update()
        AppLogAnalytics.putPageData(SEARCH_ENTRANCE, SearchEntrance.value())
        setAdsPageData()
        handleSearchHeaderThematic()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppLogTopAds.removeLastAdsPageData(this)
    }

    private fun setAdsPageData() {
        AppLogTopAds.currentPageName = PageName.SEARCH_RESULT
        AppLogTopAds.putAdsPageData(this, AppLogParam.PAGE_NAME, PageName.SEARCH_RESULT)
    }

    private fun fetchRollence() {
        SearchRollenceController.fetchInboxNotifTopNavValue()
    }

    private fun observeSearchState() {
        lifecycleScope.launchWhenCreated {
            searchViewModel?.stateFlow?.collectLatest {
                if (it.shouldShowThematic()) {
                    it.thematicModel?.let { it1 ->
                        showThematic(
                            it1
                        )
                    }
                }
            }
        }
    }

    private fun handleSearchHeaderThematic() {
        if (!DeviceScreenInfo.isTablet(this)) {
            searchViewModel?.getThematic()
        }
    }

    /**
     * @param statusBarColor: color resource id
     */
    private fun setStatusBarColor(statusBarColor: Int = unifyprinciplesR.color.Unify_NN0) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDarkMode()) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
    }

    private fun getExtrasFromIntent(intent: Intent) {
        searchParameter = getSearchParameterFromIntentUri(intent)

        if (searchParameter.getSearchQuery().isEmpty()) {
            // Should be safe to cast non-null type to nullable type
            @Suppress("UNCHECKED_CAST")
            val map = searchParameter.getSearchParameterMap() as Map<String?, Any>
            SearchLogger().logAnomalyNoKeyword(UrlParamUtils.generateUrlParamString(map))
        }
    }

    private fun getSearchParameterFromIntentUri(intent: Intent): SearchParameter {
        val uri = intent.data
        val searchParameter =
            if (uri == null) SearchParameter() else SearchParameter(uri.toString())
        searchParameter.cleanUpNullValuesInMap()
        return searchParameter
    }

    private fun initActivityOnCreate() {
        initInjector()
    }

    private fun initInjector() {
        searchComponent = DaggerSearchComponent
            .builder()
            .baseAppComponent(component)
            .searchContextModule(SearchContextModule(this))
            .searchParameterModule(SearchParameterModule(searchParameter))
            .build()

        searchComponent?.inject(this)
    }

    private fun proceed() {
        findViews()
        prepareView()
        configureSearchBox()
    }

    private fun findViews() {
        searchNavigationToolbar = findViewById(R.id.searchNavigationToolbar)
        container = findViewById(R.id.container)
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
                IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.SRP))
                    .addIcon(
                        IconList.ID_CART,
                        disableRouteManager = false,
                        disableDefaultGtmTracker = false
                    ) {
                        AppLogSearch.eventCartEntranceClick()
                    }
                    .addIcon(
                        IconList.ID_NAV_GLOBAL,
                        disableRouteManager = false,
                        disableDefaultGtmTracker = false
                    ) { }
            )

            AppLogSearch.eventCartEntranceShow()
        }
    }

    private fun moveToAutoCompleteActivity() {
        val applink = ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?" + autoCompleteParamsString()
        RouteManager.route(this, applink)
    }

    private fun autoCompleteParamsString() =
        UrlParamUtils.generateUrlParamString(autoCompleteParams())

    private fun autoCompleteParams(): Map<String?, String> =
        if (searchParameter.isMps()) autoCompleteMPSParams()
        else autoCompleteApplinkParams() + enterMethodMap(CLICK_SEARCH_BAR)

    private fun autoCompleteMPSParams() = searchParameter.getSearchQueryMap() + mapOf(ACTIVE_TAB to MPS)

    private fun autoCompleteApplinkParams(): Map<String, String> {
        val query = encodedQuery()
        val currentAutoCompleteApplink = currentAutoCompleteApplink(query)
        val currentAutoCompleteParams = URLParser(currentAutoCompleteApplink).paramKeyValueMap

        return currentAutoCompleteParams + mapOf(PREVIOUS_KEYWORD to query)
    }

    private fun encodedQuery() = URLEncoder
        .encode(searchParameter.getSearchQuery())
        .replace("+", " ")

    private fun currentAutoCompleteApplink(query: String) =
        autocompleteApplink.ifEmpty { ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + query }

    override fun startActivityWithApplink(applink: String?, vararg parameter: String?) {
        RouteManager.route(this, applink, *parameter)
    }

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
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                searchViewModel?.setActiveTab(position)
                this@SearchActivity.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun onPageSelected(position: Int) {
        when (position) {
            SearchTabPosition.TAB_FIRST_POSITION ->
                SearchTracking.eventSearchResultTabClick(productTabTitle)

            SearchTabPosition.TAB_SECOND_POSITION ->
                SearchTracking.eventSearchResultTabClick(shopTabTitle)
        }
        val fragmentItem = viewPagerAdapter?.getRegisteredFragmentAtPosition(position)
        fragmentItem?.let { BtmSDK.onPageShow(it, true, PageShowParams().apply { reuse = true }) }
    }

    private fun configureTabLayout() {
        if (isLandingPage() ||
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP
        ) {
            return
        }

        container?.let {
            it.loadLayoutDescription(R.xml.search_tab_layout_scene)
            it.setTransition(R.id.searchMotionTabStart, R.id.searchMotionTabEnd)
            it.setTransitionListener(getContainerTransitionListener())
        }
    }

    private fun getContainerTransitionListener(): MotionLayout.TransitionListener {
        return object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, i: Int, i1: Int) {}
            override fun onTransitionChange(
                motionLayout: MotionLayout,
                i: Int,
                i1: Int,
                v: Float
            ) {
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout,
                i: Int,
                b: Boolean,
                v: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
                onContainerTransitionCompleted(i)
            }
        }
    }

    private fun onContainerTransitionCompleted(id: Int) {
        val viewPager = viewPager ?: return
        val fragmentItem = viewPagerAdapter?.getRegisteredFragmentAtPosition(viewPager.currentItem)

        if (fragmentItem !is QuickFilterElevation) return

        when (id) {
            R.id.searchMotionTabStart -> fragmentItem.configure(true)
            R.id.searchMotionTabEnd -> fragmentItem.configure(false)
        }
    }

    private fun handleIntent() {
        initResources()

        observeViewModel()

        performProductSearch()
        setToolbarTitle()
    }

    private fun initResources() {
        productTabTitle = getString(R.string.product_tab_title)
        shopTabTitle = getString(R.string.shop_tab_title)
    }

    private fun observeViewModel() {
        searchViewModel?.apply {
            observeState()

            onEach(SearchState::activeTabPosition, ::setViewPagerCurrentItem)
        }
    }

    private fun setViewPagerCurrentItem(position: Int) {
        viewPager?.post {
            viewPager?.currentItem = position
        }
    }

    private fun performProductSearch() {
        setSearchParameterDefaultActiveTab()
        loadSection()
    }

    private fun setSearchParameterDefaultActiveTab() {
        val activeTab = searchParameter.get(ACTIVE_TAB)
        if (shouldSetActiveTabToDefault(activeTab)) {
            searchParameter.set(ACTIVE_TAB, SearchConstant.ActiveTab.PRODUCT)
        }
    }

    private fun shouldSetActiveTabToDefault(activeTab: String): Boolean {
        return activeTab !in listOf(
            SearchConstant.ActiveTab.PRODUCT,
            SearchConstant.ActiveTab.SHOP,
            SearchConstant.ActiveTab.MPS
        )
    }

    private fun loadSection() {
        val searchFragmentTitles = mutableListOf<String>()
        addFragmentTitlesToList(searchFragmentTitles)
        initTabLayout()

        viewPagerAdapter = createPagerAdapter(searchFragmentTitles).also {
            viewPager?.adapter = it.asViewPagerAdapter()
        }

        tabLayout?.setupWithViewPager(viewPager)
    }

    private fun createPagerAdapter(searchFragmentTitles: List<String>): SearchViewPagerAdapter =
        if (searchParameter.isMps()) {
            MPSPagerAdapter(
                supportFragmentManager,
                searchFragmentTitles,
                classLoader,
                supportFragmentManager.fragmentFactory
            )
        } else {
            SearchSectionPagerAdapter(
                supportFragmentManager,
                searchFragmentTitles,
                searchParameter,
                classLoader,
                supportFragmentManager.fragmentFactory,
            )
        }

    private fun addFragmentTitlesToList(searchSectionItemList: MutableList<String>) {
        searchSectionItemList.add(productTabTitle)

        if (!isLandingPage()) searchSectionItemList.add(shopTabTitle)
    }

    private fun initTabLayout() {
        tabLayout?.clearOnTabSelectedListeners()
        tabLayout?.addOnTabSelectedListener(object :
                TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                override fun onTabReselected(tab: TabLayout.Tab) {
                    this@SearchActivity.onTabReselected(tab.position)
                }
            })
    }

    private fun onTabReselected(tabPosition: Int) {
        when (tabPosition) {
            SearchTabPosition.TAB_FIRST_POSITION -> firstPositionFragmentReselected()
            SearchTabPosition.TAB_SECOND_POSITION -> secondPositionFragmentReselected()
        }
    }

    private fun firstPositionFragmentReselected() {
        val firstPageFragment = viewPagerAdapter?.getFirstPageFragment()
        if (firstPageFragment is BackToTopView) {
            firstPageFragment.backToTop()
        }
        AppLogAnalytics.putPageData(PAGE_NAME, GOODS_SEARCH)
    }

    private fun secondPositionFragmentReselected() {
        val secondPageFragment = viewPagerAdapter?.getSecondPageFragment()
        if (secondPageFragment is BackToTopView) {
            secondPageFragment.backToTop()
        }
        AppLogAnalytics.putPageData(PAGE_NAME, STORE_SEARCH)
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
            disableDefaultGtmTracker = false
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
        if (userSession.isLoggedIn) {
            setSearchNavigationCartButtonCount()
        }
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

    override fun showSearchInputView() {
        moveToAutoCompleteActivity()
    }

    override fun setAutocompleteApplink(autocompleteApplink: String?) {
        this.autocompleteApplink = autocompleteApplink ?: ""
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun startActivityWithUrl(url: String?, vararg parameter: String?) {
        RouteManager.route(this, url, *parameter)
    }

    override fun updateSearchParameter(searchParameter: SearchParameter?) {
        if (searchParameter == null) return

        this.searchParameter = searchParameter

        updateKeyword()
    }

    override fun updateSearchBarNotification() {
        searchNavigationToolbar?.updateNotification()
    }

    private fun updateKeyword() {
        setToolbarTitle()
    }

    override fun getTelemetrySectionName() = "search"

    override fun refresh() = withState(searchViewModel) {
        if (it.isOpeningAutoComplete) {
            showSearchInputView()
            searchViewModel?.showAutoCompleteHandled()
        }
    }

    private fun showThematic(thematicModel: ThematicModel) = binding?.let {
        val thematicImageLoadListener = object : ImageLoaderStateListener {
            override fun successLoad(view: ImageView) {
                view.show()
                setThematicStatusBar()
            }

            override fun failedLoad(view: ImageView) {
                view.hide()
            }
        }

        val thematicHeight = calculateThematicHeight(thematicModel)
        it.thematicForeground.setLayoutHeight(thematicHeight)
        it.thematicBackground.setLayoutHeight(thematicHeight)

        it.thematicForeground.loadImageWithoutPlaceholder(
            thematicModel.foregroundImageURL,
            "thematicForeground",
            thematicImageLoadListener,
            skipErrorPlaceholder = true
        )
        it.thematicBackground.loadImageWithoutPlaceholder(
            thematicModel.backgroundImageURL,
            "thematicBackground",
            thematicImageLoadListener,
            skipErrorPlaceholder = true
        )

        thematic_container.visibility = View.VISIBLE
        setThematicTheme()
    }

    private fun calculateThematicHeight(thematicModel: ThematicModel): Int {
        return (thematicModel.heightPercentage * getScreenHeight()) / 100
    }

    /**
     * Status bar set to have layout underneath and set as transparent so that layout can be seen
     */
    private fun setThematicStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        container?.loadLayoutDescription(R.xml.search_tab_layout_thematic_scene)
        val statusBarHeight = DisplayMetricUtils.getStatusBarHeight(this@SearchActivity)
        container?.getConstraintSet(R.id.searchMotionTabStart)?.setMargin(
            R.id.searchNavigationToolbar,
            ConstraintSet.TOP,
            statusBarHeight
        )
        container?.getConstraintSet(R.id.searchMotionTabEnd)?.setMargin(
            R.id.searchNavigationToolbar,
            ConstraintSet.TOP,
            statusBarHeight
        )
    }

    /**
     * Set navigation toolbar background to transparent, then switch bar so that its re-rendered
     * Should also change nav back button's color according to theme
     * Should be called after succesfuly inflating thematic
     */
    private fun setThematicTheme() {
        val state = searchViewModel?.stateFlow?.value ?: return
        binding?.let {
            searchNavigationToolbar?.setIconAndNavRecyclerCustomColor(
                ContextCompat.getColor(
                    this,
                    unifyprinciplesR.color.Unify_Static_Black
                ),
                ContextCompat.getColor(
                    this,
                    unifyprinciplesR.color.Unify_Static_White
                )
            )
            if (state.isThematicLightMode()) {
                searchNavigationToolbar?.switchToLightToolbar(force = true)
            } else if (state.isThematicDarkMode()) {
                searchNavigationToolbar?.switchToDarkToolbar(true)
            } else {
                searchNavigationToolbar?.switchToolbarBasedOnUiMode()
            }
            searchNavigationToolbar?.setBackgroundAlpha(0f)
        }
    }

    private fun configureSearchBox() {
        searchNavigationToolbar?.updateSearchBarStyle(
            showSearchBtn = false,
            useDarkerPlaceholder = true
        )
    }
}
