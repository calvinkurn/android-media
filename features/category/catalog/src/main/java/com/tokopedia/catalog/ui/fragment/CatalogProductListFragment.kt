package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalog.R
import com.tokopedia.catalog.databinding.FragmentCatalogProductListBinding
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
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
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
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
import kotlinx.android.synthetic.main.fragment_catalog_detail_product_listing.*
import javax.inject.Inject


class CatalogProductListFragment : BaseDaggerFragment(), ChooseAddressWidget.ChooseAddressWidgetListener,
    QuickFilterListener, SortFilterBottomSheet.Callback {

    @Inject
    lateinit var viewModel: CatalogProductListViewModel

    private var binding by autoClearedNullable<FragmentCatalogProductListBinding>()

    private var sortFilterBottomSheet: SortFilterBottomSheet? = null

    private var userAddressData: LocalCacheModel? = null

    val userSession: UserSession by lazy {
        UserSession(activity)
    }

    private val PAGING_ROW_COUNT = 20


    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        const val CATALOG_PRODUCT_LIST_PAGE_FRAGMENT_TAG = "CATALOG_PRODUCT_LIST_PAGE_FRAGMENT_TAG"
        fun newInstance(catalogId: String): CatalogProductListFragment {
            val fragment = CatalogProductListFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogProductListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver(view)
        initToolbar()
        initChooseAddressWidget()
        initSearchQuickSortFilter()
        viewModel.fetchQuickFilters(getQuickFilterParams())
        viewModel.fetchDynamicAttribute(getDynamicFilterParams())
        sortFilterBottomSheet = SortFilterBottomSheet()
        view.postDelayed(
            {
                addToCart(
                    CatalogProductAtcUiModel(
                        productId = "2150860905",
                        shopId = "6550986",
                        isVariant = false
                    )
                )
            }, 5000
        )
    }

    private fun loadPage(){
        viewModel.quickFilterClicked.value = true
        setSortFilterIndicatorCounter()
    }

    private fun initToolbar() {
        binding?.apply {
            toolbar.shareButton?.hide()
            toolbar.searchButton?.hide()
            toolbar.cartButton?.setOnClickListener {
                RouteManager.route(context, ApplinkConst.CART)
            }
            toolbar.setNavigationOnClickListener {
                activity?.finish()
            }
        }

    }

    override fun getScreenName() = CatalogProductListFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initChooseAddressWidget() {
        fetchUserLatestAddressData()
        binding?.chooseAddressWidget?.bindChooseAddress(this)
    }

    private fun initSearchQuickSortFilter() {
        if (viewModel.searchParametersMap.value == null) {
            addDefaultSelectedSort()
            viewModel.searchParametersMap.value = viewModel.searchParameter.getSearchParameterHashMap()
        }
    }

    private fun addDefaultSelectedSort() {
        if (viewModel.searchParameter.get(CatalogSearchApiConst.OB).isEmpty()) {
            viewModel.searchParameter.set(CatalogSearchApiConst.OB, CatalogSearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        }
    }

    private fun setupObserver(view: View) {
        viewModel.quickFilterClicked.observe(
            viewLifecycleOwner
        ) {
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
                    search_product_quick_sort_filter.hide()
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
            Toaster.build(view, it).show()
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
        viewModel.filterController?.initFilterController(viewModel.searchParameter.getSearchParameterHashMap(), quickFilterList)
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

        binding?.searchProductQuickSortFilter?.parentListener = { this.openBottomSheetFilter() }
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
        viewModel.selectedSortIndicatorCount.value = getSortFilterCount(viewModel.searchParameter.getSearchParameterMap())
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
//        reloadData()
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

    fun refreshSearchParameters(queryParams: Map<String, String>) {
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
        param.putString(CatalogConstant.QUICK_FILTER_PARAMS, createParametersForQuery(searchFilterParams.parameters))
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
        viewModel.searchParameter.getSearchParameterHashMap().putAll(applySortFilterModel.mapParameter)
        viewModel.searchParametersMap.value = viewModel.searchParameter.getSearchParameterHashMap()
//        reloadData()
    }

    private fun applySort(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        if (applySortFilterModel.selectedSortName.isEmpty() ||
            applySortFilterModel.selectedSortMapParameter.isEmpty()
        ) {
            return
        }
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        sortFilterBottomSheet?.setResultCountText(getString(R.string.catalog_apply_filter))
    }

    override fun onResume() {
        super.onResume()
        updateChooseAddressWidget()
        checkAddressUpdate(false)
    }

    private fun fetchUserLatestAddressData() {
        context?.let {
            userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }
    private fun checkAddressUpdate(isReload: Boolean = true) {
        context?.let {
            if (userAddressData != null) {
                if (ChooseAddressUtils.isLocalizingAddressHasUpdated(it, userAddressData!!)) {
                    userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
                    if (isReload) {
//                        refreshPage()
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

    private fun getProductListParams(start: Int): RequestParams {
        val param = RequestParams.create()
        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.apply {
            putString(CategoryNavConstants.START, (start * PAGING_ROW_COUNT).toString())
            putString(CategoryNavConstants.DEVICE, CatalogConstant.DEVICE)
            putString(CategoryNavConstants.USER_ID, userSession.userId)
            putString(CategoryNavConstants.ROWS, PAGING_ROW_COUNT.toString())
            putString(CategoryNavConstants.SOURCE, CatalogConstant.SOURCE)
//            putString(CategoryNavConstants.CTG_ID, catalogId)
            putString(CategoryNavConstants.USER_CITY_ID, userAddressData?.city_id ?: "")
            putString(CategoryNavConstants.USER_DISTRICT_ID, userAddressData?.district_id ?: "")
//            if (productSortingStatus == 1) {
//                putInt(CategoryNavConstants.SHOP_TIER,
//                    CatalogDetailProductListingFragment.SHOP_TIER_VALUE
//                )
//            }
            viewModel.searchParametersMap.value?.let { safeSearchParams ->
                putAllString(safeSearchParams)
            }
        }
        param.putString(CatalogConstant.PRODUCT_PARAMS, createParametersForQuery(searchProductRequestParams.parameters))
        return param
    }

    private fun addToCart(atcModel: CatalogProductAtcUiModel) {
        if (atcModel.isVariant) {
            openVariantBottomSheet(atcModel)
        } else {
            viewModel.addProductToCart(atcModel)
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
}
