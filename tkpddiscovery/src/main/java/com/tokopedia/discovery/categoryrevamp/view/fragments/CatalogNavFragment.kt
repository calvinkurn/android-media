package com.tokopedia.discovery.categoryrevamp.view.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.BaseCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.CatalogNavListAdapter
import com.tokopedia.discovery.categoryrevamp.analytics.CategoryPageAnalytics
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.filter.DAFilterQueryType
import com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog.CatalogTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog.CatalogTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CatalogCardListener
import com.tokopedia.discovery.categoryrevamp.viewmodel.CatalogNavViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_category_nav.*
import kotlinx.android.synthetic.main.layout_nav_no_product.*
import javax.inject.Inject

class CatalogNavFragment : BaseCategorySectionFragment(),
        BaseCategoryAdapter.OnItemChangeView,
        CatalogCardListener {

    override fun getDepartMentId(): String {
        return mDepartmentId
    }

    override fun setOnCatalogClicked(catalogID: String, catalogName: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalDiscovery.CATALOG)
        intent.putExtra(EXTRA_CATALOG_ID, catalogID)
        intent.putExtra(EXTRA_CATEGORY_DEPARTMENT_ID, mDepartmentId)
        intent.putExtra(EXTRA_CATEGORY_DEPARTMENT_NAME, mDepartmentName)
        startActivityForResult(intent, REQUEST_CODE_GOTO_CATALOG_DETAIL)
    }

    override fun getFilterRequestCode(): Int {
        return REQUEST_ACTIVITY_FILTER_PRODUCT
    }

    override fun getSortRequestCode(): Int {
        return REQUEST_ACTIVITY_SORT_PRODUCT
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var catalogNavViewModel: CatalogNavViewModel


    var list: ArrayList<Visitable<CatalogTypeFactory>> = ArrayList()

    lateinit var catalogTypeFactory: CatalogTypeFactory

    var catalogNavListAdapter: CatalogNavListAdapter? = null

    lateinit var categoryNavComponent: CategoryNavComponent

    private var staggeredGridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private val REQUEST_ACTIVITY_SORT_PRODUCT = 102
    private val REQUEST_ACTIVITY_FILTER_PRODUCT = 103

    var mDepartmentId: String = ""
    var mDepartmentName: String = ""
    val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"
    private val REQUEST_CODE_GOTO_CATALOG_DETAIL = 124


    companion object {
        private val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
        private val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"

        @JvmStatic
        fun newInstance(departmentid: String, departmentName: String): Fragment {
            val fragment = CatalogNavFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_ID, departmentid)
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_NAME, departmentName)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun getAdapter(): BaseCategoryAdapter? {
        return catalogNavListAdapter
    }

    override fun getScreenName(): String {
        return "category page - " + getDepartMentId();
    }

    override fun initInjector() {
        categoryNavComponent = DaggerCategoryNavComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                        .baseAppComponent).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryNavComponent.inject(this)
        arguments?.let {
            if (it.containsKey(EXTRA_CATEGORY_DEPARTMENT_ID)) {
                mDepartmentId = it.getString(EXTRA_CATEGORY_DEPARTMENT_ID, "")
                mDepartmentName = it.getString(EXTRA_CATEGORY_DEPARTMENT_NAME, "")
            }
        }

        initView()
        setUpAdapter()
        observeData()
        setUpNavigation()
    }

    private fun observeData() {

        catalogNavViewModel.mCatalog.observe(this, Observer {
            when (it) {
                is Success -> {

                    if (catalogNavListAdapter?.isShimmerRunning() == true) {
                        catalogNavListAdapter?.removeShimmer()
                    }
                    catalogNavListAdapter?.removeLoading()
                    if (it.data.count > 0) {
                        showNoDataScreen(false)
                        list.addAll(it.data.items as ArrayList<Visitable<CatalogTypeFactory>>)
                        catalog_recyclerview.adapter?.notifyDataSetChanged()
                        staggeredGridLayoutLoadMoreTriggerListener?.updateStateAfterGetData()
                    } else {
                        showNoDataScreen(true)
                    }
                    hideRefreshLayout()
                    reloadFilter(createFilterParam())
                }

                is Fail -> {
                    showNoDataScreen(true)
                    catalogNavListAdapter?.removeLoading()
                    hideRefreshLayout()
                }
            }
        })

        catalogNavViewModel.mCatalogCount.observe(this@CatalogNavFragment, Observer {

            it?.let {
                if (it.toInt() > 0) {
                    txt_catalog_count.text = activity?.getString(R.string.category_nav_catalog_count, it)
                } else {
                    txt_catalog_count.text = ""
                }
                setTotalSearchResultCount(it)
            }
        })

        catalogNavViewModel.mDynamicFilterModel.observe(this@CatalogNavFragment, Observer {
            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }

                is Fail -> {
                }
            }
        })
    }

    private fun showNoDataScreen(toShow: Boolean) {
        if (toShow) {
            layout_no_data.visibility = View.VISIBLE
            txt_no_data_header.text = resources.getText(R.string.category_nav_catalog_no_data_title)
            txt_no_data_description.text = resources.getText(R.string.category_nav_catalog_no_data_description)
        } else {
            layout_no_data.visibility = View.GONE
        }

    }


    private fun createFilterParam(): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = mDepartmentId
        paramMap.putString(CategoryNavConstants.SOURCE, "search_catalog")
        paramMap.putObject(CategoryNavConstants.FILTER, daFilterQueryType)
        paramMap.putString(CategoryNavConstants.Q, "")
        paramMap.putString(CategoryNavConstants.SOURCE, "directory_catalog")
        return paramMap
    }

    private fun reloadFilter(param: RequestParams) {
        catalogNavViewModel.fetchDynamicAttribute(param)
    }

    private fun setUpAdapter() {
        catalogTypeFactory = CatalogTypeFactoryImpl(this)
        catalogNavListAdapter = CatalogNavListAdapter(catalogTypeFactory, list, this)
        catalog_recyclerview.adapter = catalogNavListAdapter
        catalog_recyclerview.layoutManager = getStaggeredGridLayoutManager()
        catalogNavListAdapter?.addShimmer()
        getStaggeredGridLayoutManager()?.let {
            staggeredGridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(it)
        }
        staggeredGridLayoutLoadMoreTriggerListener?.let {
            catalog_recyclerview.addOnScrollListener(it)
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                fetchCatalogData(getCatalogListParams(page))
                catalogNavListAdapter?.addLoading()
            }
        }
    }

    private fun initView() {
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            catalogNavViewModel = viewModelProvider.get(CatalogNavViewModel::class.java)
            fetchCatalogData(getCatalogListParams(0))
        }
    }

    private fun getCatalogListParams(page: Int): RequestParams {

        val catalogMap = RequestParams()
        catalogMap.putInt(CategoryNavConstants.START, page * 10)
        catalogMap.putString(CategoryNavConstants.QUERY, "")
        catalogMap.putString(CategoryNavConstants.SOURCE, "directory")
        catalogMap.putString(CategoryNavConstants.ST, "catalog")
        catalogMap.putInt(CategoryNavConstants.ROWS, 10)
        catalogMap.putInt("ob", getSelectedSort()["ob"]?.toInt() ?: 23)
        val hashmap = getSelectedFilter()
        var pmin = ""
        var pmax = ""
        if (hashmap.containsKey("pmin")) {
            pmin = hashmap.get("pmin") ?: ""
        }
        if (hashmap.containsKey("pmax")) {
            pmax = hashmap.get("pmax") ?: ""
        }

        catalogMap.putObject("filter", AceFilterInput(pmin, pmax, mDepartmentId))

        return catalogMap

    }

    override fun onChangeList() {
        catalog_recyclerview.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        catalog_recyclerview.requestLayout()
    }

    override fun onChangeSingleGrid() {
        catalog_recyclerview.requestLayout()
    }


    data class AceFilterInput(var pmin: String, var pmax: String, var sc: String)


    override fun onSwipeToRefresh() {
        reloadData()
    }

    override fun reloadData() {
        if (catalogNavListAdapter == null) {
            return
        }
        showRefreshLayout()
        catalogNavListAdapter?.clearData()
        catalogNavListAdapter?.addShimmer()
        staggeredGridLayoutLoadMoreTriggerListener?.resetState()
        fetchCatalogData(getCatalogListParams(0))

    }


    private fun fetchCatalogData(paramMap: RequestParams) {
        catalogNavViewModel.fetchCatalogDetail(paramMap)
    }


    override fun onDetach() {
        super.onDetach()
        catalogNavViewModel.onDetach()
    }

    override fun onListItemImpressionEvent(item: Visitable<Any>, position: Int) {

    }

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {
        CategoryPageAnalytics.catAnalyticsInstance.eventSortApplied(getDepartMentId(),
                selectedSortName, sortValue)
    }

    override fun wishListEnabledTracker(wishListTrackerUrl: String) {
    }
    override fun onShareButtonClicked() {
    }
}
