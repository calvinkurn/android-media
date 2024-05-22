package com.tokopedia.catalog.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.catalog.R
import com.tokopedia.catalog.analytics.CatalogReimagineDetailAnalytics
import com.tokopedia.catalog.analytics.CatalogTrackerConstant
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.composeUi.screen.CatalogSellerOfferingScreen
import com.tokopedia.catalog.ui.mapper.ProductListMapper.Companion.mapperToCatalogProductAtcUiModel
import com.tokopedia.catalog.ui.model.CatalogFilterProductListState
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.catalog.ui.model.CatalogProductListState
import com.tokopedia.catalog.ui.model.CatalogProductListUiModel
import com.tokopedia.catalog.ui.viewmodel.CatalogSellerOfferingProductListViewModel
import com.tokopedia.catalog.util.ColorConst.COLOR_DEEP_AZURE
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.model.filter.DAFilterQueryType
import com.tokopedia.common_category.util.ParamMapToUrl
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.oldcatalog.model.util.CatalogSearchApiConst
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.sortfilter.compose.SortFilter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogSellerOfferingFragment :
    BaseDaggerFragment(),
    ChooseAddressWidget.ChooseAddressWidgetListener,
    SortFilterBottomSheet.Callback {

    private var limit = 20
    private var catalogId = ""
    private var minPrice = ""
    private var maxPrice = ""
    private var userSession: UserSession? = null
    private var catalogUrl: String = ""
    private var page = 0
    private var selectedMoreFilterCount = mutableStateOf(0)
    private var throwableError = mutableStateOf<Throwable?>(null)
    private var productListState = mutableStateOf<CatalogProductListState>(CatalogProductListState.Loading)
    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var productTitleState = mutableStateOf(String.EMPTY)
    private var productVariantState = mutableStateOf(String.EMPTY)
    private var backgroundState = mutableStateOf(String.EMPTY)
    private var totalItemCart = mutableStateOf(0)
    private var sortFilter = mutableStateOf<CatalogFilterProductListState>(CatalogFilterProductListState.Loading)
    private var userAddressData: LocalCacheModel? = null
    private var hasNextPageState = mutableStateOf(false)
    private var productList = mutableListOf<CatalogProductListUiModel.CatalogProductUiModel>()
    private val seenTracker = mutableListOf<String>()
    private var refreshState = mutableStateOf(false)
    private var clickFilterState = mutableStateOf(false)

    @Inject
    lateinit var viewModel: CatalogSellerOfferingProductListViewModel

    private val sortFilterBottomSheet: SortFilterBottomSheet by lazy {
        SortFilterBottomSheet()
    }

    companion object {
        private const val LOGIN_REQUEST_CODE = 1004
        private const val ARG_CATALOG_ID = "catalog_id"
        private const val ARG_PRODUCT_TITLE = "product_title"
        private const val ARG_PRODUCT_VARIANT = "product_variant"
        private const val ARG_BACKGROUND = "background"
        private const val ARG_LIMIT = "limit"
        private const val ARG_MIN_PRICE = "min_price"
        private const val ARG_MAX_PRICE = "max_price"

        fun newInstance(
            catalogId: String,
            productTitle: String,
            productVariant: String,
            background: String,
            limit: String,
            minPrice: String,
            maxPrice: String
        ): CatalogSellerOfferingFragment {
            val fragment = CatalogSellerOfferingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_CATALOG_ID, catalogId)
            bundle.putString(ARG_PRODUCT_TITLE, productTitle)
            bundle.putString(ARG_PRODUCT_VARIANT, productVariant)
            bundle.putString(ARG_BACKGROUND, background)
            bundle.putString(ARG_LIMIT, limit)
            bundle.putString(ARG_MIN_PRICE, minPrice)
            bundle.putString(ARG_MAX_PRICE, maxPrice)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                NestTheme(isOverrideStatusBarColor = false, darkTheme = false) {
                    CatalogSellerOfferingScreen(
                        productTitleState.value,
                        productVariantState.value,
                        backgroundState.value.stringHexColorParseToInt(),
                        totalItemCart.value,
                        sortFilter,
                        productListState,
                        selectedMoreFilterCount.value,
                        throwableError.value,
                        lcaListener = {
                            this@CatalogSellerOfferingFragment.chooseAddressWidget = it
                            chooseAddressWidget?.bindChooseAddress(this@CatalogSellerOfferingFragment)
                        },
                        onClickVariant = {
                        },
                        onToolbarBackIconPressed = ::onToolbarBackPressed,
                        onClickActionButtonCart = ::goToCart,
                        onClickActionButtonMenu = ::goToMenu,
                        onClickMoreFilter = ::openBottomSheetFilter,
                        onLoadMore = ::loadMoreData,
                        onClickItemProduct = ::onClickItemProduct,
                        onErrorRefresh = ::onRefresh,
                        onClickAtc = ::onClickAtc,
                        hasNextPage = hasNextPageState,
                        onImpressionProduct = ::onImpressionProduct,
                        onRefresh = {
                            refreshState.value = true
                            initLoadData()
                        },
                        isRefreshing = refreshState.value,
                        clickFilter = clickFilterState.value,
                        resetFilter = ::resetFilter
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productTitleState.value = arguments?.getString(ARG_PRODUCT_TITLE).orEmpty()
        productVariantState.value = arguments?.getString(ARG_PRODUCT_VARIANT).orEmpty()
        backgroundState.value = "#${arguments?.getString(ARG_BACKGROUND) ?: COLOR_DEEP_AZURE}"
        limit = arguments?.getString(ARG_LIMIT).orEmpty().toIntSafely()
        catalogId = arguments?.getString(ARG_CATALOG_ID).orEmpty()
        minPrice = arguments?.getString(ARG_MIN_PRICE).orEmpty()
        maxPrice = arguments?.getString(ARG_MAX_PRICE).orEmpty()
        initChooseAddressWidget()
        initSearchQuickSortFilter()
        viewModel.fetchQuickFilters(getQuickFilterParams())
        viewModel.fetchDynamicAttribute(getDynamicFilterParams())
        initLoadData()
        setupObserver(view)
    }

    override fun getScreenName() = CatalogSellerOfferingFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        chooseAddressWidget?.updateWidget()
        checkAddressUpdate()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {
    }

    override fun onLocalizingAddressServerDown() {
        chooseAddressWidget?.hide()
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
    }

    override fun getLocalizingAddressHostFragment(): Fragment {
        return this
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return CatalogConstant.SOURCE
    }

    override fun onLocalizingAddressLoginSuccess() {
    }

    override fun onChangeTextColor(): Int {
        return unifyprinciplesR.color.Unify_Static_White
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.refreshNotification()
        }
        AtcVariantHelper.onActivityResultAtcVariant(context ?: return, requestCode, data) {
            viewModel.refreshNotification()
            goToCart()
        }
    }

    override fun onResume() {
        super.onResume()
        updateChooseAddressWidget()
        checkAddressUpdate(false)
        viewModel.refreshNotification()
    }

    private fun initLoadData(isFilter: Boolean = false) {
        page = 0
        productListState.value = CatalogProductListState.Loading
        productList.clear()
        if (isFilter) {
            clickFilterState.value = true
            viewModel.quickFilterClicked.value = true
            setSortFilterIndicatorCounter()
        }
        viewModel.fetchProductListing(getProductListParams())
    }

    private fun loadMoreData() {
        page += 1
        viewModel.fetchProductListing(getProductListParams(page))
    }
    private fun setupObserver(view: View) {
        viewModel.totalCartItem.observe(viewLifecycleOwner) {
            totalItemCart.value = it
        }

        viewModel.quickFilterClicked.observe(viewLifecycleOwner) {
            viewModel.quickFilterModel.value?.let {
                processQuickFilter(it.data)
            }
        }

        viewModel.quickFilterResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    startFilter(it.data.data)
                    viewModel.quickFilterModel.value = it.data
                }

                is Fail -> {
                    throwableError.value = it.throwable
                }
            }
        }

        viewModel.dynamicFilterModel.observe(viewLifecycleOwner) {
            it?.let { dm ->
                setDynamicFilter(dm)
            }
        }

        viewModel.mDynamicFilterModel.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    viewModel.dynamicFilterModel.value = it.data
                }

                is Fail -> {
                }
            }
        }

        viewModel.productList.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    hasNextPageState.value = it.data.products.size == limit &&
                        productListState.value.data?.size.orZero() != it.data.header.totalData
                    productList.addAll(it.data.products)
                    productListState.value = CatalogProductListState.Success(productList)
                    refreshState.value = false
                }

                is Fail -> {
                    throwableError.value = it.throwable
                }
            }
        }

        viewModel.errorsToaster.observe(viewLifecycleOwner) {
            Toaster.build(view, it.message.orEmpty(), type = Toaster.TYPE_ERROR).show()
        }

        viewModel.textToaster.observe(viewLifecycleOwner) {
            if (it != null) goToCart()
        }
        viewModel.selectedSortIndicatorCount.observe(viewLifecycleOwner) {
            selectedMoreFilterCount.value = it
        }
    }

    private fun onClickVariantToolbar(atcModel: CatalogProductAtcUiModel) {
        if (viewModel.isUserLoggedIn()) {
            if (atcModel.isVariant) {
                openVariantBottomSheet(atcModel)
            } else {
                viewModel.addProductToCart(atcModel)
            }
        } else {
            goToLoginPage()
        }
    }

    private fun onToolbarBackPressed() {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = CatalogTrackerConstant.EVENT_VIEW_CLICK_PG,
            action = CatalogTrackerConstant.EVENT_ACTION_CLICK_BACK_BUTTON,
            category = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
            labels = catalogId,
            trackerId = CatalogTrackerConstant.TRACKER_ID_BACK_BUTTON_CATALOG_PRODUCT_LIST
        )
        activity?.finish()
    }

    private fun fetchUserLatestAddressData() {
        context?.let {
            userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun checkAddressUpdate(isReload: Boolean = true) {
        context?.let {
            userAddressData?.let { addressData ->
                if (ChooseAddressUtils.isLocalizingAddressHasUpdated(it, addressData)) {
                    userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
                    if (isReload) {
                        initLoadData()
                    }
                }
            }
        }
    }
    private fun initChooseAddressWidget() {
        fetchUserLatestAddressData()
        chooseAddressWidget?.bindChooseAddress(this)
    }

    private fun goToCart() {
        if (viewModel.isUserLoggedIn()) {
            RouteManager.route(context, ApplinkConst.CART)
        } else {
            goToLoginPage()
        }
    }

    private fun goToLoginPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        startActivityForResult(intent, LOGIN_REQUEST_CODE)
    }

    private fun goToMenu() {
        RouteManager.route(context, ApplinkConst.HOME_NAVIGATION)
    }

    private fun updateChooseAddressWidget() {
        chooseAddressWidget?.updateWidget()
    }

    private fun openVariantBottomSheet(atcModel: CatalogProductAtcUiModel) {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                it,
                atcModel.productId,
                VariantPageSource.CATALOG_PAGESOURCE,
                shopId = atcModel.shopId,
                dismissAfterTransaction = true,
                startActivitResult = { intent, reqCode ->
                    startActivityForResult(intent, reqCode)
                }
            )
        }
    }

    private fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel) {
        viewModel.filterController?.appendFilterList(
            viewModel.searchParameter.getSearchParameterHashMap(),
            dynamicFilterModel.data.filter
        )
        sortFilterBottomSheet.setDynamicFilterModel(dynamicFilterModel)
    }

    private fun processQuickFilter(quickFilterData: DataValue) {
        if (viewModel.dynamicFilterModel.value == null) {
            initFilterControllerForQuickFilter(quickFilterData.filter)
        }

        val sortFilterItems = mutableListOf<SortFilter>()
        viewModel.quickFilterOptionList.clear()

        quickFilterData.filter.forEach { filter ->
            val options = filter.options
            viewModel.quickFilterOptionList.addAll(options)
            sortFilterItems.addAll(convertToSortFilterItem(options))
        }

        if (sortFilterItems.isNotEmpty()) {
            setQuickFilter(sortFilterItems)
        }
    }

    private fun initFilterControllerForQuickFilter(quickFilterList: List<Filter>) {
        viewModel.filterController?.initFilterController(
            viewModel.searchParameter.getSearchParameterHashMap(),
            quickFilterList
        )
    }

    private fun convertToSortFilterItem(options: List<Option>) =
        options.map { option ->
            createSortFilterItem(option.name, option)
        }

    private fun createSortFilterItem(title: String, option: Option): SortFilter {
        val item = SortFilter(title, isQuickFilterSelected(option), icon = option.iconUrl) {
            onQuickFilterSelected(option)
        }
        return item
    }

    private fun startFilter(quickFilterData: DataValue) {
        processQuickFilter(quickFilterData)
    }
    private fun setQuickFilter(items: List<SortFilter>) {
        sortFilter.value = CatalogFilterProductListState.Success(items.toMutableList())
    }

    private fun initSearchQuickSortFilter() {
        if (viewModel.searchParametersMap.value == null) {
            addDefaultSelectedSort()
            viewModel.searchParametersMap.value =
                viewModel.searchParameter.getSearchParameterHashMap()
        }
    }

    private fun addDefaultSelectedSort() {
        if (viewModel.searchParameter.get(CatalogSearchApiConst.OB).isEmpty()) {
            viewModel.searchParameter.set(
                CatalogSearchApiConst.OB,
                CatalogSearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
            )
        }
    }

    private fun onQuickFilterSelected(option: Option) {
        val isQuickFilterSelectedReversed = !isQuickFilterSelected(option)
        setFilterToQuickFilterController(option, isQuickFilterSelectedReversed)
        val queryParams = viewModel.filterController?.getParameter()
        queryParams?.let {
            refreshSearchParameters(queryParams)
            refreshFilterControllers(HashMap(queryParams))
        }
        val labelTracker = "$catalogId - sort & filter: ${option.name}"
        CatalogReimagineDetailAnalytics.sendEvent(
            event = CatalogTrackerConstant.EVENT_VIEW_CLICK_PG,
            action = CatalogTrackerConstant.EVENT_ACTION_CLICK_QUICK_FILTER,
            category = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
            labels = labelTracker,
            trackerId = CatalogTrackerConstant.TRACKER_ID_QUICK_FILTER_CATALOG_PRODUCT_LIST
        )
        initLoadData(true)
    }

    private fun isQuickFilterSelected(option: Option): Boolean {
        return viewModel.filterController?.getFilterViewState(option.uniqueId) ?: return false
    }

    private fun setFilterToQuickFilterController(option: Option, isQuickFilterSelected: Boolean) {
        if (option.isCategoryOption) {
            viewModel.filterController?.setFilter(option, isQuickFilterSelected, true)
        } else {
            viewModel.filterController?.setFilter(option, isQuickFilterSelected)
        }
    }

    private fun refreshSearchParameters(queryParams: Map<String, String>) {
        viewModel.searchParameter.apply {
            getSearchParameterHashMap().clear()
            getSearchParameterHashMap().putAll(queryParams)
            getSearchParameterHashMap()[SearchApiConst.ORIGIN_FILTER] =
                SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        }
    }

    private fun refreshFilterControllers(queryParams: HashMap<String, String>) {
        val params = HashMap(queryParams)
        params[SearchApiConst.ORIGIN_FILTER] =
            SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE

        viewModel.filterController?.refreshMapParameter(params)
    }

    private fun getQuickFilterParams(): RequestParams {
        val param = RequestParams.create()
        val searchFilterParams = RequestParams.create()
        searchFilterParams.apply {
            putString(CategoryNavConstants.DEVICE, CatalogConstant.DEVICE)
            putString(CategoryNavConstants.Q, "")
            putString(CategoryNavConstants.SOURCE, CatalogConstant.QUICK_FILTER_SOURCE)
        }
        param.putString(
            CatalogConstant.QUICK_FILTER_PARAMS,
            createParametersForQuery(searchFilterParams.parameters)
        )
        return param
    }

    private fun getDynamicFilterParams(): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = ""
        paramMap.apply {
            putString(CategoryNavConstants.DEVICE, CatalogConstant.DEVICE)
            putString(CategoryNavConstants.SOURCE, CatalogConstant.SEARCH_PRODUCT_SOURCE)
            putObject(CategoryNavConstants.FILTER, daFilterQueryType)
            putString(CategoryNavConstants.Q, "")
        }
        return paramMap
    }
    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }

    private fun openBottomSheetFilter() {
        activity?.supportFragmentManager?.let {
            sortFilterBottomSheet.show(
                it,
                viewModel.searchParametersMap.value,
                viewModel.dynamicFilterModel.value,
                this
            )
        }
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        applySort(applySortFilterModel)
        viewModel.filterController?.refreshMapParameter(applySortFilterModel.mapParameter)
        viewModel.searchParameter.getSearchParameterHashMap().clear()
        viewModel.searchParameter.getSearchParameterHashMap()
            .putAll(applySortFilterModel.mapParameter)
        viewModel.searchParametersMap.value = viewModel.searchParameter.getSearchParameterHashMap()
        val labelTracker = "$catalogId - sort & filter: ${applySortFilterModel.selectedSortName}"
        CatalogReimagineDetailAnalytics.sendEvent(
            event = CatalogTrackerConstant.EVENT_VIEW_CLICK_PG,
            action = CatalogTrackerConstant.EVENT_ACTION_CLICK_FILTER,
            category = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
            labels = labelTracker,
            trackerId = CatalogTrackerConstant.TRACKER_ID_FILTER_CATALOG_PRODUCT_LIST
        )
        initLoadData(true)
    }

    private fun applySort(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        if (applySortFilterModel.selectedSortName.isEmpty() ||
            applySortFilterModel.selectedSortMapParameter.isEmpty()
        ) {
            return
        }
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        sortFilterBottomSheet.setResultCountText(getString(R.string.catalog_apply_filter))
    }

    private fun setSortFilterIndicatorCounter() {
        viewModel.selectedSortIndicatorCount.value =
            getSortFilterCount(viewModel.searchParameter.getSearchParameterMap())
    }

    private fun onClickAtc(catalogProduct: CatalogProductListUiModel.CatalogProductUiModel, position: Int) {
        CatalogReimagineDetailAnalytics.sendEventAtc(
            event = CatalogTrackerConstant.EVENT_NAME_PRODUCT_CLICK,
            eventAction = CatalogTrackerConstant.EVENT_ACTION_CLICK_ADD_TO_CART,
            eventCategory = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
            catalogId = catalogId,
            trackerId = CatalogTrackerConstant.TRACKER_ID_CLICK_ADD_TO_CART_CATALOG_PRODUCT_LIST,
            item = catalogProduct,
            searchFilterMap = viewModel.searchParametersMap.value,
            position = position,
            userSession?.userId.orEmpty(),
            catalogUrl,
            productTitleState.value
        )
        addToCart(mapperToCatalogProductAtcUiModel(catalogProduct))
    }
    private fun addToCart(atcModel: CatalogProductAtcUiModel) {
        if (viewModel.isUserLoggedIn()) {
            if (atcModel.isVariant) {
                openVariantBottomSheet(atcModel)
            } else {
                viewModel.addProductToCart(atcModel)
            }
        } else {
            goToLoginPage()
        }
    }

    private fun getProductListParams(start: Int = 0): RequestParams {
        val param = RequestParams.create()
        val searchProductRequestParams = RequestParams.create()

        searchProductRequestParams.apply {
            putString(CategoryNavConstants.START, (start * limit).toString())
            putString(CategoryNavConstants.DEVICE, CatalogConstant.DEVICE_MOBILE)
            putString(CategoryNavConstants.USER_ID, viewModel.getUserId())
            putString(CategoryNavConstants.ROWS, limit.toString())
            putString(CategoryNavConstants.SOURCE, CatalogConstant.SOURCE)
            putString(CategoryNavConstants.CTG_ID, catalogId)
            putString(CategoryNavConstants.USER_CITY_ID, userAddressData?.city_id ?: "")
            putString(CategoryNavConstants.USER_DISTRICT_ID, userAddressData?.district_id ?: "")
            putString(CategoryNavConstants.MIN_PRICE, minPrice)
            putString(CategoryNavConstants.MAX_PRICE, maxPrice)

            viewModel.searchParametersMap.value?.let { safeSearchParams ->
                putAllString(safeSearchParams)
            }
        }
        param.putString(
            CatalogConstant.PRODUCT_PARAMS,
            createParametersForQuery(searchProductRequestParams.parameters)
        )
        return param
    }

    private fun onRefresh() {
        throwableError.value = null
        chooseAddressWidget?.show()
        fetchUserLatestAddressData()
        viewModel.fetchQuickFilters(getQuickFilterParams())
        viewModel.fetchDynamicAttribute(getDynamicFilterParams())
        initLoadData()
    }

    private fun onClickItemProduct(catalogProduct: CatalogProductListUiModel.CatalogProductUiModel, position: Int) {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = CatalogTrackerConstant.EVENT_NAME_PRODUCT_CLICK,
            eventAction = CatalogTrackerConstant.EVENT_ACTION_CLICK_PRODUCT,
            eventCategory = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
            catalogId = catalogId,
            trackerId = CatalogTrackerConstant.TRACKER_ID_CLICK_CATALOG_PRODUCT_LIST,
            item = catalogProduct,
            searchFilterMap = viewModel.searchParametersMap.value,
            position = position + 1,
            userId = userSession?.userId.orEmpty(),
            catalogUrl = catalogUrl,
            productName = productTitleState.value
        )
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, catalogProduct.productID)
    }
    private fun onImpressionProduct(item: CatalogProductListUiModel.CatalogProductUiModel, position: Int) {
        sendOnTimeImpression(item.productID) {
            CatalogReimagineDetailAnalytics.sendEventImpression(
                event = CatalogTrackerConstant.EVENT_NAME_PRODUCT_VIEW,
                eventAction = CatalogTrackerConstant.EVENT_ACTION_IMPRESSION_PRODUCT,
                eventCategory = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
                catalogId = catalogId,
                trackerId = CatalogTrackerConstant.TRACKER_ID_IMPRESSION_PRODUCT,
                item = item,
                searchFilterMap = viewModel.searchParametersMap.value,
                position = position + 1,
                userId = userSession?.userId.orEmpty(),
                catalogUrl = catalogUrl,
                productName = productTitleState.value
            )
        }
    }

    private fun resetFilter() {
        viewModel.searchParameter.getSearchParameterHashMap().clear()
        viewModel.searchParametersMap.value = null
        viewModel.filterController?.resetAllFilters()
        setSortFilterIndicatorCounter()
        initSearchQuickSortFilter()
        initLoadData(true)
    }

    private fun sendOnTimeImpression(uniqueId: String, trackerFunction: () -> Unit) {
        if (!seenTracker.any { it == uniqueId }) {
            seenTracker.add(uniqueId)
            trackerFunction.invoke()
        }
    }
}
