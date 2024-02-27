package com.tokopedia.catalog.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.catalog.R
import com.tokopedia.catalog.analytics.CatalogReimagineDetailAnalytics
import com.tokopedia.catalog.analytics.CatalogTrackerConstant
import com.tokopedia.catalog.base.BaseSimpleListFragment
import com.tokopedia.catalog.databinding.FragmentCatalogProductListImprovementBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.ui.adapter.CatalogProductListAdapterFactoryImpl
import com.tokopedia.catalog.ui.adapter.CatalogProductListDiffer
import com.tokopedia.catalog.ui.adapter.CatalogProductListImprovementAdapter
import com.tokopedia.catalog.ui.adapter.EmptyStateFilterListener
import com.tokopedia.catalog.ui.adapter.ProductListAdapterListener
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.catalog.ui.model.CatalogProductListEmptyModel
import com.tokopedia.catalog.ui.viewmodel.CatalogProductListViewModel
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.interfaces.QuickFilterListener
import com.tokopedia.common_category.model.filter.DAFilterQueryType
import com.tokopedia.common_category.util.ParamMapToUrl
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterCount
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.oldcatalog.model.util.CatalogSearchApiConst
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CatalogProductListImprovementFragment :
    BaseSimpleListFragment<CatalogProductListImprovementAdapter, CatalogProductItem>(),
    ChooseAddressWidget.ChooseAddressWidgetListener,
    QuickFilterListener, SortFilterBottomSheet.Callback, ProductListAdapterListener,
    EmptyStateFilterListener {

    companion object {
        private const val LOGIN_REQUEST_CODE = 1003
        private const val SHOP_TIER_VALUE = 2
        private const val ARG_EXTRA_CATALOG_URL = "ARG_EXTRA_CATALOG_URL"
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_PRODUCT_SORTING_STATUS = "ARG_EXTRA_PRODUCT_SORTING_STATUS"
        private const val ARG_EXTRA_CATALOG_TITLE = "ARG_EXTRA_CATALOG_TITLE"
        const val CATALOG_PRODUCT_LIST_PAGE_FRAGMENT_TAG = "CATALOG_PRODUCT_LIST_PAGE_FRAGMENT_TAG"
        fun newInstance(
            catalogId: String,
            catalogTitle: String,
            productSortingStatus: String,
            catalogUrl: String
        ): CatalogProductListImprovementFragment {
            val fragment = CatalogProductListImprovementFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            bundle.putString(ARG_EXTRA_CATALOG_URL, catalogUrl)
            bundle.putString(ARG_EXTRA_CATALOG_TITLE, catalogTitle)
            bundle.putString(ARG_EXTRA_PRODUCT_SORTING_STATUS, productSortingStatus)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: CatalogProductListViewModel

    private var binding by autoClearedNullable<FragmentCatalogProductListImprovementBinding>()
    private var userAddressData: LocalCacheModel? = null

    private val sortFilterBottomSheet: SortFilterBottomSheet by lazy {
        SortFilterBottomSheet()
    }

    private var userSession: UserSession? = null

    private var catalogUrl: String = ""

    private var catalogTitle: String = ""

    private var catalogId: String =  ""

    private var productSortingStatus: String = ""

    private val adapterFactory by lazy {
        CatalogProductListAdapterFactoryImpl(
            this,
            this
        )
    }

    private val emptyModelFilter: CatalogProductListEmptyModel by lazy {
        CatalogProductListEmptyModel(isFromFilter = true).apply {
            description = getString(R.string.text_empty_state_desc)
            title = getString(R.string.text_empty_state_title)
            urlRes = TokopediaImageUrl.ILLUSTRATION_EMPTY_CATALOG_PRODUCT_LIST
        }
    }

    private val emptyModelInitial: CatalogProductListEmptyModel by lazy {
        CatalogProductListEmptyModel(isFromFilter = false).apply {
            description = getString(R.string.catalog_no_products_body)
            title = getString(R.string.catalog_no_products_title)
            urlRes = TokopediaImageUrl.ILLUSTRATION_EMPTY_CATALOG_PRODUCT_LIST
        }
    }

    override fun createAdapter(): CatalogProductListImprovementAdapter? {
        val asyncDifferConfig = AsyncDifferConfig.Builder(CatalogProductListDiffer())
            .build()
        return CatalogProductListImprovementAdapter(asyncDifferConfig, adapterFactory)
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return binding?.productRecyclerview
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return binding?.swipeRefreshLayout
    }

    override fun getPerPage(): Int {
        return  PAGING_ROW_COUNT
    }

    override fun loadData(page: Int) {
        if (page == defaultInitialPage) {
            viewModel.quickFilterClicked.value = true
            setSortFilterIndicatorCounter()
            isLoadingInitialData = true
            clearAllData()
            onShowLoading()
        }
        viewModel.fetchProductListing(getProductListParams(page))
    }

    override fun onShowLoading() {
        binding?.productRecyclerview?.gone()
        binding?.shimmerProductList?.root?.show()
    }

    override fun onHideLoading() {
        binding?.productRecyclerview?.show()
        binding?.shimmerProductList?.root?.gone()
    }

    override fun onDataEmpty() {
        if (viewModel.selectedSortIndicatorCount.value.orZero() > 0) {
            adapter?.submitList(listOf(emptyModelFilter))
        } else {
            adapter?.submitList(listOf(emptyModelInitial))
        }
    }

    override fun onGetListError(message: String) {
        showErrorPage()
    }

    override fun onSwipeRefreshPulled() {
        loadInitialData()
    }

    override fun clearAdapterData() {
        adapter?.submitList(null)
    }

    override fun addElementToAdapter(list: List<CatalogProductItem>) {
        adapter?.submitList(list)
    }

    private val PAGING_ROW_COUNT = 20


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogProductListImprovementBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userSession = UserSession(activity)
        catalogUrl = arguments?.getString(ARG_EXTRA_CATALOG_URL).orEmpty()
        catalogTitle = arguments?.getString(ARG_EXTRA_CATALOG_TITLE).orEmpty()
        catalogId = arguments?.getString(ARG_EXTRA_CATALOG_ID).orEmpty()
        productSortingStatus = arguments?.getString(ARG_EXTRA_PRODUCT_SORTING_STATUS).orEmpty()

        setupObserver(view)
        initToolbar()
        initChooseAddressWidget()
        initSearchQuickSortFilter()
        initRecyclerView()
        viewModel.refreshNotification()
        super.onViewCreated(view, savedInstanceState)
        binding?.errorPage?.run {
            setActionClickListener {
                loadInitialData()
            }
        }
    }

    private fun initRecyclerView() {
        binding?.productRecyclerview?.adapter = adapter
        binding?.productRecyclerview?.layoutManager = LinearLayoutManager(context)

    }

    override fun getScreenName() = CatalogProductListImprovementFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun loadInitialData() {
        super.loadInitialData()
        binding?.shimmerSortFilter?.visible()
        hideErrorPage()
        viewModel.fetchQuickFilters(getQuickFilterParams())
        viewModel.fetchDynamicAttribute(getDynamicFilterParams())
    }


    private fun initChooseAddressWidget() {
        fetchUserLatestAddressData()
        binding?.chooseAddressWidget?.bindChooseAddress(this)
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

    private fun setupObserver(view: View) {
        viewModel.productList.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val hasNextPage = it.data.size == getPerPage()
                    renderList(it.data, hasNextPage)
                }

                is Fail -> {
                    showGetListError(it.throwable)
                }
            }
        }

        viewModel.quickFilterClicked.observe(viewLifecycleOwner) {
            viewModel.quickFilterModel.value?.let {
                processQuickFilter(it.data)
            }
        }

        viewModel.quickFilterResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.shimmerSortFilter?.gone()
                    binding?.clFilterAndLCA?.visible()
                    startFilter(it.data.data)
                    viewModel.quickFilterModel.value = it.data
                }

                is Fail -> {
                    binding?.searchProductQuickSortFilter?.hide()
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

        viewModel.errorsToaster.observe(viewLifecycleOwner) {
            Toaster.build(view, it.message.orEmpty(), type = Toaster.TYPE_ERROR).show()
        }

        viewModel.textToaster.observe(viewLifecycleOwner) {
            if (it != null) Toaster.build(view, it).show()
        }

        viewModel.totalCartItem.observe(viewLifecycleOwner) {
            binding?.toolbar?.cartCount = it
        }

        viewModel.selectedSortIndicatorCount.observe(viewLifecycleOwner) {
            binding?.searchProductQuickSortFilter?.indicatorCounter = it
        }
    }

    private fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel) {
        viewModel.filterController?.appendFilterList(
            viewModel.searchParameter.getSearchParameterHashMap(),
            dynamicFilterModel.data.filter
        )
        sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)
    }

    private fun startFilter(quickFilterData: DataValue) {
        processQuickFilter(quickFilterData)
    }

    private fun processQuickFilter(quickFilterData: DataValue) {
        if (viewModel.dynamicFilterModel.value == null) {
            initFilterControllerForQuickFilter(quickFilterData.filter)
        }

        val sortFilterItems = mutableListOf<SortFilterItem>()
        viewModel.quickFilterOptionList.clear()

        quickFilterData.filter.forEach { filter ->
            val options = filter.options
            viewModel.quickFilterOptionList.addAll(options)
            sortFilterItems.addAll(convertToSortFilterItem(filter.title, options))
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

    private fun convertToSortFilterItem(title: String, options: List<Option>) =
        options.map { option ->
            createSortFilterItem(option.name, option)
        }

    private fun createSortFilterItem(title: String, option: Option): SortFilterItem {
        val item = SortFilterItem(title) {
            onQuickFilterSelected(option)
        }
        setSortFilterItemState(item, option)
        return item
    }

    private fun setSortFilterItemState(item: SortFilterItem, option: Option) {
        if (isQuickFilterSelected(option)) {
            item.type = ChipsUnify.TYPE_SELECTED
            item.typeUpdated = false
        }
    }

    private fun setQuickFilter(items: List<SortFilterItem>) {
        binding?.searchProductQuickSortFilter?.apply {
            sortFilterItems.removeAllViews()
            visibility = View.VISIBLE
            sortFilterHorizontalScrollView.scrollX = 0
            addItem(items as ArrayList<SortFilterItem>)
            textView?.text = getString(R.string.catalog_filter_text)
        }

        binding?.searchProductQuickSortFilter?.parentListener = {
            this.openBottomSheetFilter()
        }
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
        item.refChipUnify.showNewNotification = isNew
    }

    private fun setSortFilterIndicatorCounter() {
        viewModel.selectedSortIndicatorCount.value =
            getSortFilterCount(viewModel.searchParameter.getSearchParameterMap())
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        updateChooseAddressWidget()
        checkAddressUpdate()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {
    }

    override fun onLocalizingAddressServerDown() {
        binding?.chooseAddressWidget?.hide()
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

    override fun onQuickFilterSelected(option: Option) {
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
        loadData(defaultInitialPage)
    }

    override fun onAtcButtonClicked(
        atcModel: CatalogProductAtcUiModel,
        item: CatalogProductItem,
        adapterPosition: Int
    ) {
        CatalogReimagineDetailAnalytics.sendEventAtc(
            event = CatalogTrackerConstant.EVENT_NAME_PRODUCT_CLICK,
            eventAction = CatalogTrackerConstant.EVENT_ACTION_CLICK_ADD_TO_CART,
            eventCategory = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
            catalogId = catalogId,
            trackerId = CatalogTrackerConstant.TRACKER_ID_CLICK_ADD_TO_CART_CATALOG_PRODUCT_LIST,
            item = item,
            searchFilterMap = viewModel.searchParametersMap.value,
            position = adapterPosition + 1,
            userSession?.userId.orEmpty(),
            catalogUrl
        )
        addToCart(atcModel)
    }

    override fun onClickProductCard(item: CatalogProductItem, adapterPosition: Int) {
        CatalogReimagineDetailAnalytics.sendEvent(
            event = CatalogTrackerConstant.EVENT_NAME_PRODUCT_CLICK,
            eventAction = CatalogTrackerConstant.EVENT_ACTION_CLICK_PRODUCT,
            eventCategory = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
            catalogId = catalogId,
            trackerId = CatalogTrackerConstant.TRACKER_ID_CLICK_CATALOG_PRODUCT_LIST,
            item = item,
            searchFilterMap = viewModel.searchParametersMap.value,
            position = adapterPosition + 1,
            userId = userSession?.userId.orEmpty(),
            catalogUrl = catalogUrl
        )
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.id)
    }

    override fun onProductImpression(item: CatalogProductItem, position: Int) {
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
            catalogUrl = catalogUrl
        )
    }

    private fun setFilterToQuickFilterController(option: Option, isQuickFilterSelected: Boolean) {
        if (option.isCategoryOption) {
            viewModel.filterController?.setFilter(option, isQuickFilterSelected, true)
        } else {
            viewModel.filterController?.setFilter(option, isQuickFilterSelected)
        }
    }

    override fun isQuickFilterSelected(option: Option): Boolean {
        return viewModel.filterController?.getFilterViewState(option.uniqueId) ?: return false
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

    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }

    private fun openBottomSheetFilter() {
        activity?.supportFragmentManager?.let {
            sortFilterBottomSheet?.show(
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
        loadData(defaultInitialPage)
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

    override fun onResume() {
        super.onResume()
        updateChooseAddressWidget()
        checkAddressUpdate(false)
        viewModel.refreshNotification()
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
                        loadData(defaultInitialPage)
                    }
                }
            }
        }
    }

    private fun updateChooseAddressWidget() {
        binding?.chooseAddressWidget?.updateWidget()
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

    private fun getProductListParams(start: Int = 0): RequestParams {
        val param = RequestParams.create()
        val searchProductRequestParams = RequestParams.create()

        searchProductRequestParams.apply {
            putString(CategoryNavConstants.START, (start * PAGING_ROW_COUNT).toString())
            putString(CategoryNavConstants.DEVICE, CatalogConstant.DEVICE_MOBILE)
            putString(CategoryNavConstants.USER_ID, viewModel.getUserId())
            putString(CategoryNavConstants.ROWS, PAGING_ROW_COUNT.toString())
            putString(CategoryNavConstants.SOURCE, CatalogConstant.SOURCE)
            putString(CategoryNavConstants.CTG_ID, catalogId)
            putString(CategoryNavConstants.USER_CITY_ID, userAddressData?.city_id ?: "")
            putString(CategoryNavConstants.USER_DISTRICT_ID, userAddressData?.district_id ?: "")
            if (productSortingStatus.toIntSafely() == 1) {
                putInt(
                    CategoryNavConstants.SHOP_TIER,
                    SHOP_TIER_VALUE
                )
            }
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

    private fun showErrorPage() {
        binding?.errorPage?.show()
        binding?.groupContent?.gone()
    }

    private fun hideErrorPage() {
        binding?.errorPage?.gone()
        binding?.groupContent?.show()
    }

    override fun resetFilter() {
        initSearchQuickSortFilter()
        viewModel.selectedSortIndicatorCount.value = 0
        viewModel.searchParameter.getSearchParameterHashMap().clear()
        viewModel.filterController?.resetAllFilters()
        loadData(defaultInitialPage)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.refreshNotification()
        }
        AtcVariantHelper.onActivityResultAtcVariant(context ?: return, requestCode, data) {
            viewModel.refreshNotification()
        }
    }

    private fun initToolbar() {
        binding?.toolbar?.apply {
            shareButton?.hide()
            searchButton?.hide()
            title = catalogTitle
            cartButton?.setOnClickListener {
                if (viewModel.isUserLoggedIn()) {
                    RouteManager.route(context, ApplinkConst.CART)
                } else {
                    goToLoginPage()
                }
            }
            setNavigationOnClickListener {
                CatalogReimagineDetailAnalytics.sendEvent(
                    event = CatalogTrackerConstant.EVENT_VIEW_CLICK_PG,
                    action = CatalogTrackerConstant.EVENT_ACTION_CLICK_BACK_BUTTON,
                    category = CatalogTrackerConstant.EVENT_CATEGORY_CATALOG_PAGE_REIMAGINE_PRODUCT_LIST,
                    labels = catalogId,
                    trackerId = CatalogTrackerConstant.TRACKER_ID_BACK_BUTTON_CATALOG_PRODUCT_LIST
                )
                activity?.finish()
            }
            setColors(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN950))
        }
    }


    private fun goToLoginPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        startActivityForResult(intent, LOGIN_REQUEST_CODE)
    }

}
