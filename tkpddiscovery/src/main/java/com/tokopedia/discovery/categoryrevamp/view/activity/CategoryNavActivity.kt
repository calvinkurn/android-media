package com.tokopedia.discovery.categoryrevamp.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.analytics.CategoryPageAnalytics.Companion.catAnalyticsInstance
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseCategorySectionFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.CatalogNavFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.ProductNavFragment
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CategoryNavigationListener
import com.tokopedia.discovery.common.data.Filter
import com.tokopedia.discovery.newdiscovery.base.BottomSheetListener
import com.tokopedia.discovery.newdiscovery.search.adapter.SearchSectionPagerAdapter
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter
import com.tokopedia.discovery.newdiscovery.search.model.SearchSectionItem
import com.tokopedia.discovery.newdiscovery.widget.BottomSheetFilterView
import kotlinx.android.synthetic.main.activity_category_nav.*


class CategoryNavActivity : BaseActivity(), CategoryNavigationListener, BottomSheetListener {

    override fun hideBottomNavigation() {
        searchNavContainer?.visibility = View.GONE
    }

    fun showBottomNavigation() {
        searchNavContainer?.visibility = View.VISIBLE
    }

    override fun loadFilterItems(filters: java.util.ArrayList<Filter>?, searchParameter: MutableMap<String, String>?) {
        bottomSheetFilterView?.loadFilterItems(filters, searchParameter)
    }

    override fun setFilterResultCount(formattedResultCount: String?) {
        bottomSheetFilterView?.setFilterResultCount(formattedResultCount)
    }

    override fun closeFilterBottomSheet() {
        bottomSheetFilterView?.closeView()
    }

    override fun isBottomSheetShown(): Boolean {
        return bottomSheetFilterView?.isBottomSheetShown ?: false
    }

    override fun launchFilterBottomSheet() {
        bottomSheetFilterView?.launchFilterBottomSheet()
    }

    private var searchSectionPagerAdapter: SearchSectionPagerAdapter? = null
    private var isForceSwipeToShop: Boolean = false
    private var activeTabPosition: Int = 0

    val searchSectionItemList = ArrayList<SearchSectionItem>()

    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()

    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null

    private val STATE_GRID = 1
    private val STATE_LIST = 2
    private val STATE_BIG = 3
    private var bottomSheetFilterView: BottomSheetFilterView? = null
    private var searchNavContainer: View? = null


    private var searchParameter: SearchParameter? = null


    private val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
    private val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"

    private var departmentId: String = ""
    private var departmentName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_nav)
        bottomSheetFilterView = findViewById(R.id.bottomSheetFilter)
        searchNavContainer = findViewById(R.id.search_nav_container)

        prepareView()
        handleIntent(intent)


    }

    private fun handleIntent(intent: Intent) {
        getExtrasFromIntent(intent)
    }

    private fun getExtrasFromIntent(intent: Intent) {
        searchParameter = getSearchParameterFromIntentUri(intent)
        // isForceSwipeToShop = intent.getBooleanExtra(EXTRA_FORCE_SWIPE_TO_SHOP, false)
    }

    private fun getSearchParameterFromIntentUri(intent: Intent): SearchParameter {
        val uri = intent.data

        return if (uri == null) SearchParameter() else SearchParameter(uri.toString())
    }


    private fun initSwitchButton() {

        icon_sort.setOnClickListener {
            visibleFragmentListener?.onSortClick()
            catAnalyticsInstance.eventSortClicked(departmentId)

        }
        button_sort.setOnClickListener {
            visibleFragmentListener?.onSortClick()
            catAnalyticsInstance.eventSortClicked(departmentId)
        }

        icon_filter.setOnClickListener {
            visibleFragmentListener?.onFilterClick()
            catAnalyticsInstance.eventFilterClicked(departmentId)
        }

        button_filter.setOnClickListener {
            visibleFragmentListener?.onFilterClick()
            catAnalyticsInstance.eventFilterClicked(departmentId)
        }


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
        fetchBundle()
        initToolbar()
        initViewPager()
        loadSection()
        initSwitchButton()
        initBottomSheetListener()

        bottomSheetFilterView?.initFilterBottomSheet()

    }

    private fun initBottomSheetListener() {
        bottomSheetFilterView?.setCallback(object : BottomSheetFilterView.Callback {
            override fun onApplyFilter(filterParameter: Map<String, String>) {
                applyFilter(filterParameter)
            }

            override fun onShow() {
                hideBottomNavigation()
            }

            override fun onHide() {
                showBottomNavigation()
                //sendBottomSheetHideEventForProductList()
            }

            override fun isSearchShown(): Boolean {
                return false
            }

            override fun hideKeyboard() {
                KeyboardHandler.hideSoftKeyboard(this@CategoryNavActivity)
            }

            override fun getActivity(): AppCompatActivity {
                return this@CategoryNavActivity
            }
        })
    }

    private fun applyFilter(filterParameter: Map<String, String>) {
        val selectedFragment = searchSectionPagerAdapter?.getItem(pager.currentItem) as BaseCategorySectionFragment

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
        if (bundle?.containsKey(EXTRA_CATEGORY_DEPARTMENT_ID) != null) run {
            departmentId = bundle.getString(EXTRA_CATEGORY_DEPARTMENT_ID, "")
            departmentName = bundle.getString(EXTRA_CATEGORY_DEPARTMENT_NAME, "")
        }
    }

    private fun loadSection() {
        populateTab(searchSectionItemList)

        searchSectionPagerAdapter = SearchSectionPagerAdapter(supportFragmentManager)
        searchSectionPagerAdapter!!.setData(searchSectionItemList)
        pager.adapter = searchSectionPagerAdapter
        tabs.setupWithViewPager(pager)

        setActiveTab()
    }

    private fun populateTab(searchSectionItemList: ArrayList<SearchSectionItem>) {
        initFragments()
        addFragmentsToList(searchSectionItemList)

    }

    private fun addFragmentsToList(searchSectionItemList: ArrayList<SearchSectionItem>) {
        searchSectionItemList.add(SearchSectionItem("Produk", ProductNavFragment.newInstance(departmentId, departmentName)))
        searchSectionItemList.add(SearchSectionItem("Katalog", CatalogNavFragment.newInstance(departmentId, departmentName)))
    }

    private fun initFragments() {
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
                bottomSheetFilterView?.closeView()
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
    }

    private fun initToolbar() {
        action_up_btn.setOnClickListener {
            catAnalyticsInstance.eventBackButtonClicked(departmentId)
            onBackPressed()
        }
        et_search.text = departmentName

        layout_search.setOnClickListener {
            catAnalyticsInstance.eventSearchBarClicked(departmentId)
            moveToAutoCompleteActivity()
        }
    }

    private fun moveToAutoCompleteActivity() {
        RouteManager.route(this, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE + "?q=" + departmentName)
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
        handleDefaultActivityResult(requestCode, resultCode, data)
    }

    private fun handleDefaultActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        bottomSheetFilterView?.onActivityResult(requestCode, resultCode, data)
    }

    fun getCategoryId(): String {
        return departmentId
    }

}
