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
import com.tokopedia.catalog.analytics.CatalogDetailPageAnalytics
import com.tokopedia.catalog.di.CatalogComponent
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.viewmodel.CatalogDetailProductListingViewModel
import com.tokopedia.common_category.adapter.BaseCategoryAdapter
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.factory.ProductTypeFactory
import com.tokopedia.common_category.factory.catalog.CatalogTypeFactoryImpl
import com.tokopedia.common_category.fragment.BaseCategorySectionFragment
import com.tokopedia.common_category.interfaces.ProductCardListener
import com.tokopedia.common_category.interfaces.QuickFilterListener
import com.tokopedia.common_category.model.filter.DAFilterQueryType
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.util.ParamMapToUrl
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.android.synthetic.main.fragment_catalog_detail_product_listing.*
import javax.inject.Inject

class CatalogDetailProductListingFragment : BaseCategorySectionFragment(),
        BaseCategoryAdapter.OnItemChangeView,
        QuickFilterListener,
        ProductCardListener,
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
    private var catalogName: String = ""
    private var departmentId: String = ""
    private var departmentName: String = ""

    var productNavListAdapter: CatalogProductNavListAdapter? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null

    private var pageCount = 0
    private var isPagingAllowed: Boolean = true
    private var pagingRowCount = 20

    var list: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()
    private var dynamicFilterModel : DynamicFilterModel? = null

    private lateinit var productTypeFactory: ProductTypeFactory

    private lateinit var userSession: UserSession
    private lateinit var gcmHandler: GCMHandler
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_CATALOG_NAME = "ARG_EXTRA_CATALOG_NAME"
        private const val ARG_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
        private const val ARG_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"

        private val REQUEST_ACTIVITY_SORT_PRODUCT = 102
        private val REQUEST_ACTIVITY_FILTER_PRODUCT = 103

        @JvmStatic
        fun newInstance(catalogId: String, catalogName: String, departmentid: String?, departmentName: String?): BaseCategorySectionFragment {
            val fragment = CatalogDetailProductListingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            bundle.putString(ARG_EXTRA_CATALOG_NAME, catalogName)
            bundle.putString(ARG_CATEGORY_DEPARTMENT_ID, departmentid)
            bundle.putString(ARG_CATEGORY_DEPARTMENT_NAME, departmentName)
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
                catalogName = it.getString(ARG_EXTRA_CATALOG_NAME, "")
                departmentId = it.getString(ARG_CATEGORY_DEPARTMENT_ID, "")
                departmentName = it.getString(ARG_CATEGORY_DEPARTMENT_NAME, "")
            }
        }
        initView()
        observeData()
        setUpAdapter()
        setUpNavigation()
        setUpVisibleFragmentListener()
        initSearchQuickSortFilter(view)
        addDefaultSelectedSort()
    }

    private fun initView() {
        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)

        activity?.let { observer ->
            val viewModelProvider = ViewModelProvider(observer, viewModelFactory)
            viewModel = viewModelProvider.get(CatalogDetailProductListingViewModel::class.java)
            fetchProductData(getProductListParams(getPage()))
            viewModel.fetchQuickFilters(getQuickFilterParams())
        }
    }

    private fun setUpAdapter() {
        productTypeFactory = CatalogTypeFactoryImpl(this)
        productNavListAdapter = CatalogProductNavListAdapter(productTypeFactory, list, this)
        productNavListAdapter?.changeListView()
        product_recyclerview.adapter = productNavListAdapter
        product_recyclerview.layoutManager = getLinearLayoutManager()
        productNavListAdapter?.addShimmer()

        attachScrollListener()
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
                fetchProductData(getProductListParams(page))
                productNavListAdapter?.addLoading()
            }
        }
    }

    private fun observeData() {
        viewModel.mProductList.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    if (productNavListAdapter?.isShimmerRunning() == true) {
                        productNavListAdapter?.removeShimmer()
                    }

                    if (it.data.isNotEmpty()) {
                        showNoDataScreen(false)
                        list.addAll(it.data as ArrayList<Visitable<ProductTypeFactory>>)
                        productNavListAdapter?.removeLoading()
                        product_recyclerview.adapter?.notifyDataSetChanged()
                        setHeaderCount(list.size)
                        loadMoreTriggerListener?.updateStateAfterGetData()
                        isPagingAllowed = true
                    } else {
                        if (list.isEmpty()) {
                            showNoDataScreen(true)
                        }
                    }
                    hideRefreshLayout()
                    reloadFilter(getDynamicFilterParams())
                }

                is Fail -> {
                    productNavListAdapter?.removeLoading()
                    hideRefreshLayout()
                    if (list.isEmpty()) {
                        showNoDataScreen(true)
                    }
                    isPagingAllowed = true
                }

            }
        })

        viewModel.mProductCount.observe(viewLifecycleOwner, Observer {
            it?.let {
                setTotalSearchResultCount(it)
            }
        })

        viewModel.getDynamicFilterData().observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                    dynamicFilterModel = it.data
                    setDynamicFilter(it.data)
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

    private fun setHeaderCount(totalProductsCount : Int){
        headerTitle.text = getString(R.string.catalog_search_product_count_text,totalProductsCount.toString())
    }

    private fun reloadFilter(param: RequestParams) {
        viewModel.fetchDynamicAttribute(param)
    }

    private fun showNoDataScreen(toShow: Boolean) {
        if (toShow) {
            layout_no_data.show()
        } else {
            layout_no_data.visibility = View.GONE
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
        productNavListAdapter?.addShimmer()
        resetPage()
        loadMoreTriggerListener?.resetState()
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
        isPagingAllowed = true
        pageCount = 0
    }

    private fun fetchProductData(paramMap: RequestParams) {
        viewModel.fetchProductListing(paramMap)
    }

    private fun getQuickFilterParams(): RequestParams {
        val param = RequestParams.create()
        val searchFilterParams = RequestParams.create()
        searchFilterParams.apply {
            putString(CategoryNavConstants.DEVICE, "android")
            putString(CategoryNavConstants.Q,"")
            putString(CategoryNavConstants.SOURCE, "quick_filter")
        }
        param.putString(CatalogConstant.QUICK_FILTER_PARAMS, createParametersForQuery(searchFilterParams.parameters))
        return param
    }

    private fun getDynamicFilterParams(): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = departmentId
        paramMap.apply {
            putString(CategoryNavConstants.DEVICE, "android")
            putString(CategoryNavConstants.SOURCE, "search_product")
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
            putString(CategoryNavConstants.SC, departmentId)
            putString(CategoryNavConstants.DEVICE, "android")
            putString(CategoryNavConstants.UNIQUE_ID, getUniqueId())
            putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
            putString(CategoryNavConstants.ROWS, pagingRowCount.toString())
            putString(CategoryNavConstants.SOURCE, "catalog")
            putString(CategoryNavConstants.CTG_ID, catalogId)
            putAllString(getSelectedSort())
            putAllString(getSelectedFilter())
        }
        param.putString(CatalogConstant.PRODUCT_PARAMS, createParametersForQuery(searchProductRequestParams.parameters))


        val topAdsRequestParam = RequestParams.create()
        topAdsRequestParam.apply {
            putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
            putString(CategoryNavConstants.DEVICE, "android")
            putString(CategoryNavConstants.KEY_SRC, "directory")
            putString(CategoryNavConstants.KEY_PAGE, start.toString())
            putString(CategoryNavConstants.KEY_EP, "product")
            putString(CategoryNavConstants.KEY_ITEM, "2")
            putString(CategoryNavConstants.KEY_F_SHOP, "1")
            putString(CategoryNavConstants.KEY_DEPT_ID, departmentId)
            putString(CategoryNavConstants.CTG_ID, catalogId)
        }
        topAdsRequestParam.putAllString(getSelectedSort())

        param.putString(CatalogConstant.TOP_ADS_PARAMS, createParametersForQuery(topAdsRequestParam.parameters))
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
        return pageCount
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

    override fun onItemClicked(item: ProductsItem, adapterPosition: Int) {
        val intent = getProductIntent(item.id.toString(), item.categoryID.toString())

        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
            startActivityForResult(intent, 1002)
        }

        CatalogDetailPageAnalytics.eventProductListClick(
                item.name,
                item.id.toString(),
                CurrencyFormatHelper.convertRupiahToInt(item.price),
                adapterPosition,
                "catalog/$catalogName - $catalogId",
                item.categoryBreadcrumb ?: "",
                item.isTopAds)
    }

    override fun onLongClick(item: ProductsItem, adapterPosition: Int) {

    }

    /*********************************   WishList  ******************************/

    override fun onWishlistButtonClicked(productItem: ProductsItem, position: Int) {
        if (userSession.isLoggedIn) {
            disableWishListButton(productItem.id.toString())
            if (productItem.wishlist) {
                removeWishList(productItem.id.toString(), userSession.userId, position)
            } else {
                addWishList(productItem.id.toString(), userSession.userId, position)
            }
        } else {
            launchLoginActivity(productItem.id.toString())
        }
    }

    override fun onThreeDotsClicked(productItem: ProductsItem, position: Int) {
        onWishlistButtonClicked(productItem,position)
    }

    private fun enableWishListButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId.toInt() ?: 0, true)
    }

    private fun disableWishListButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId.toInt() ?: 0, false)
    }

    private fun removeWishList(productId: String, userId: String, adapterPosition: Int) {
        CatalogDetailPageAnalytics.trackEventWishlist(false, productId)
        removeWishlistActionUseCase.createObservable(productId,
                userId, this)
    }

    private fun addWishList(productId: String, userId: String, adapterPosition: Int) {
        CatalogDetailPageAnalytics.trackEventWishlist(true, productId)
        addWishlistActionUseCase.createObservable(productId, userId,
                this)
    }

    private fun launchLoginActivity(productId: String) {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    override fun onProductImpressed(item: ProductsItem, adapterPosition: Int) {}

    override fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            val filter = getSelectedFilter()
            filter[option.key] = option.value
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
            CatalogDetailPageAnalytics.trackEvenClickQuickFilter(option, true)
        } else {
            val filter = getSelectedFilter()
            filter.remove(option.key)
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
            CatalogDetailPageAnalytics.trackEvenClickQuickFilter(option, false)
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
        viewModel.onDetach()
    }

    override fun onListItemImpressionEvent(viewedProductList: List<Visitable<Any>>, viewedTopAdsList: List<Visitable<Any>>) {
        if (viewedProductList.isNotEmpty()) {
            CatalogDetailPageAnalytics.eventProductListImpression(
                    "catalog/$catalogName - $catalogId",
                    viewedProductList, false)
        }

        if (viewedTopAdsList.isNotEmpty()) {
            CatalogDetailPageAnalytics.eventProductListImpression(
                    "catalog/$catalogName - $catalogId",
                    viewedTopAdsList, true)
        }
    }

    override fun onPause() {
        super.onPause()
        productNavListAdapter?.onPause()
    }

    override fun hasThreeDots() = true

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {
        CatalogDetailPageAnalytics.trackEvenSortApplied(selectedSortName, sortValue)
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
    }

    private fun initFilterControllerForQuickFilter(quickFilterList : List<Filter>) {
        filterController!!.initFilterController(searchParameter.getSearchParameterHashMap(), quickFilterList)
    }

    private fun setSortFilterIndicatorCounter() {
        searchSortFilter!!.indicatorCounter = getSortFilterCount(searchParameter.getSearchParameterMap())
    }

    private fun getSortFilterCount(mapParameter: Map<String, Any>): Int {
        var sortFilterCount = 0

        val mutableMapParameter = mapParameter.toMutableMap()
        val sortFilterParameter = mutableMapParameter.createAndCountSortFilterParameter {
            sortFilterCount += it
        }

        if (sortFilterParameter.hasMinAndMaxPriceFilter()) sortFilterCount -= 1
        if (sortFilterParameter.isSortHasDefaultValue()) sortFilterCount -= 1

        return sortFilterCount
    }

    private fun MutableMap<String, Any>.createAndCountSortFilterParameter(count: (Int) -> Unit): Map<String, Any> {
        val iterator = iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()

            if (entry.isNotSortAndFilterEntry()) {
                iterator.remove()
                continue
            }

            count(entry.value.toString().split(OptionHelper.OPTION_SEPARATOR).size)
        }

        return this
    }

    private fun Map.Entry<String, Any>.isNotSortAndFilterEntry(): Boolean {
        return isNotFilterAndSortKey() || isPriceFilterWithZeroValue()
    }

    private  val NON_FILTER_PREFIX = "srp_"

    private fun Map.Entry<String, Any>.isNotFilterAndSortKey(): Boolean {
        return nonFilterParameterKeyList.contains(key) || key.startsWith(NON_FILTER_PREFIX)
    }

    private fun Map.Entry<String, Any>.isPriceFilterWithZeroValue(): Boolean {
        return (key == SearchApiConst.PMIN && value.toString() == "0")
                || (key == SearchApiConst.PMAX && value.toString() == "0")
    }

    internal val nonFilterParameterKeyList = setOf(
            SearchApiConst.Q,
            SearchApiConst.RF,
            SearchApiConst.ACTIVE_TAB,
            SearchApiConst.SOURCE,
            SearchApiConst.LANDING_PAGE,
            SearchApiConst.PREVIOUS_KEYWORD,
            SearchApiConst.ORIGIN_FILTER,
            SearchApiConst.SKIP_REWRITE,
            SearchApiConst.NAVSOURCE,
            SearchApiConst.SKIP_BROADMATCH,
            SearchApiConst.HINT,
            SearchApiConst.FIRST_INSTALL,
            SearchApiConst.SEARCH_REF
    )

    private fun Map<String, Any>.hasMinAndMaxPriceFilter(): Boolean {
        var hasMinPriceFilter = false
        var hasMaxPriceFilter = false

        for(entry in this) {
            if (entry.key == SearchApiConst.PMIN) hasMinPriceFilter = true
            if (entry.key == SearchApiConst.PMAX) hasMaxPriceFilter = true

            // Immediately return so it doesn't continue the loop
            if (hasMinPriceFilter && hasMaxPriceFilter) return true
        }

        return false
    }

    fun Map<String, Any>.isSortHasDefaultValue(): Boolean {
        val sortValue = this[SearchApiConst.OB]

        return sortValue == SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
    }

    private fun addDefaultSelectedSort() {
        if (searchParameter.get(SearchApiConst.OB).isEmpty()) {
            searchParameter.set(SearchApiConst.OB, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        }
    }

    private fun hideQuickFilterShimmering() {

    }

    private fun setQuickFilter(items : List<SortFilterItem> ) {
        searchSortFilter!!.sortFilterItems.removeAllViews()
        searchSortFilter!!.visibility = View.VISIBLE
        searchSortFilter!!.sortFilterHorizontalScrollView.scrollX = 0
        searchSortFilter!!.addItem(items as ArrayList<SortFilterItem>)
        searchSortFilter!!.textView.text = getString(R.string.catalog_filter_text)
        searchSortFilter!!.parentListener = { this.openBottomSheetFilterRevamp() }
        setSortFilterNewNotification(items)
    }

    private fun setSortFilterNewNotification(items: List<SortFilterItem>) {
        val quickFilterOptionList: List<Option> = quickFilterOptionList
        for (i in items.indices) {
            if (i >= quickFilterOptionList.size) break
            val item = items[i]
            val quickFilterOption = quickFilterOptionList[i]
            sortFilterItemShowNew(item, quickFilterOption.isNew)
        }
    }

    private fun sortFilterItemShowNew(item: SortFilterItem, isNew: Boolean) {
        if (item.refChipUnify != null) item.refChipUnify.showNewNotification = isNew
    }

    private var quickFilterOptionList: List<Option> = ArrayList()

    private fun startFilter(quickFilterData : DataValue){
        processQuickFilter(quickFilterData)
    }

    private fun processQuickFilter(quickFilterData: DataValue) {
        if (dynamicFilterModel == null) initFilterControllerForQuickFilter(quickFilterData.filter)
        val sortFilterItems = arrayListOf<SortFilterItem>()
        quickFilterOptionList = arrayListOf()
        for (filter in quickFilterData.filter) {
            val options = filter.options
            (quickFilterOptionList as java.util.ArrayList<Option>).addAll(options)
            convertToSortFilterItem(filter.title, options)?.let { sortFilterItems.addAll(it) }
        }
        if (sortFilterItems.size > 0) {
            hideQuickFilterShimmering()
            setQuickFilter(sortFilterItems)
        }
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
        if(dynamicFilterModel != null){
            sortFilterBottomSheet = SortFilterBottomSheet()
            sortFilterBottomSheet?.show(
                    requireFragmentManager(),
                    searchParameter.getSearchParameterHashMap(),
                    dynamicFilterModel,
                    this
            )
            sortFilterBottomSheet!!.setOnDismissListener {
                sortFilterBottomSheet = null
            }
        }
    }

    private fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel){
        if (searchParameter != null) {
            filterController!!.appendFilterList(searchParameter.getSearchParameterHashMap(), dynamicFilterModel.data.filter)
        }

        if (sortFilterBottomSheet != null) {
            sortFilterBottomSheet!!.setDynamicFilterModel(dynamicFilterModel)
        }
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        sortFilterBottomSheet = null
        filterController!!.refreshMapParameter(applySortFilterModel.mapParameter)
        searchParameter.getSearchParameterHashMap().clear()
        searchParameter.getSearchParameterHashMap().putAll(applySortFilterModel.mapParameter)
        reloadData()
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        if (sortFilterBottomSheet == null) return
        sortFilterBottomSheet!!.setResultCountText(getString(R.string.catalog_apply_filter))
    }
}
