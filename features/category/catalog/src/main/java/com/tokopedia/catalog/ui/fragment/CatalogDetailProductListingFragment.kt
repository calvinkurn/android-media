package com.tokopedia.catalog.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.tokopedia.catalog.analytics.CatalogDetailPageAnalytics
import com.tokopedia.catalog.di.CatalogComponent
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.viewmodel.CatalogDetailProductListingViewModel
import com.tokopedia.common_category.adapter.BaseCategoryAdapter
import com.tokopedia.common_category.adapter.ProductNavListAdapter
import com.tokopedia.common_category.adapter.QuickFilterAdapter
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.factory.ProductTypeFactory
import com.tokopedia.common_category.factory.product.ProductTypeFactoryImpl
import com.tokopedia.common_category.fragment.BaseCategorySectionFragment
import com.tokopedia.common_category.interfaces.ProductCardListener
import com.tokopedia.common_category.interfaces.QuickFilterListener
import com.tokopedia.common_category.model.filter.DAFilterQueryType
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.util.ParamMapToUrl
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.show
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
        WishListActionListener {

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

    var productNavListAdapter: ProductNavListAdapter? = null
    private lateinit var quickFilterAdapter: QuickFilterAdapter

    var pageCount = 0
    var isPagingAllowed: Boolean = true

    var list: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()
    var quickFilterList = ArrayList<Filter>()

    private lateinit var productTypeFactory: ProductTypeFactory

    lateinit var userSession: UserSession
    private lateinit var gcmHandler: GCMHandler
    private var staggeredGridLayoutLoadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

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
        if (userVisibleHint) {
            setUpVisibleFragmentListener()
        }
    }

    private fun initView() {

        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)

        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            viewModel = viewModelProvider.get(CatalogDetailProductListingViewModel::class.java)
            fetchProductData(getProductListParamMap(getPage()))
            viewModel.fetchQuickFilters(getQuickFilterParams())
        }
    }

    private fun getQuickFilterParams(): RequestParams {
        val quickFilterParam = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = departmentId
        quickFilterParam.putObject(CategoryNavConstants.FILTER, daFilterQueryType)
        quickFilterParam.putString(CategoryNavConstants.SOURCE, "quick_filter")
        return quickFilterParam
    }

    private fun setUpAdapter() {
        productTypeFactory = ProductTypeFactoryImpl(this)
        productNavListAdapter = ProductNavListAdapter(productTypeFactory, list, this)
        product_recyclerview.adapter = productNavListAdapter
        product_recyclerview.layoutManager = getStaggeredGridLayoutManager()
        productNavListAdapter?.addShimmer()

        attachScrollListener()
    }

    private fun setQuickFilterAdapter(productCount: String) {
        quickFilterAdapter = QuickFilterAdapter(quickFilterList, this, productCount)
        quickfilter_recyclerview.adapter = quickFilterAdapter
        quickfilter_recyclerview.layoutManager = LinearLayoutManager(activity,
                RecyclerView.HORIZONTAL, false)
    }

    private fun attachScrollListener() {
        getStaggeredGridLayoutManager()?.let {
            staggeredGridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(it)
        }
        staggeredGridLayoutLoadMoreTriggerListener?.let {
            product_recyclerview.addOnScrollListener(it)
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                fetchProductData(getProductListParamMap(page))
                productNavListAdapter?.addLoading()
            }
        }
    }

    private fun observeData() {
        viewModel.mProductList.observe(this, Observer {

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
                        staggeredGridLayoutLoadMoreTriggerListener?.updateStateAfterGetData()
                        isPagingAllowed = true
                    } else {
                        if (list.isEmpty()) {
                            showNoDataScreen(true)
                        }
                    }
                    hideRefreshLayout()
                    reloadFilter(createFilterParam())
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

        viewModel.mProductCount.observe(this, Observer {
            it?.let {
                setTotalSearchResultCount(it)
                if (!TextUtils.isEmpty(it)) {
                    setQuickFilterAdapter(getString(R.string.catalog_result_count_template_text, it))
                } else {
                    setQuickFilterAdapter("")
                }
            }
        })

        viewModel.getDynamicFilterData().observe(this, Observer {

            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }

                is Fail -> {
                }
            }
        })


        viewModel.mQuickFilterModel.observe(this, Observer {

            when (it) {
                is Success -> {
                    initQuickFilter(it.data as ArrayList)
                }

                is Fail -> {
                }
            }

        })

    }

    private fun createFilterParam(): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = departmentId
        paramMap.putString(CategoryNavConstants.SOURCE, "search_product")
        paramMap.putObject(CategoryNavConstants.FILTER, daFilterQueryType)
        paramMap.putString(CategoryNavConstants.Q, "")
        paramMap.putString(CategoryNavConstants.SOURCE, "directory")
        return paramMap
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

    private fun initQuickFilter(list: ArrayList<Filter>) {
        quickFilterList.clear()
        quickFilterList.addAll(list)
        quickfilter_recyclerview.adapter?.notifyDataSetChanged()

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
        staggeredGridLayoutLoadMoreTriggerListener?.resetState()
        fetchProductData(getProductListParamMap(getPage()))

        viewModel.fetchQuickFilters(getQuickFilterParams())
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

    private fun getProductListParamMap(start: Int): RequestParams {

        val param = RequestParams.create()


        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.putString(CategoryNavConstants.START, (start * 10).toString())
        searchProductRequestParams.putString(CategoryNavConstants.SC, departmentId)
        searchProductRequestParams.putString(CategoryNavConstants.DEVICE, "android")
        searchProductRequestParams.putString(CategoryNavConstants.UNIQUE_ID, getUniqueId())
        searchProductRequestParams.putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        searchProductRequestParams.putString(CategoryNavConstants.ROWS, "10")
        searchProductRequestParams.putString(CategoryNavConstants.SOURCE, "search_product")
        searchProductRequestParams.putString(CategoryNavConstants.CTG_ID, catalogId)
        searchProductRequestParams.putAllString(getSelectedSort())
        searchProductRequestParams.putAllString(getSelectedFilter())
        param.putString("product_params", createParametersForQuery(searchProductRequestParams.parameters))


        val topAdsRequestParam = RequestParams.create()
        topAdsRequestParam.putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        topAdsRequestParam.putString(CategoryNavConstants.DEVICE, "android")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_SRC, "directory")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_PAGE, start.toString())
        topAdsRequestParam.putString(CategoryNavConstants.KEY_EP, "product")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_ITEM, "2")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_F_SHOP, "1")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_DEPT_ID, departmentId)
        topAdsRequestParam.putString(CategoryNavConstants.CTG_ID, catalogId)

        topAdsRequestParam.putAllString(getSelectedSort())

        param.putString("top_params", createParametersForQuery(topAdsRequestParam.parameters))
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

    override fun onWishlistButtonClicked(productItem: ProductsItem, position: Int) {
        if (userSession.isLoggedIn) {
            disableWishlistButton(productItem.id.toString())
            if (productItem.wishlist) {
                removeWishlist(productItem.id.toString(), userSession.userId, position)
            } else {
                addWishlist(productItem.id.toString(), userSession.userId, position)
            }
        } else {
            launchLoginActivity(productItem.id.toString())
        }
    }

    fun enableWishlistButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId?.toInt() ?: 0, true)
    }

    fun disableWishlistButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId?.toInt() ?: 0, false)
    }

    private fun removeWishlist(productId: String, userId: String, adapterPosition: Int) {
        CatalogDetailPageAnalytics.trackEventWishlist(false, productId)
        removeWishlistActionUseCase.createObservable(productId,
                userId, this)
    }

    private fun addWishlist(productId: String, userId: String, adapterPosition: Int) {
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
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessAddWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), true)
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.msg_add_wishlist))
    }

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String) {
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), false)
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.msg_remove_wishlist))
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
}
