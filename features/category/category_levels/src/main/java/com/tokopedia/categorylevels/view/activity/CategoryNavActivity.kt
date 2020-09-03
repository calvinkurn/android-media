package com.tokopedia.categorylevels.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.categorylevels.R
import com.tokopedia.categorylevels.analytics.CategoryPageAnalytics.Companion.catAnalyticsInstance
import com.tokopedia.categorylevels.data.catalogModel.CategorySectionItem
import com.tokopedia.categorylevels.di.DaggerCategoryNavComponent
import com.tokopedia.categorylevels.view.fragment.CatalogNavFragment
import com.tokopedia.categorylevels.view.fragment.ProductNavFragment
import com.tokopedia.common_category.customview.SearchNavigationView
import com.tokopedia.common_category.fragment.BaseBannedProductFragment
import com.tokopedia.common_category.fragment.BaseCategorySectionFragment
import com.tokopedia.common_category.interfaces.CategoryNavigationListener
import com.tokopedia.common_category.model.bannedCategory.BannedData
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import com.tokopedia.filter.widget.BottomSheetFilterView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_category_nav.*
import javax.inject.Inject

private const val EXTRA_PARENT_ID = " PARENT_ID"
private const val EXTRA_PARENT_NAME = " PARENT_NAME"
private const val STATE_GRID = 1
private const val STATE_LIST = 2
private const val STATE_BIG = 3
private const val IS_BANNED = 1

class CategoryNavActivity : BaseActivity(), CategoryNavigationListener,
        SearchNavigationView.SearchNavClickListener,
        BaseCategorySectionFragment.SortAppliedListener,
        BaseBannedProductFragment.OnBannedFragmentInteractionListener,
        BottomSheetListener {

    private var categorySectionPagerAdapter: com.tokopedia.categorylevels.adapters.CategoryNavigationPagerAdapter? = null
    private var isForceSwipeToShop: Boolean = false
    private var activeTabPosition: Int = 0

    private val categorySectionItemList = ArrayList<CategorySectionItem>()

    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()

    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null

    private var bottomSheetFilterView: BottomSheetFilterView? = null
    private var searchNavContainer: SearchNavigationView? = null
    private var searchParameter: SearchParameter? = null
    private var departmentId: String = ""
    private var departmentName: String = ""
    private var parentId: String? = null
    private var parentName: String? = null

    private var categoryUrl: String? = null
    private var addCatalog: Boolean = true
    private var bannedData: BannedData? = null

    private lateinit var categoryNavComponent: com.tokopedia.categorylevels.di.CategoryNavComponent

    @JvmField
    @Inject
    var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var categoryNavViewModel: com.tokopedia.categorylevels.viewmodel.CategoryNavViewModel


    companion object {
        private const val ORDER_BY = "ob"
        private const val SCREEN_NAME = "/p"
        const val EXTRA_CATEGORY_NAME = "categoryName"

        const val CATEGORY_LEVELS_RESULT_TRACE = "category_levels_result_trace"
        const val CATEGORY_LEVELS_PLT_PREPARE_METRICS = "category_levels_plt_prepare_metrics"
        const val CATEGORY_LEVELS_PLT_NETWORK_METRICS = "category_levels_plt_network_metrics"
        const val CATEGORY_LEVELS_PLT_RENDER_METRICS = "category_levels_plt_render_metrics"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        startPerformanceMonitoring()
        setContentView(R.layout.activity_category_nav)
        bottomSheetFilterView = findViewById(R.id.bottomSheetFilter)
        searchNavContainer = findViewById(R.id.category_search_nav_container)
        fetchIntentData()
        initToolbar()
        prepareView()
        initializeSearchParameter(intent)
    }

    private fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startMonitoring(CATEGORY_LEVELS_RESULT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun sendScreenAnalytics() {
        //Empty to remove double open screen events
    }

    private fun sendOpenScreenAnalytics(data: Data) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, getDimensionMap(data))
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    private fun getDimensionMap(data: Data): Map<String, String>? {
        return catAnalyticsInstance.createOpenScreenEventMap(rootId = data.rootId, parent = data.parent, id = data.id.toString(), url = data.url)
    }

    private fun initializeSearchParameter(intent: Intent) {
        searchParameter = getSearchParameterFromIntentUri(intent)
    }

    override fun onSortApplied(showTick: Boolean) {
        searchNavContainer?.onSortSelected(showTick)
    }

    override fun hideBottomNavigation() {
        searchNavContainer?.visibility = View.GONE
    }

    fun showBottomNavigation() {
        searchNavContainer?.visibility = View.VISIBLE
    }

    override fun loadFilterItems(filters: java.util.ArrayList<Filter>?, searchParameter: Map<String, String>?) {
        bottomSheetFilterView?.loadFilterItems(filters, searchParameter)
    }

    override fun setFilterResultCount(formattedResultCount: String?) {
        bottomSheetFilterView?.setFilterResultCount(formattedResultCount)
    }

    override fun launchFilterBottomSheet() {
        bottomSheetFilterView?.launchFilterBottomSheet()
    }

    private fun getSearchParameterFromIntentUri(intent: Intent): SearchParameter {
        val uri = intent.data

        return if (uri == null) SearchParameter() else SearchParameter(uri.toString())
    }

    private fun initInjector() {
        categoryNavComponent = DaggerCategoryNavComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication)
                        .baseAppComponent).build()
        categoryNavComponent.inject(this)
    }

    private fun initSwitchButton() {
        searchNavContainer?.setSearchNavListener(this)

        img_display_button.tag = STATE_GRID
        img_display_button.setOnClickListener {

            for (navListener in navigationListenerList) {
                navListener.onChangeGridClick()
            }
            when (img_display_button.tag) {

                STATE_GRID -> {
                    catAnalyticsInstance.eventDisplayButtonClicked(departmentId, "grid")
                    img_display_button.tag = STATE_LIST
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, com.tokopedia.common_category.R.drawable.ic_list_display))
                }

                STATE_LIST -> {
                    catAnalyticsInstance.eventDisplayButtonClicked(departmentId, "list")
                    img_display_button.tag = STATE_BIG
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, com.tokopedia.common_category.R.drawable.ic_big_display))
                }
                STATE_BIG -> {
                    catAnalyticsInstance.eventDisplayButtonClicked(departmentId, "big")
                    img_display_button.tag = STATE_GRID
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, com.tokopedia.common_category.R.drawable.ic_grid_display))
                }
            }
        }
    }

    private fun prepareView() {
        this.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            categoryNavViewModel = viewModelProvider.get(com.tokopedia.categorylevels.viewmodel.CategoryNavViewModel::class.java)
            observeCategoryDetail()
            fetchCategoryDetail()
            bottomSheetFilterView?.initFilterBottomSheet(FilterTrackingData(
                    FilterEventTracking.Event.CLICK_CATEGORY,
                    FilterEventTracking.Category.FILTER_CATEGORY,
                    getCategoryId(),
                    FilterEventTracking.Category.PREFIX_CATEGORY_PAGE))

        }
    }

    private fun fetchCategoryDetail() {
        progressBar.show()
        categoryNavViewModel.fetchCategoryDetail(departmentId)
    }

    private fun observeCategoryDetail() {
        categoryNavViewModel.getRedirectionUrlLiveData().observe(this, Observer {
            progressBar.hide()
            when (it) {
                is Success -> {
                    moveToDiscovery(it.data)
                }
            }
        })

        categoryNavViewModel.getAdultProductLiveData().observe(this, Observer {
            progressBar.hide()
            when (it) {
                is Success -> {
                    updateToolBarHeading(it.data.name?:"")
                    bannedData = convertDataIfBanned(it.data)
                    openAdultPage()
                }
            }
        })

        categoryNavViewModel.getCategoryDetailLiveData().observe(this, Observer {
            progressBar.hide()
            when (it) {
                is Success -> {
                    updateToolBarHeading(it.data.name ?: "")
                    this.departmentId = it.data.id.toString()
                    sendOpenScreenAnalytics(it.data)
                    bannedData = convertDataIfBanned(it.data)
                    handleCategoryDetailSuccess()
                }
                is Fail -> {
                    setErrorPage()
                }
            }
        })

        categoryNavViewModel.getHasCatalogLiveData().observe(this, Observer {
            if (!it) {
                removeCatalogTab()
            }
            container.show()
        })
    }

    private fun convertDataIfBanned(data: Data): BannedData {
        return if (data.isBanned == IS_BANNED) {
            BannedData(data.name, data.bannedMessage, data.bannedMsgHeader, data.appRedirection, data.displayButton, data.isBanned)
        } else {
            BannedData()
        }
    }

    private fun removeCatalogTab() {
        category_tabs.hide()
        addCatalog = false
    }

    private fun handleCategoryDetailSuccess() {
        showBottomNavigation()
        initViewPager()
        loadSection()
        initSwitchButton()
        initBottomSheetListener()
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
        stopPerformanceMonitoring()
    }

    private fun openAdultPage() {
        AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_CATEGORY_PAGE, departmentId)
    }

    private fun moveToDiscovery(url: String?) {
        RouteManager.route(this, url)
        finish()
    }

    private fun setErrorPage() {
        hideBottomNavigation()
        view_pager_container.hide()
        layout_no_data.show()
    }

    private fun initBottomSheetListener() {
        bottomSheetFilterView?.setCallback(object : BottomSheetFilterView.Callback {
            override fun onApplyFilter(filterParameter: Map<String, String>?) {
                applyFilter(filterParameter)
            }

            override fun onShow() {
                hideBottomNavigation()
            }

            override fun onHide() {
                showBottomNavigation()
                sendBottomSheetHideEvent()
            }

            override fun getActivity(): AppCompatActivity {
                return this@CategoryNavActivity
            }
        })
    }

    private fun sendBottomSheetHideEvent() {
        val selectedFragment = categorySectionPagerAdapter?.getItem(pager.currentItem) as BaseCategorySectionFragment
        selectedFragment.onBottomSheetHide()
    }

    private fun applyFilter(filterParameter: Map<String, String>?) {

        if (filterParameter == null) return

        val selectedFragment = categorySectionPagerAdapter?.getItem(pager.currentItem) as BaseCategorySectionFragment

        if (filterParameter.isNotEmpty() && (filterParameter.size > 1 || !filterParameter.containsKey(ORDER_BY))) {
            searchNavContainer?.onFilterSelected(true)
        } else {
            searchNavContainer?.onFilterSelected(false)
        }
        val presentFilterList = selectedFragment.getSelectedFilter()
        if (presentFilterList.size < filterParameter.size) {
            for (i in filterParameter.entries) {
                if (!presentFilterList.containsKey(i.key)) {
                    catAnalyticsInstance.eventFilterApplied(departmentId, i.key, i.value)
                }
            }
        }

        selectedFragment.applyFilterToSearchParameter(filterParameter)
        selectedFragment.setSelectedFilter(HashMap(filterParameter))
        selectedFragment.clearDataFilterSort()
        selectedFragment.reloadData()
    }

    private fun fetchIntentData() {
        intent.data?.let {
            categoryUrl = it.toString()
            departmentId = it.pathSegments[0]
            departmentName = it.getQueryParameter(EXTRA_CATEGORY_NAME) ?: ""
            markDefaultFilterState(it)
        }

        intent.extras?.let { bundle ->
            if (bundle.containsKey(EXTRA_PARENT_ID))
                parentId = bundle.getString(EXTRA_PARENT_ID, "")

            if (bundle.containsKey(EXTRA_PARENT_NAME))
                parentName = bundle.getString(EXTRA_PARENT_NAME, "")
        }
    }

    private fun markDefaultFilterState(uri: Uri) {
        val map = URLParser(uri.encodedQuery ?: uri.toString()).paramKeyValueMap
        map.remove(EXTRA_CATEGORY_NAME)
        if (map.size > 0) {
            searchNavContainer?.onFilterSelected(true)
        }
    }

    private fun loadSection() {
        populateTab(categorySectionItemList)
        categorySectionPagerAdapter = com.tokopedia.categorylevels.adapters.CategoryNavigationPagerAdapter(supportFragmentManager)
        categorySectionPagerAdapter?.setData(categorySectionItemList)
        pager.adapter = categorySectionPagerAdapter
        category_tabs.tabLayout.setupWithViewPager(pager)
        setActiveTab()
    }

    private fun populateTab(searchSectionItemList: ArrayList<CategorySectionItem>) {
        addFragmentsToList(searchSectionItemList)
    }

    private fun addFragmentsToList(searchSectionItemList: ArrayList<CategorySectionItem>) {
        searchSectionItemList.add(CategorySectionItem("Produk", ProductNavFragment.newInstance(departmentId, departmentName, categoryUrl, bannedData)))
        if (addCatalog)
            searchSectionItemList.add(CategorySectionItem("Katalog", CatalogNavFragment.newInstance(departmentId, departmentName, bannedData)))
    }

    private fun setActiveTab() {
        pager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                pager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                pager.currentItem = 0
            }
        })
    }

    private fun initViewPager() {
        pager.offscreenPageLimit = 3
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    catAnalyticsInstance.eventClickProductTab(departmentId)
                } else {
                    catAnalyticsInstance.eventClickCatalogTab(departmentId)
                }
                onPageSelectedCalled(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


    private fun onPageSelectedCalled(position: Int) {
        this.isForceSwipeToShop = false
        this.activeTabPosition = position
        if (categorySectionPagerAdapter?.getItem(pager.currentItem) is BaseCategorySectionFragment) {
            val selectedFragment = categorySectionPagerAdapter?.getItem(pager.currentItem) as BaseCategorySectionFragment
            selectedFragment.resetSortTick()
            val filterParameter = selectedFragment.getSelectedFilter()
            if (filterParameter.isNotEmpty() && (filterParameter.size > 1 || !filterParameter.containsKey(ORDER_BY))) {
                searchNavContainer?.onFilterSelected(true)
            } else {
                searchNavContainer?.onFilterSelected(false)
            }
        }
    }

    private fun initToolbar() {
        action_up_btn.setOnClickListener {
            catAnalyticsInstance.eventBackButtonClicked(departmentId)
            onBackPressed()
        }
        updateToolBarHeading(departmentName)
        layout_search.setOnClickListener {
            catAnalyticsInstance.eventSearchBarClicked(departmentId)
            moveToAutoCompleteActivity(departmentName)
        }
    }

    private fun updateToolBarHeading(header: String) {
        departmentName = header
        et_search.text = header
    }

    private fun moveToAutoCompleteActivity(departMentName: String) {
        RouteManager.route(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + departMentName)
    }

    override fun setupSearchNavigation(clickListener: CategoryNavigationListener.ClickListener) {
        navigationListenerList.add(clickListener)
    }

    override fun setUpVisibleFragmentListener(visibleClickListener: CategoryNavigationListener.VisibleClickListener) {
        visibleFragmentListener = visibleClickListener
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        bottomSheetFilterView?.let {
            if (!it.onBackPressed()) {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AdultManager.handleActivityResult(this, requestCode, resultCode, data, object : AdultManager.Callback {
            override fun onFail() {
                finish()
            }

            override fun onVerificationSuccess(message: String?) {
                handleCategoryDetailSuccess()
            }

            override fun onLoginPreverified() {
                handleCategoryDetailSuccess()
            }

        })
        handleDefaultActivityResult(requestCode, resultCode, data)
    }

    private fun handleDefaultActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        bottomSheetFilterView?.onActivityResult(requestCode, resultCode, data)
    }

    fun getCategoryId(): String {
        return departmentId
    }

    override fun onSortButtonClicked() {
        visibleFragmentListener?.onSortClick()
        catAnalyticsInstance.eventSortClicked(departmentId)
    }

    override fun onFilterButtonClicked() {
        visibleFragmentListener?.onFilterClick()
        catAnalyticsInstance.eventFilterClicked(departmentId)
    }

    override fun onButtonClicked(bannedProduct: BannedData) {
        bannedProduct.name?.let { catAnalyticsInstance.eventBukaClick(bannedProduct.appRedirection.toString(), it) }
    }

    override fun onBannedFragmentAttached() {
        hideBottomNavigation()
    }

    private fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }
}
