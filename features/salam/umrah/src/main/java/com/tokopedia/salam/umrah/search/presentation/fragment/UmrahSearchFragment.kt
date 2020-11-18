package com.tokopedia.salam.umrah.search.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.DefaultOption
import com.tokopedia.salam.umrah.common.data.UmrahOption
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.common.util.UmrahSpaceItemDecoration
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.search.data.model.ParamFilter
import com.tokopedia.salam.umrah.search.di.UmrahSearchComponent
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_CATEGORY_SLUG_NAME
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DEPARTURE_CITY_ID
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DEPARTURE_PERIOD
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DURATION_DAYS_MAX
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_DURATION_DAYS_MIN
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_PRICE_MAX
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_PRICE_MIN
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.EXTRA_SORT
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.REQUEST_FILTER
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity.Companion.REQUEST_PDP
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchFilterActivity
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchAdapter
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchAdapterTypeFactory
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchSortAdapter
import com.tokopedia.salam.umrah.search.presentation.adapter.viewholder.UmrahSearchEmptyViewHolder
import com.tokopedia.salam.umrah.search.presentation.viewmodel.UmrahSearchFilterSortViewModel
import com.tokopedia.salam.umrah.search.presentation.viewmodel.UmrahSearchViewModel
import com.tokopedia.salam.umrah.search.util.SearchOrCategory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheets_umrah_search_sort.view.*
import kotlinx.android.synthetic.main.fragment_umrah_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 18/10/2019
 */
class UmrahSearchFragment : BaseListFragment<Visitable<UmrahSearchAdapterTypeFactory>, UmrahSearchAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, UmrahSearchAdapter.OnClickListener, UmrahSearchActivity.OnBackListener,
        UmrahSearchEmptyViewHolder.OnClickListener {

    private val umrahSearchSortAdapter: UmrahSearchSortAdapter by lazy { UmrahSearchSortAdapter() }
    private var sort = DefaultOption()
    private lateinit var sortBottomSheets: BottomSheetUnify
    private lateinit var sortView: View
    private var searchOrCategory: SearchOrCategory = SearchOrCategory.SEARCH
    private val searchParam = UmrahSearchProductDataParam()
    private val selectedFilter = ParamFilter()
    private var isRVInited = false
    private var isPassingEmpty = false
    lateinit var performanceMonitoring: PerformanceMonitoring

    override fun onEmptyContentItemTextClicked() {}

    @Inject
    lateinit var umrahSearchViewModel: UmrahSearchViewModel

    @Inject
    lateinit var umrahSearchFilterSortViewModel: UmrahSearchFilterSortViewModel

    @Inject
    lateinit var umrahTrackingAnalytics: UmrahTrackingAnalytics

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahSearchComponent::class.java).inject(this)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_FILTER -> {
                    data?.let {
                        selectedFilter.apply {
                            departureCity = it.getStringExtra(EXTRA_DEPARTURE_CITY_ID)
                            departurePeriod = it.getStringExtra(EXTRA_DEPARTURE_PERIOD)
                            priceMinimum = it.getIntExtra(EXTRA_PRICE_MIN, 0)
                            priceMaximum = it.getIntExtra(EXTRA_PRICE_MAX, 0)
                            durationDaysMinimum = it.getIntExtra(EXTRA_DURATION_DAYS_MIN, 0)
                            durationDaysMaximum = it.getIntExtra(EXTRA_DURATION_DAYS_MAX, 0)
                        }
                        umrahSearchViewModel.setFilter(selectedFilter)
                        umrahTrackingAnalytics.umrahSearchNCategoryFilterClick(selectedFilter, searchOrCategory)
                        loadInitialData()
                        isFilter = true
                        setHideFAB()
                    }
                }
                REQUEST_PDP -> loadInitialData()
                REQUEST_CODE_LOGIN -> context?.let { checkChatSession() }

            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initializePerformance() {
        performanceMonitoring = PerformanceMonitoring.start(UMRAH_SEARCH_PAGE_PERFORMANCE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_umrah_search_message.bringToFront()
        fab_umrah_search_message.setOnClickListener {
            checkChatSession()
        }
    }

    private fun checkChatSession() {
        if (userSessionInterface.isLoggedIn) {
            context?.let {
                startChatUmrah(it)
            }
        } else {
            goToLoginPage()
        }
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        initializePerformance()
        setHideFAB()
    }

    private fun startChatUmrah(context: Context) {
        val intent = RouteManager.getIntent(context,
                ApplinkConst.TOPCHAT_ASKSELLER,
                resources.getString(R.string.umrah_shop_id), "",
                resources.getString(R.string.umrah_shop_source), resources.getString(R.string.umrah_shop_name), "")
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        if (!isFilter) {
            getSearchParamFromBundle()
            setSelectedFilterFromBundle()
        }
        searchOrCategory = umrahSearchViewModel.getSearchOrCategory()
        initSortBottomSheets()
    }

    private fun setSelectedFilterFromBundle() {
        searchParam.let {
            selectedFilter.apply {
                departureCity = it.departureCityId
                departurePeriod = it.departurePeriod
                durationDaysMinimum = it.durationDaysMin
                durationDaysMaximum = it.durationDaysMax
                priceMinimum = it.priceMin
                priceMaximum = it.priceMax
            }
        }
        umrahSearchViewModel.setFilter(selectedFilter)
    }

    private fun getSearchParamFromBundle() {
        arguments?.let {
            searchParam.apply {
                categorySlugName = it.getString(EXTRA_CATEGORY_SLUG_NAME, "")
                departureCityId = it.getString(EXTRA_DEPARTURE_CITY_ID, "")
                departurePeriod = it.getString(EXTRA_DEPARTURE_PERIOD, "")
                priceMin = it.getInt(EXTRA_PRICE_MIN, 0)
                priceMax = it.getInt(EXTRA_PRICE_MAX, 0)
                if (sortMethod == "") sortMethod = it.getString(EXTRA_SORT, "")
                durationDaysMin = it.getInt(EXTRA_DURATION_DAYS_MIN, 0)
                durationDaysMax = it.getInt(EXTRA_DURATION_DAYS_MAX, 0)
            }
        }
        umrahSearchViewModel.setSearchParam(searchParam)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHideFAB()
        umrahSearchViewModel.searchResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetResult(it.data)
                    fab_umrah_search_message.show()
                    performanceMonitoring.stopTrace()
                }
                is Fail -> {
                    performanceMonitoring.stopTrace()
                    NetworkErrorHelper.showEmptyState(context, view?.rootView, null, null, null, R.drawable.img_umrah_pdp_empty_state) {
                        loadInitialData()
                    }
                }

            }
        })
        umrah_search_bottom_action_view.setDefault()
        umrah_search_bottom_action_view.sortItem.title = context?.getString(
                com.tokopedia.common.travel.R.string.travel_title_sort_search) ?: "Urutkan"
        umrah_search_bottom_action_view.sortItem.listener = { openSortBottomSheets() }
        umrah_search_bottom_action_view.filterItem.listener = { openFilterFragment() }
    }

    private fun setHideFAB() {
        fab_umrah_search_message.hide()
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.umrah_search_swipe_refresh_layout

    override fun getRecyclerViewResourceId(): Int = R.id.umrah_search_recycler_view

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getAdapterTypeFactory(): UmrahSearchAdapterTypeFactory = UmrahSearchAdapterTypeFactory(this, this)

    override fun onItemClicked(product: Visitable<UmrahSearchAdapterTypeFactory>?) {}
    override fun onItemClicked(product: UmrahSearchProduct, position: Int) {
        umrahTrackingAnalytics.umrahSearchNCategoryProductClick(product, position, umrahSearchViewModel.getSortValue(), selectedFilter, searchOrCategory)
        startActivityForResult(context?.let { UmrahPdpActivity.createIntent(it, product.slugName) }, REQUEST_PDP)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<UmrahSearchAdapterTypeFactory>, UmrahSearchAdapterTypeFactory> {
        val adapter = super.createAdapterInstance()
        adapter.errorNetworkModel = ErrorNetworkModel().apply {
            iconDrawableRes = R.drawable.umrah_img_empty_search_png
        }
        return UmrahSearchAdapter(this, adapterTypeFactory)
    }

    override fun loadData(page: Int) {
        umrahSearchViewModel.searchUmrahProducts(page,
                UmrahQuery.UMRAH_SEARCH_PRODUCT_QUERY)
    }

    private fun onSuccessGetResult(data: List<UmrahSearchProduct>) {
        if (!isRVInited) umrah_search_recycler_view.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(searchParam.limit * 2)
            addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2), RecyclerView.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!isRVInited) {
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val dataList = (adapter as BaseListAdapter<*, *>).data
                        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
                        isRVInited = true
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == SCROLL_STATE_IDLE) {
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val dataList = (adapter as BaseListAdapter<*, *>).data
                        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
                    }
                }
            })
        }
        umrah_search_bottom_action_view.visible()

        if (isPassingEmpty && data.isNullOrEmpty()) {
            umrah_search_bottom_action_view.gone()
            val emptyModel = EmptyModel()
            emptyModel.content = REQUEST_ALL_EMPTY
            renderList(listOf(emptyModel as Visitable<UmrahSearchAdapterTypeFactory>))
        } else {
            renderList(data, data.size >= searchParam.limit)
        }

        if (isPassingEmpty && data.isNotEmpty()) {
            showEmptyState()
        }
    }

    private fun trackImpression(startIndex: Int, lastIndex: Int, data: MutableList<out Any>) {
        for (i in startIndex..lastIndex) {
            if (i < data.size) {
                if (data[i] is UmrahSearchProduct) {
                    val product = data[i] as UmrahSearchProduct
                    if (!product.isViewed) {
                        umrahTrackingAnalytics.umrahSearchNCategoryProductListImpression(product, getIndexScrolled(i), umrahSearchViewModel.getSortValue(), selectedFilter, searchOrCategory)
                        product.isViewed = true
                    }
                }
            }
        }
    }

    private fun openFilterFragment() {
        isPassingEmpty = false
        startActivityForResult(context?.let { UmrahSearchFilterActivity.createIntent(it) }, REQUEST_FILTER)
    }

    override fun showEmpty() {
        if (!isPassingEmpty) {
            isPassingEmpty = true
            umrahSearchViewModel.resetSearchParam()
            loadInitialData()
        }
    }

    override fun onEmptyButtonClicked() {
        if (!isFilter) activity?.onBackPressed()
        else openFilterFragment()
    }

    @SuppressLint("InflateParams")
    private fun initSortBottomSheets() {
        sortView = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_search_sort, null).also { sortView ->
            sortView.rv_umrah_search_sort.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = umrahSearchSortAdapter.also {
                    it.listener = object : UmrahSearchSortAdapter.OnSortMenuSelected {
                        override fun onSelect(option: UmrahOption) {
                            umrahTrackingAnalytics.umrahSearchNCategorySortClick(option.query, searchOrCategory)
                            umrahSearchViewModel.setSortValue(option.query)
                            arguments?.putString("EXTRA_SORT", option.query)
                            GlobalScope.launch(Dispatchers.Main) {
                                delay(250)
                                sortBottomSheets.dismiss()
                                loadInitialData()
                            }
                        }
                    }
                }
            }
        }
        sortBottomSheets = BottomSheetUnify().also {
            it.setChild(sortView)
            it.setTitle(getString(R.string.umrah_search_sort_by))
            it.setCloseClickListener { sortBottomSheets.dismiss() }
        }
        loadSortData()
        observeSortData()
    }

    private fun openSortBottomSheets() {
        umrahSearchSortAdapter.setSelectedOption(umrahSearchViewModel.getSortValue())
        sortBottomSheets.show(fragmentManager!!, "")
    }

    private fun loadSortData() {
        val searchQuery = UmrahQuery.UMRAH_HOMEPAGE_SEARCH_PARAM_QUERY
        umrahSearchFilterSortViewModel.getUmrahSearchParameter(searchQuery)
    }

    private fun observeSortData() {
        umrahSearchFilterSortViewModel.umrahSearchParameter.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetSort(it.data)
                is Fail -> Fail(it.throwable)
            }
        })
    }

    private fun onSuccessGetSort(data: UmrahSearchParameterEntity) {
        sort = data.umrahSearchParameter.sortMethods
        umrahSearchSortAdapter.addOptions(sort.options)
        umrahSearchViewModel.initSortValue(sort.options[sort.defaultOption].query)
    }

    companion object {
        var isFilter = false
        const val REQUEST_CODE_LOGIN = 400
        const val UMRAH_SEARCH_PAGE_PERFORMANCE = "sl_umrah_searchpage"
        const val REQUEST_ALL_EMPTY = "REQUEST_ALL_EMPTY"

        fun getInstance(categorySlugName: String?, departureCityId: String?, departurePeriod: String?,
                        priceMin: Int?, priceMax: Int?, durationMin: Int,
                        durationMax: Int, defaultSort: String) =
                UmrahSearchFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_CATEGORY_SLUG_NAME, categorySlugName)
                        putString(EXTRA_DEPARTURE_CITY_ID, departureCityId)
                        putString(EXTRA_DEPARTURE_PERIOD, departurePeriod)
                        if (priceMin != null) putInt(EXTRA_PRICE_MIN, priceMin)
                        if (priceMax != null) putInt(EXTRA_PRICE_MAX, priceMax)
                        putInt(EXTRA_DURATION_DAYS_MIN, durationMin)
                        putInt(EXTRA_DURATION_DAYS_MAX, durationMax)
                        putString(EXTRA_SORT, defaultSort)
                    }
                }
    }

    override fun onBackPressed() {
        if (!isDetached) {
            UmrahSearchFilterFragment.selectedFilter = ParamFilter()
            isFilter = false
            umrahTrackingAnalytics.umrahSearchNCategoryBackClick(searchOrCategory)
        }
    }

    private fun goToLoginPage() {
        if (activity != null) {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN)
        }
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    private fun showEmptyState() {
        if (adapter.data[0] is UmrahSearchProduct) {
            adapter.data.add(0, EmptyModel() as Visitable<UmrahSearchAdapterTypeFactory>)
            adapter.notifyItemChanged(0)
            umrah_search_bottom_action_view.gone()
        }
    }


    override fun umrahSearchEmptyOnClickListener() {
        openFilterFragment()
    }

    private fun getIndexScrolled(index: Int): Int {
        return if (isPassingEmpty) index - 1
        else index
    }
}