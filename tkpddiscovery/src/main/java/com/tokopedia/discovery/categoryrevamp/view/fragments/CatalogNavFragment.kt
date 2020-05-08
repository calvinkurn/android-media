package com.tokopedia.discovery.categoryrevamp.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.common_category.adapter.BaseCategoryAdapter
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.fragment.BaseBannedProductFragment
import com.tokopedia.common_category.model.filter.DAFilterQueryType
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.CatalogNavListAdapter
import com.tokopedia.discovery.categoryrevamp.analytics.CategoryPageAnalytics
import com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog.CatalogTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog.CatalogTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CatalogCardListener
import com.tokopedia.discovery.categoryrevamp.viewmodel.CatalogNavViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_category_nav.*
import javax.inject.Inject

private const val REQUEST_ACTIVITY_SORT_PRODUCT = 102
private const val REQUEST_ACTIVITY_FILTER_PRODUCT = 103
private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"
private const val REQUEST_CODE_GOTO_CATALOG_DETAIL = 124

class CatalogNavFragment : BaseBannedProductFragment(),
        BaseCategoryAdapter.OnItemChangeView,
        CatalogCardListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var catalogNavViewModel: CatalogNavViewModel

    var list: ArrayList<Visitable<CatalogTypeFactory>> = ArrayList()

    private lateinit var catalogTypeFactory: CatalogTypeFactory

    var catalogNavListAdapter: CatalogNavListAdapter? = null

    private lateinit var categoryNavComponent: CategoryNavComponent

    private var staggeredGridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private var mDepartmentId: String = ""
    private var mDepartmentName: String = ""

    companion object {
        private const val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
        private const val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"

        @JvmStatic
        fun newInstance(departmentId: String, departmentName: String): Fragment {
            val fragment = CatalogNavFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_ID, departmentId)
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_NAME, departmentName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getAdapter(): BaseCategoryAdapter? {
        return catalogNavListAdapter
    }

    override fun getScreenName(): String {
        return "category page - " + getDepartMentId()
    }

    override fun initInjector() {
        categoryNavComponent = DaggerCategoryNavComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                        .baseAppComponent).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        categoryNavComponent.inject(this)
    }

    override fun getDataFromArguments() {
        arguments?.let {
            if (it.containsKey(EXTRA_CATEGORY_DEPARTMENT_ID)) {
                mDepartmentId = it.getString(EXTRA_CATEGORY_DEPARTMENT_ID, "")
                mDepartmentName = it.getString(EXTRA_CATEGORY_DEPARTMENT_NAME, "")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category_nav, container, false)
    }

    override fun addFragmentView() {
        view?.findViewById<View>(R.id.layout_banned_screen)?.hide()
    }

    override fun hideFragmentView() {
        view?.findViewById<View>(R.id.swipe_refresh_layout)?.hide()
    }

    override fun initFragmentView() {
        initView()
        setUpAdapter()
        observeData()
        setUpNavigation()
    }

    private fun observeData() {
        catalogNavViewModel.mCatalog.observe(viewLifecycleOwner, Observer {
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

        catalogNavViewModel.mCatalogCount.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.toInt() > 0) {
                    txt_catalog_count.text = activity?.getString(R.string.category_nav_catalog_count, it)
                } else {
                    txt_catalog_count.text = ""
                }
                setTotalSearchResultCount(it)
            }
        })

        catalogNavViewModel.mDynamicFilterModel.observe(viewLifecycleOwner, Observer {
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
            layout_no_data.run {
                show()
                setHeaderText(R.string.category_nav_catalog_no_data_title)
                setDescriptionText(R.string.category_nav_catalog_no_data_description)
            }
            txt_catalog_count.hide()
        } else {
            layout_no_data.hide()
            txt_catalog_count.show()
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
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        catalogNavViewModel = viewModelProvider.get(CatalogNavViewModel::class.java)
        fetchCatalogData(getCatalogListParams(0))
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
            pmin = hashmap["pmin"] ?: ""
        }
        if (hashmap.containsKey("pmax")) {
            pmax = hashmap["pmax"] ?: ""
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


    override fun onListItemImpressionEvent(viewedProductList: List<Visitable<Any>>, viewedTopAdsList: List<Visitable<Any>>) {
    }

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {
        CategoryPageAnalytics.catAnalyticsInstance.eventSortApplied(getDepartMentId(),
                selectedSortName, sortValue)
    }

    override fun wishListEnabledTracker(wishListTrackerUrl: String) {
    }

    override fun onShareButtonClicked() {
    }

    override fun topAdsTrackerUrlTrigger(url: String) {
    }

    override fun onDestroyView() {
        catalog_recyclerview?.let {
            it.layoutManager = null
            it.adapter = null
        }
        catalogNavListAdapter = null
        super.onDestroyView()
    }

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

    override fun addBannedProductScreen() {
        super.addBannedProductScreen()
        view?.findViewById<View>(R.id.layout_banned_screen)?.show()
    }
}
