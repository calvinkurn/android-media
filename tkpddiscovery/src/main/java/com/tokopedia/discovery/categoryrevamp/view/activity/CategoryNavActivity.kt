package com.tokopedia.discovery.categoryrevamp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tkpd.library.utils.URLParser
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.core.router.discovery.BrowseProductRouter
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.ui.customview.SearchNavigationView
import com.tokopedia.discovery.categoryrevamp.adapters.CategoryNavigationPagerAdapter
import com.tokopedia.discovery.categoryrevamp.analytics.CategoryPageAnalytics.Companion.catAnalyticsInstance
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.CategorySectionItem
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.Data
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.view.fragments.BannedProductFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseCategorySectionFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.CatalogNavFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.ProductNavFragment
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CategoryNavigationListener
import com.tokopedia.discovery.categoryrevamp.viewmodel.CategoryNavViewModel
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import com.tokopedia.filter.widget.BottomSheetFilterView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_category_nav.*
import kotlinx.android.synthetic.main.activity_category_nav.layout_no_data
import kotlinx.android.synthetic.main.fragment_product_nav.*
import kotlinx.android.synthetic.main.layout_nav_no_product.*
import javax.inject.Inject


class CategoryNavActivity : BaseActivity(), CategoryNavigationListener,
        SearchNavigationView.SearchNavClickListener,
        BaseCategorySectionFragment.SortAppliedListener,
        BannedProductFragment.OnBannedFragmentInteractionListener,
        BottomSheetListener {

    private var categorySectionPagerAdapter: CategoryNavigationPagerAdapter? = null
    private var isForceSwipeToShop: Boolean = false
    private var activeTabPosition: Int = 0

    private val categorySectionItemList = ArrayList<CategorySectionItem>()

    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()

    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null

    private val STATE_GRID = 1
    private val STATE_LIST = 2
    private val STATE_BIG = 3
    private var bottomSheetFilterView: BottomSheetFilterView? = null
    private var searchNavContainer: SearchNavigationView? = null


    private var searchParameter: SearchParameter? = null


    private val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
    private val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"
    private val EXTRA_PARENT_ID = " PARENT_ID"
    private val EXTRA_PARENT_NAME = " PARENT_NAME"

    private var departmentId: String = ""
    private var departmentName: String = ""
    private var parentId: String? = null
    private var parentName: String? = null

    private var categoryUrl: String? = null


    lateinit var categoryNavComponent: CategoryNavComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var categoryNavViewModel: CategoryNavViewModel

    companion object {
        private const val ORDER_BY = "ob"
        private const val IS_BANNED = 1
        private const val IS_ADULT = 1
        private const val SCREEN_NAME = "/p"
        fun isBannedNavigationEnabled(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_BANNED_NAVIGATION, true)
        }

        @JvmStatic
        fun isCategoryRevampEnabled(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_CATEGORY_REVAMP, true)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_nav)
        bottomSheetFilterView = findViewById(R.id.bottomSheetFilter)
        searchNavContainer = findViewById(R.id.search_nav_container)
        initInjector()
        prepareView()
        handleIntent(intent)
    }

    override fun sendScreenAnalytics() {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, getDimensionMap())
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    private fun getDimensionMap(): Map<String, String>? {
        return catAnalyticsInstance.createOpenScreenEventMap(parentId,parentName,departmentId,departmentName)
    }


    private fun handleIntent(intent: Intent) {
        getExtrasFromIntent(intent)
    }

    private fun getExtrasFromIntent(intent: Intent) {
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

    fun initInjector() {
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
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_list_display))
                }

                STATE_LIST -> {
                    catAnalyticsInstance.eventDisplayButtonClicked(departmentId, "list")
                    img_display_button.tag = STATE_BIG
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_big_display))
                }
                STATE_BIG -> {
                    catAnalyticsInstance.eventDisplayButtonClicked(departmentId, "big")
                    img_display_button.tag = STATE_GRID
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_grid_display))
                }
            }
        }
    }

    private fun prepareView() {
        this.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            categoryNavViewModel = viewModelProvider.get(CategoryNavViewModel::class.java)
            fetchBundle()
            initToolbar()
            progressBar.visibility = View.VISIBLE
            checkIfBanned()
            bottomSheetFilterView?.initFilterBottomSheet(FilterTrackingData(
                    FilterEventTracking.Event.CLICK_CATEGORY,
                    FilterEventTracking.Category.FILTER_CATEGORY,
                    getCategoryId(),
                    FilterEventTracking.Category.PREFIX_CATEGORY_PAGE))

        }

    }

    private fun checkIfBanned() {
        categoryNavViewModel.mBannedCheck.observe(this, Observer {
            when (it) {
                is Success -> {
                    progressBar.hide()
                    if (it.data.appRedirectionURL != null && !it.data.appRedirectionURL?.equals("")!!) {
                        RouteManager.route(this, it.data.appRedirectionURL)
                        finish()
                    } else {
                        if (it.data.isBanned == IS_BANNED) {
                            hideBottomNavigation()
                        } else {
                            showBottomNavigation()
                        }
                        if (it.data.isAdult == IS_ADULT) {
                            AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_CATEGORY_PAGE, departmentId)
                        }
                        initViewPager()
                        loadSection(it.data)
                        initSwitchButton()
                        initBottomSheetListener()
                    }
                }
                is Fail -> {
                    progressBar.hide()
                    setErrorPage()
                }
            }
        })
        categoryNavViewModel.fetchBannedCheck(getSubCategoryParam())
    }

    private fun setErrorPage() {
        hideBottomNavigation()
        layout_no_data.show()
        txt_no_data_header.text = resources.getText(R.string.category_nav_product_no_data_title)
        txt_no_data_description.text = resources.getText(R.string.category_nav_product_no_data_description)
    }

    private fun getSubCategoryParam(): RequestParams {
        val subCategoryMap = RequestParams()
        subCategoryMap.putString(CategoryNavConstants.IDENTIFIER, departmentId)
        subCategoryMap.putBoolean(CategoryNavConstants.INTERMEDIARY, false)
        subCategoryMap.putBoolean(CategoryNavConstants.SAFESEARCH, false)
        return subCategoryMap
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

    private fun fetchBundle() {
        val bundle = intent.extras
        bundle?.let {
            if (it.containsKey(BrowseProductRouter.EXTRA_CATEGORY_URL)) {
                categoryUrl = it.getString(BrowseProductRouter.EXTRA_CATEGORY_URL, "")
                val urlParser = URLParser(categoryUrl)
                departmentId = urlParser.getDepIDfromURI(this)
                departmentName = ""
                searchNavContainer?.onFilterSelected(true)
            }

            if (it.containsKey(EXTRA_CATEGORY_DEPARTMENT_ID)) {
                departmentId = bundle.getString(EXTRA_CATEGORY_DEPARTMENT_ID, "")
                departmentName = bundle.getString(EXTRA_CATEGORY_DEPARTMENT_NAME, "")

                if (bundle.containsKey(EXTRA_PARENT_ID)) {
                    parentId = bundle.getString(EXTRA_PARENT_ID, "")
                    parentName = bundle.getString(EXTRA_PARENT_NAME, "")
                }
            }
        }
    }

    private fun loadSection(data: Data) {
        departmentName = data.name ?: ""
        updateToolBarHeading(data.name ?: "")
        populateTab(categorySectionItemList, data)

        categorySectionPagerAdapter = CategoryNavigationPagerAdapter(supportFragmentManager)
        categorySectionPagerAdapter?.setData(categorySectionItemList)
        pager.adapter = categorySectionPagerAdapter
        tabs.setupWithViewPager(pager)

        setActiveTab()
    }

    private fun populateTab(searchSectionItemList: ArrayList<CategorySectionItem>, data: Data) {
        addFragmentsToList(searchSectionItemList, data)
    }

    private fun addFragmentsToList(searchSectionItemList: ArrayList<CategorySectionItem>, data: Data) {
        if (data.isBanned == IS_BANNED) {
            searchSectionItemList.add(CategorySectionItem("Produk", BannedProductFragment.newInstance(data)))
            searchSectionItemList.add(CategorySectionItem("Katalog", BannedProductFragment.newInstance(data)))
        }else{
            searchSectionItemList.add(CategorySectionItem("Produk", ProductNavFragment.newInstance(data, categoryUrl)))
            searchSectionItemList.add(CategorySectionItem("Katalog", CatalogNavFragment.newInstance(departmentId, departmentName)))
        }
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
        if(categorySectionPagerAdapter?.getItem(pager.currentItem) is BaseCategorySectionFragment) {
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
        if (item?.itemId == R.id.home) {
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
        AdultManager.handleActivityResult(this, requestCode, resultCode, data)
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

    override fun onButtonClicked(bannedProduct: Data) {
        bannedProduct.name?.let { catAnalyticsInstance.eventBukaClick(bannedProduct.appRedirection.toString(), it) }
    }
}
