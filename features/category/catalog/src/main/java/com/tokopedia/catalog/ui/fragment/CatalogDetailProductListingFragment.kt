package com.tokopedia.catalog.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogProductNavListAdapter
import com.tokopedia.catalog.adapter.factory.CatalogTypeFactory
import com.tokopedia.catalog.adapter.factory.CatalogTypeFactoryImpl
import com.tokopedia.catalog.analytics.CatalogDetailAnalytics
import com.tokopedia.catalog.di.CatalogComponent
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.model.util.CatalogUtil
import com.tokopedia.catalog.viewmodel.CatalogDetailProductListingViewModel
import com.tokopedia.common_category.adapter.BaseCategoryAdapter
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.fragment.BaseCategorySectionFragment
import com.tokopedia.common_category.interfaces.QuickFilterListener
import com.tokopedia.common_category.model.filter.DAFilterQueryType
import com.tokopedia.common_category.util.ParamMapToUrl
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.android.synthetic.main.fragment_catalog_detail_product_listing.*
import javax.inject.Inject

class CatalogDetailProductListingFragment : BaseCategorySectionFragment(),
        BaseCategoryAdapter.OnItemChangeView,
        QuickFilterListener,
        CatalogProductCardListener,
        WishListActionListener,
        SortFilterBottomSheet.Callback{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: CatalogDetailProductListingViewModel

    @Inject
    lateinit var removeWishlistActionUseCase: RemoveWishListUseCase
    @Inject
    lateinit var addWishlistActionUseCase: AddWishListUseCase

    private lateinit var catalogComponent: CatalogComponent

    private var catalogId: String = ""
    private var catalogUrl: String = ""
    private var departmentId: String = ""

    var productNavListAdapter: CatalogProductNavListAdapter? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null

    private var pagingRowCount = 20

    private lateinit var catalogTypeFactory: CatalogTypeFactory

    private lateinit var userSession: UserSession
    private lateinit var gcmHandler: GCMHandler
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_CATALOG_URL = "ARG_EXTRA_CATALOG_URL"

        private const val REQUEST_ACTIVITY_SORT_PRODUCT = 102
        private const val REQUEST_ACTIVITY_FILTER_PRODUCT = 103

        @JvmStatic
        fun newInstance(catalogId: String, catalogUrl : String?): BaseCategorySectionFragment {
            val fragment = CatalogDetailProductListingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            bundle.putString(ARG_EXTRA_CATALOG_URL, catalogUrl)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_detail_product_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catalogComponent.inject(this)
        arguments?.let {
            if (it.containsKey(ARG_EXTRA_CATALOG_ID)) {
                catalogId = it.getString(ARG_EXTRA_CATALOG_ID, "")
                catalogUrl = it.getString(ARG_EXTRA_CATALOG_URL, "")
            }
        }
        initView()
        observeData()
        setUpAdapter()
        setupRecyclerView()
        setUpNavigation()
        setUpVisibleFragmentListener()
        initSearchQuickSortFilter(view)
        sortFilterBottomSheet = SortFilterBottomSheet()
    }

    private fun initView() {
        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)

        activity?.let { observer ->
            val viewModelProvider = ViewModelProvider(observer, viewModelFactory)
            viewModel = viewModelProvider.get(CatalogDetailProductListingViewModel::class.java)
            fetchProductData(getProductListParams(getPage()))
            viewModel.fetchQuickFilters(getQuickFilterParams())
            viewModel.catalogUrl = catalogUrl
        }
    }

    private fun setUpAdapter() {
        catalogTypeFactory = CatalogTypeFactoryImpl(this)
        productNavListAdapter = CatalogProductNavListAdapter(catalogTypeFactory, viewModel.list, this,this)
        productNavListAdapter?.changeListView()
        if(viewModel.list.size == 0)
            productNavListAdapter?.addShimmer()

        attachScrollListener()
    }

    private fun setupRecyclerView() {
        product_recyclerview.apply {
            layoutManager = getLinearLayoutManager()
            setHasFixedSize(true)
            adapter = productNavListAdapter
        }
    }

    private fun attachScrollListener() {
        getLinearLayoutManager()?.let {
            loadMoreTriggerListener = getEndlessRecyclerViewListener(it)
        }
        loadMoreTriggerListener?.let {
            product_recyclerview.addOnScrollListener(it)
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                fetchProductData(getProductListParams(viewModel.pageCount))
                productNavListAdapter?.addLoading()
            }
        }
    }

    private fun observeData() {

        viewModel.sortFilterItems.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                setQuickFilter(it)
            }
        })

        viewModel.selectedSortIndicatorCount.observe(viewLifecycleOwner, Observer {
            searchSortFilter?.indicatorCounter = it
        })

        viewModel.dynamicFilterModel.observe(viewLifecycleOwner, Observer {
            it?.let { dm ->
                setDynamicFilter(dm)
            }
        })

        viewModel.mProductList.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    if (productNavListAdapter?.isShimmerRunning() == true) {
                        productNavListAdapter?.removeShimmer()
                    }

                    if (it.data.isNotEmpty()) {
                        showNoDataScreen(false)
                        productNavListAdapter?.removeLoading()
                        product_recyclerview.adapter?.notifyDataSetChanged()
                        loadMoreTriggerListener?.updateStateAfterGetData()
                        viewModel.isPagingAllowed = true
                    } else {
                        productNavListAdapter?.removeLoading()
                        if (viewModel.list.isEmpty()) {
                            showNoDataScreen(true)
                        }
                    }
                    sortFilterBottomSheet = SortFilterBottomSheet()
                    hideRefreshLayout()
                    reloadFilter(getDynamicFilterParams())
                }

                is Fail -> {
                    productNavListAdapter?.removeLoading()
                    hideRefreshLayout()
                    if (viewModel.list.isEmpty()) {
                        showNoDataScreen(true)
                    }
                    viewModel.isPagingAllowed = true
                }

            }
        })

        viewModel.mProductCount.observe(viewLifecycleOwner, Observer {
            it?.let {
                setHeaderCount(it)
            }
        })

        viewModel.getDynamicFilterData().observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    viewModel.dynamicFilterModel.value = it.data
                }

                is Fail -> {
                }
            }
        })

        viewModel.mQuickFilterModel.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    startFilter(it.data.data)
                }

                is Fail -> {
                    search_product_quick_sort_filter.hide()
                }
            }

        })

    }

    private fun setHeaderCount(totalProductsCount : String){
        if(CatalogConstant.ZERO_VALUE == totalProductsCount) {
            headerTitle.text = getString(R.string.catalog_search_product_zero_count_text)
            showNoDataScreen(true)
        }
        else
            headerTitle.text = getString(R.string.catalog_search_product_count_text,totalProductsCount)
    }

    private fun reloadFilter(param: RequestParams) {
        viewModel.fetchDynamicAttribute(param)
    }

    private fun showNoDataScreen(toShow: Boolean) {
        if (toShow) {
            layout_no_data.show()
            product_recyclerview.hide()
        } else {
            layout_no_data.hide()
            product_recyclerview.show()
        }
    }

    override fun getAdapter(): BaseCategoryAdapter? {
        return productNavListAdapter
    }

    override fun reloadData() {
        if (productNavListAdapter == null) {
            return
        }
        showRefreshLayout()
        productNavListAdapter?.clearData()
        viewModel.list.clear()
        if(viewModel.list.size == 0)
            productNavListAdapter?.addShimmer()
        layout_no_data?.hide()
        resetPage()
        loadMoreTriggerListener?.resetState()
        viewModel.searchParametersMap.value = searchParameter.getSearchParameterHashMap()
        fetchProductData(getProductListParams(getPage()))
        viewModel.fetchQuickFilters(getQuickFilterParams())
        setSortFilterIndicatorCounter()
    }

    override fun getDepartMentId(): String {
        return departmentId
    }

    override fun getFilterRequestCode(): Int {
        return REQUEST_ACTIVITY_FILTER_PRODUCT
    }

    override fun getSortRequestCode(): Int {
        return REQUEST_ACTIVITY_SORT_PRODUCT
    }

    override fun getScreenName(): String {
        return "category page - " + getDepartMentId();
    }

    override fun initInjector() {
        catalogComponent = DaggerCatalogComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                        .baseAppComponent).build()
    }

    private fun resetPage() {
        viewModel.isPagingAllowed = true
        viewModel.pageCount = 0
    }

    private fun fetchProductData(paramMap: RequestParams) {
        viewModel.fetchProductListing(paramMap)
    }

    private fun getQuickFilterParams(): RequestParams {
        val param = RequestParams.create()
        val searchFilterParams = RequestParams.create()
        searchFilterParams.apply {
            putString(CategoryNavConstants.DEVICE, CatalogConstant.DEVICE)
            putString(CategoryNavConstants.Q,"")
            putString(CategoryNavConstants.SOURCE, CatalogConstant.QUICK_FILTER_SOURCE)
        }
        param.putString(CatalogConstant.QUICK_FILTER_PARAMS, createParametersForQuery(searchFilterParams.parameters))
        return param
    }

    private fun getDynamicFilterParams(): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = departmentId
        paramMap.apply {
            putString(CategoryNavConstants.DEVICE, CatalogConstant.DEVICE)
            putString(CategoryNavConstants.SOURCE, CatalogConstant.SEARCH_PRODUCT_SOURCE)
            putObject(CategoryNavConstants.FILTER, daFilterQueryType)
            putString(CategoryNavConstants.Q, "")
        }
        return paramMap
    }

    private fun getProductListParams(start: Int): RequestParams {
        val param = RequestParams.create()
        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.apply {
            putString(CategoryNavConstants.START, (start * pagingRowCount).toString())
            putString(CategoryNavConstants.DEVICE, CatalogConstant.DEVICE)
            putString(CategoryNavConstants.UNIQUE_ID, getUniqueId())
            putString(CategoryNavConstants.ROWS, pagingRowCount.toString())
            putString(CategoryNavConstants.SOURCE, CatalogConstant.SOURCE)
            putString(CategoryNavConstants.CTG_ID, catalogId)
            viewModel.searchParametersMap.value?.let { safeSearchParams ->
                putAllString(safeSearchParams)
            }
        }
        param.putString(CatalogConstant.PRODUCT_PARAMS, createParametersForQuery(searchProductRequestParams.parameters))
        return param
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthHelper.getMD5Hash(userSession.userId)
        else
            AuthHelper.getMD5Hash(gcmHandler.registrationId)
    }

    private fun getPage(): Int {
        return viewModel.pageCount
    }

    private fun getProductIntent(productId: String, warehouseId: String): Intent? {
        if (context == null) {
            return null
        }

        return if (!TextUtils.isEmpty(warehouseId)) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID, productId, warehouseId)
        } else {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        }
    }

    override fun onItemClicked(item: CatalogProductItem, adapterPosition: Int) {
        val intent = getProductIntent(item.id, "")

        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
            startActivityForResult(intent, 1002)
            CatalogDetailAnalytics.trackProductCardClick(catalogId,viewModel.catalogUrl,userSession.userId,
                    item,(adapterPosition + 1).toString(),viewModel.searchParametersMap.value)
        }
    }

    override fun onProductImpressed(item: CatalogProductItem, adapterPosition: Int) {
        CatalogDetailAnalytics.trackEventImpressionProductCard(catalogId,viewModel.catalogUrl,userSession.userId,
                item,(adapterPosition + 1).toString(),viewModel.searchParametersMap.value)
    }

    override fun onLongClick(item: CatalogProductItem, adapterPosition: Int) {

    }

    /*********************************   WishList  ******************************/

    override fun onWishlistButtonClicked(productItem: CatalogProductItem, position: Int) {
        if (userSession.isLoggedIn) {
            disableWishListButton(productItem.id)
            if (productItem.wishlist) {
                removeWishList(productItem.id, userSession.userId, position)
            } else {
                addWishList(productItem.id, userSession.userId, position)
            }
        } else {
            launchLoginActivity(productItem.id)
        }
    }

    override fun onThreeDotsClicked(productItem: CatalogProductItem, position: Int) {
        onWishlistButtonClicked(productItem,position)
    }

    private fun enableWishListButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId.toInt(), true)
    }

    private fun disableWishListButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId.toInt(), false)
    }

    private fun removeWishList(productId: String, userId: String, adapterPosition: Int) {
        CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                CatalogDetailAnalytics.ActionKeys.CLICK_THREE_DOTS,
                "$catalogId - ${CatalogDetailAnalytics.ActionKeys.ACTION_REMOVE_WISHLIST}",userSession.userId)
        removeWishlistActionUseCase.createObservable(productId,
                userId, this)
    }

    private fun addWishList(productId: String, userId: String, adapterPosition: Int) {
        CatalogDetailAnalytics.sendEvent(
                CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                CatalogDetailAnalytics.ActionKeys.CLICK_THREE_DOTS,
                "$catalogId - ${CatalogDetailAnalytics.ActionKeys.ACTION_ADD_WISHLIST}",userSession.userId)
        addWishlistActionUseCase.createObservable(productId, userId,
                this)
    }

    private fun launchLoginActivity(productId: String) {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    override fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            val filter = getSelectedFilter()
            filter[option.key] = option.value
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
            CatalogDetailAnalytics.sendEvent(
                    CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                    CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                    CatalogDetailAnalytics.ActionKeys.CLICK_QUICK_FILTER,
                    "$catalogId - ${CatalogUtil.getSortFilterAnalytics(viewModel.searchParametersMap.value)}",
                    userSession.userId)
        } else {
            val filter = getSelectedFilter()
            filter.remove(option.key)
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
        }
    }

    override fun isQuickFilterSelected(option: Option): Boolean {
        return getSelectedFilter().containsKey(option.key)
    }

    override fun onChangeList() {
        product_recyclerview.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        product_recyclerview.requestLayout()
    }

    override fun onChangeSingleGrid() {
        product_recyclerview.requestLayout()
    }

    override fun onErrorAddWishList(errorMessage: String?, productId: String) {
        enableWishListButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessAddWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), true)
        enableWishListButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist))
    }

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String) {
        enableWishListButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), false)
        enableWishListButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist))
    }

    override fun onDetach() {
        super.onDetach()
        sortFilterBottomSheet = null
        viewModel.onDetach()
    }

    override fun onListItemImpressionEvent(viewedProductList: List<Visitable<Any>>, viewedTopAdsList: List<Visitable<Any>>) {

    }

    override fun onPause() {
        super.onPause()
        productNavListAdapter?.onPause()
    }

    override fun hasThreeDots() = true

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {

    }

    override fun wishListEnabledTracker(wishListTrackerUrl: String) {
    }

    override fun onShareButtonClicked() {
    }

    override fun topAdsTrackerUrlTrigger(url: String, id: String, name: String, imageURL: String) {
    }

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout? {
        return view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)
    }

    /*********************************   Sort Filter ******************************/

    private var searchSortFilter: SortFilter? = null

    private fun initSearchQuickSortFilter(rootView: View) {
        searchSortFilter = rootView.findViewById(R.id.search_product_quick_sort_filter)
        if(viewModel.searchParametersMap.value == null) {
            viewModel.searchParametersMap.value = searchParameter.getSearchParameterHashMap()
            addDefaultSelectedSort()
        }
    }

    private fun startFilter(quickFilterData : DataValue){
        processQuickFilter(quickFilterData)
    }

    private fun processQuickFilter(quickFilterData: DataValue) {
        if (viewModel.dynamicFilterModel == null) initFilterControllerForQuickFilter(quickFilterData.filter)
        val sortFilterItems = arrayListOf<SortFilterItem>()
        viewModel.quickFilterOptionList = arrayListOf()
        for (filter in quickFilterData.filter) {
            val options = filter.options
            (viewModel.quickFilterOptionList as java.util.ArrayList<Option>).addAll(options)
            convertToSortFilterItem(filter.title, options)?.let {
                sortFilterItems.addAll(it)
                viewModel.sortFilterItems.value = sortFilterItems
            }
        }
    }

    private fun initFilterControllerForQuickFilter(quickFilterList : List<Filter>) {
        filterController?.initFilterController(searchParameter.getSearchParameterHashMap(), quickFilterList)
    }

    private fun setSortFilterIndicatorCounter() {
        viewModel.selectedSortIndicatorCount.value = CatalogUtil.getSortFilterCount(searchParameter.getSearchParameterMap())
    }

    private fun addDefaultSelectedSort() {
        if (searchParameter.get(SearchApiConst.OB).isEmpty()) {
            searchParameter.set(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        }
        viewModel.searchParametersMap.value = searchParameter.getSearchParameterHashMap()
    }

    private fun setQuickFilter(items : List<SortFilterItem> ) {
        searchSortFilter?.apply {
            sortFilterItems.removeAllViews()
            visibility = View.VISIBLE
            sortFilterHorizontalScrollView.scrollX = 0
            addItem(items as ArrayList<SortFilterItem>)
            textView?.text = getString(R.string.catalog_filter_text)
        }
        searchSortFilter?.parentListener = { this.openBottomSheetFilterRevamp() }
        setSortFilterNewNotification(items)
    }

    private fun setSortFilterNewNotification(items: List<SortFilterItem>) {
        val quickFilterOptionList: List<Option> = viewModel.quickFilterOptionList
        for (i in items.indices) {
            if (i >= quickFilterOptionList.size) break
            val item = items[i]
            val quickFilterOption = quickFilterOptionList[i]
            sortFilterItemShowNew(item, quickFilterOption.isNew)
        }
    }

    private fun sortFilterItemShowNew(item: SortFilterItem, isNew: Boolean) {
        item.refChipUnify?.showNewNotification = isNew
    }


    private fun convertToSortFilterItem(title: String, options: List<Option>): List<SortFilterItem>? {
        val list: MutableList<SortFilterItem> = java.util.ArrayList()
        for (option in options) {
            list.add(createSortFilterItem(title, option))
        }
        return list
    }

    private fun createSortFilterItem(title: String, option: Option): SortFilterItem {
        val item = SortFilterItem(title) {}
        setSortFilterItemListener(item, option)
        setSortFilterItemState(item, option)
        return item
    }

    private fun setSortFilterItemListener(item: SortFilterItem, option: Option) {
        item.listener = {
            onQuickFilterSelected(option)
        }
    }

    private fun setSortFilterItemState(item: SortFilterItem, option: Option) {
        if (isQuickFilterSelected(option)) {
            item.type = ChipsUnify.TYPE_SELECTED
            item.typeUpdated = false
        }
    }

    /*******************************  Bottom Sheet Filter **************************/

    private fun openBottomSheetFilterRevamp(){
        activity?.supportFragmentManager?.let{
            CatalogDetailAnalytics.sendEvent(
            CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
            CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
            CatalogDetailAnalytics.ActionKeys.CLICK_DYNAMIC_FILTER,
                    "$catalogId - ${CatalogUtil.getSortFilterAnalytics(viewModel.searchParametersMap.value)}",
                    userSession.userId)
            sortFilterBottomSheet?.show(
                    it,
                    viewModel.searchParametersMap.value,
                    viewModel.dynamicFilterModel.value,
                    this
            )
        }
    }

    private fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel){
        filterController?.appendFilterList(searchParameter.getSearchParameterHashMap(), dynamicFilterModel.data.filter)
        sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)
        renderDynamicFilter(dynamicFilterModel.data)
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        filterController?.refreshMapParameter(applySortFilterModel.mapParameter)
        searchParameter.getSearchParameterHashMap().clear()
        searchParameter.getSearchParameterHashMap().putAll(applySortFilterModel.mapParameter)
        viewModel.searchParametersMap.value = searchParameter.getSearchParameterHashMap()
        reloadData()
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        sortFilterBottomSheet?.setResultCountText(getString(R.string.catalog_apply_filter))
    }
}
