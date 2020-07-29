package com.tokopedia.categorylevels.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.categorylevels.R
import com.tokopedia.categorylevels.adapters.CatalogNavListAdapter
import com.tokopedia.categorylevels.analytics.CategoryPageAnalytics
import com.tokopedia.categorylevels.di.CategoryNavComponent
import com.tokopedia.categorylevels.di.DaggerCategoryNavComponent
import com.tokopedia.categorylevels.viewmodel.CatalogNavViewModel
import com.tokopedia.common_category.adapter.BaseCategoryAdapter
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.factory.catalog.CatalogTypeFactory
import com.tokopedia.common_category.factory.catalog.CatalogTypeFactoryImpl
import com.tokopedia.common_category.fragment.BaseBannedProductFragment
import com.tokopedia.common_category.interfaces.CatalogCardListener
import com.tokopedia.common_category.model.bannedCategory.BannedData
import com.tokopedia.common_category.model.filter.DAFilterQueryType
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
        private const val EXTRA_BANNED_DATA = "CATEGORY_DATA"

        @JvmStatic
        fun newInstance(departmentId: String, departmentName: String, data: BannedData?): Fragment {
            val fragment = CatalogNavFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_ID, departmentId)
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_NAME, departmentName)
            bundle.putParcelable(EXTRA_BANNED_DATA, data)
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
                bannedData = it.getParcelable(EXTRA_BANNED_DATA) ?: BannedData()
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
                    txt_catalog_count.text = activity?.getString(R.string.categoryLevel_nav_catalog_count, it)
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
                setHeaderText(R.string.categoryLevel_nav_catalog_no_data_title)
                setDescriptionText(R.string.categoryLevel_nav_catalog_no_data_description)
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

    data class AceFilterInput(
            @field:SerializedName("pmin")
            var pmin: String,
            @field:SerializedName("pmax")
            var pmax: String,
            @field:SerializedName("sc")
            var sc: String)

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

    override fun topAdsTrackerUrlTrigger(url: String, id: String, name: String, imageURL: String) {
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

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout? {
        return view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)
    }
}
