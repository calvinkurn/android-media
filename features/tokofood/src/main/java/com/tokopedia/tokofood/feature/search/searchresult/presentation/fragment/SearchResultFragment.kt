package com.tokopedia.tokofood.feature.search.searchresult.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddEditAddressSource
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentSearchResultBinding
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import com.tokopedia.tokofood.feature.search.common.presentation.viewholder.TokofoodSearchErrorStateViewHolder
import com.tokopedia.tokofood.feature.search.common.util.OnScrollListenerSearch
import com.tokopedia.tokofood.feature.search.common.util.hideKeyboardOnTouchListener
import com.tokopedia.tokofood.feature.search.container.presentation.listener.SearchResultViewUpdateListener
import com.tokopedia.tokofood.feature.search.di.component.DaggerTokoFoodSearchComponent
import com.tokopedia.tokofood.feature.search.searchresult.analytics.TokofoodSearchResultAnalytics
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultAdapterTypeFactory
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultDiffer
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultPageAdapter
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchEmptyWithFilterViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchEmptyWithoutFilterViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchOOCViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchResultViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.bottomsheet.TokofoodQuickPriceRangeBottomsheet
import com.tokopedia.tokofood.feature.search.searchresult.presentation.bottomsheet.TokofoodQuickSortBottomSheet
import com.tokopedia.tokofood.feature.search.searchresult.presentation.customview.TokofoodSearchFilterTab
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchOOCUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeChipUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodQuickSortUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSearchUiEvent
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodSearchResultPageViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

class SearchResultFragment :
    BaseDaggerFragment(),
    TokofoodSearchFilterTab.Listener,
    MerchantSearchResultViewHolder.TokoFoodMerchantSearchResultListener,
    TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener,
    MerchantSearchEmptyWithFilterViewHolder.Listener,
    MerchantSearchEmptyWithoutFilterViewHolder.Listener,
    SortFilterBottomSheet.Callback,
    FilterGeneralDetailBottomSheet.Callback,
    TokofoodQuickSortBottomSheet.Listener,
    ChooseAddressWidget.ChooseAddressWidgetListener,
    TokofoodQuickPriceRangeBottomsheet.Listener,
    TokofoodSearchErrorStateViewHolder.Listener,
    MerchantSearchOOCViewHolder.Listener,
    TokofoodScrollChangedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: TokofoodSearchResultAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokofoodSearchResultPageViewModel::class.java)
    }
    private val adapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        TokofoodSearchResultAdapterTypeFactory(this, this, this, this, this, this, this)
    }
    private val merchantResultAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val differ = TokofoodSearchResultDiffer()
        TokofoodSearchResultPageAdapter(adapterTypeFactory, differ)
    }
    private val loadMoreListener by lazy(LazyThreadSafetyMode.NONE) {
        createLoadMoreListener()
    }
    private val addressWidgetImpressHolder = ImpressHolder()

    private var binding by autoClearedNullable<FragmentSearchResultBinding>()
    private var searchResultViewUpdateListener: SearchResultViewUpdateListener? = null

    private var tokofoodSearchFilterTab: TokofoodSearchFilterTab? = null
    private var searchParameter: HashMap<String, String>? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private var localCacheModel: LocalCacheModel? = null
    private var currentSortFilterValue: String = String.EMPTY

    private var keyword: String = ""
    private var itemsScrollChangedListenerList: MutableList<ViewTreeObserver.OnScrollChangedListener> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        collectFlows()
        setLocalCacheModel()
    }

    override fun onResume() {
        super.onResume()
        refreshAddressData(false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodSearchComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onOpenFullFilterBottomSheet() {
        analytics.sendCompleteFilterClickTracking(keyword)
        viewModel.openDetailFilterBottomSheet()
    }

    override fun onOpenQuickFilterBottomSheet(sortList: List<Sort>) {
        val sortValue = viewModel.getCurrentSortValue()
        val destinationId = getDestinationId()
        analytics.sendSortClickTracking(keyword, sortValue, destinationId)
        showQuickSortBottomSheet(sortList)
    }

    override fun onOpenQuickFilterBottomSheet(filter: Filter, isSelected: Boolean) {
        analytics.sendMiniFilterClickTracking(keyword, filter.options, isSelected)
        showQuickFilterBottomSheet(filter)
    }

    override fun onSelectSortChip(sort: Sort, isSelected: Boolean) {
        val destinationId = getDestinationId()
        analytics.sendSortClickTracking(keyword, sort.value, destinationId)
        viewModel.applySort(sort, isSelected)
    }

    override fun onSelectFilterChip(filter: Filter, isSelected: Boolean) {
        analytics.sendMiniFilterClickTracking(keyword, filter.options, isSelected)
        viewModel.applyFilter(filter)
    }

    override fun onImpressCompleteFilterChip() {
        analytics.sendCompleteFilterImpressionTracking(keyword, getDestinationId())
    }

    override fun onImpressSortChip(sorts: List<Sort>) {
        val sortValue = viewModel.getCurrentSortValue()
        val destinationId = getDestinationId()
        analytics.sendSortImpressionTracking(keyword, destinationId, sorts, sortValue)
    }

    override fun onImpressFilterChip(options: List<Option>) {
        analytics.sendMiniFilterImpressionTracking(keyword, options, getDestinationId())
    }

    override fun onClickRetryError() {
        viewModel.getInitialMerchantSearchResult(searchParameter)
    }

    override fun onImpressMerchant(merchant: Merchant, position: Int) {
        analytics.sendMerchantCardImpressionTracking(
            destinationId = getDestinationId(),
            keyword = keyword,
            merchant = merchant,
            sortFilterValue = currentSortFilterValue,
            index = position
        )
    }

    override fun onClickMerchant(merchant: Merchant, position: Int) {
        analytics.sendMerchantCardClickTracking(
            destinationId = getDestinationId(),
            keyword = keyword,
            merchant = merchant,
            sortFilterValue = currentSortFilterValue,
            index = position
        )
        goToMerchantPage(merchant.applink)
    }

    override fun onBranchButtonClicked(merchant: Merchant) {
        analytics.sendOtherBranchesClickTracking(
            destinationId = getDestinationId(),
            keyword = keyword,
            merchant = merchant
        )
        TokofoodRouteManager.routePrioritizeInternal(context, merchant.branchApplink)
    }

    override fun onResetFilterButtonClicked() {
        viewModel.resetFilterSearch()
    }

    override fun onCheckKeywordButtonClicked() {
        this.searchResultViewUpdateListener?.onResetKeyword()
    }

    override fun onSearchInTokopediaButtonClicked() {
        goToDiscoverySearchPage()
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        analytics.sendSubmitFilterClickTracking()
        viewModel.resetParams(applySortFilterModel.selectedFilterMapParameter + applySortFilterModel.selectedSortMapParameter)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        showDetailFilterApplyButton()
    }

    override fun onApplyButtonClicked(optionList: List<Option>?) {
        optionList?.let {
            viewModel.applyOptions(it)
        }
    }

    override fun onApplySort(uiModel: TokofoodQuickSortUiModel) {
        viewModel.applySortSelected(uiModel)
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        refreshAddressData()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {}

    override fun onLocalizingAddressServerDown() {}

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {}

    override fun getLocalizingAddressHostFragment(): Fragment = this

    override fun getLocalizingAddressHostSourceData(): String = SOURCE

    override fun onLocalizingAddressLoginSuccess() {}

    override fun onClickChooseAddressTokoNowTracker() {
        analytics.sendAddressWidgetClickTracking(getDestinationId())
    }

    override fun onApplyPriceRange(checkedOptions: List<Option>) {
        viewModel.applyOptions(checkedOptions)
    }

    override fun onRetry() {
        onClickRetryError()
    }

    override fun onGoToHome() {
        navigateToNewFragment(TokoFoodHomeFragment.createInstance())
    }

    override fun onOOCActionButtonClicked(type: Int) {
        when (type) {
            MerchantSearchOOCUiModel.NO_ADDRESS -> {
                navigateToAddAddress()
            }
            MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP -> {
                navigateToAddAddressRevamp()
            }
            MerchantSearchOOCUiModel.NO_PINPOINT -> {
                navigateToSetPinpoint()
            }
            MerchantSearchOOCUiModel.OUT_OF_COVERAGE -> {
                navigateToChangeAddress()
            }
        }
    }

    override fun onGoToHomepageButtonClicked() {
        onGoToHome()
    }

    override fun needToTrackTokoNow(): Boolean {
        return true
    }

    override fun onScrollChangedListenerAdded(onScrollChangedListener: ViewTreeObserver.OnScrollChangedListener) {
        itemsScrollChangedListenerList.add(onScrollChangedListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_ADD_ADDRESS -> onResultFromAddAddress(resultCode, data)
            REQUEST_CODE_CHANGE_ADDRESS -> onResultFromChangeAddress(resultCode)
            REQUEST_CODE_SET_PINPOINT -> onResultFromSetPinpoint(resultCode, data)
        }
    }

    override fun onDestroyView() {
        tokofoodSearchFilterTab?.removeListener()
        tokofoodSearchFilterTab = null
        removeAllScrollListener()
        itemsScrollChangedListenerList.clear()
        searchParameter = null
        sortFilterBottomSheet = null
        super.onDestroyView()
    }

    fun setSearchResultViewUpdateListener(searchResultViewUpdateListener: SearchResultViewUpdateListener) {
        this.searchResultViewUpdateListener = searchResultViewUpdateListener
    }

    fun showSearchResultState(keyword: String) {
        this.searchResultViewUpdateListener?.showSearchResultView()
        this.keyword = keyword
        viewModel.resetFilterSearch()
        viewModel.setKeyword(keyword)
    }

    private fun setupLayout() {
        setupAdapter()
        setupAddressWidget()
        setupSortFilter()
    }

    private fun setupAdapter() {
        binding?.rvTokofoodSearchResult?.run {
            adapter = merchantResultAdapter
            context?.let {
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            }
            addOnScrollListener(OnScrollListenerSearch(this))
            hideKeyboardOnTouchListener()
        }
    }

    private fun setupAddressWidget() {
        binding?.addressTokofoodSearchResult?.run {
            bindChooseAddress(this@SearchResultFragment)
            addAndReturnImpressionListener(addressWidgetImpressHolder, this@SearchResultFragment) {
                analytics.sendAddressWidgetImpressionTracking(getDestinationId())
            }
        }
    }

    private fun setupSortFilter() {
        if (tokofoodSearchFilterTab == null) {
            binding?.filterTokofoodSearchResult?.let { sortFilter ->
                tokofoodSearchFilterTab = TokofoodSearchFilterTab(
                    sortFilter,
                    context,
                    this
                )
            }
        }
    }

    private fun collectFlows() {
        collectSearchParameters()
        collectSortFilterUiModels()
        collectVisitables()
        collectUiEvent()
        collectActiveFilterCount()
    }

    private fun collectSearchParameters() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.searchParameterMap.collect {
                searchParameter = it
                currentSortFilterValue = getCurrentSortFilterValue()
                if (it != null) {
                    viewModel.loadQuickSortFilter(it)
                    viewModel.getInitialMerchantSearchResult(it)
                }
            }
        }
    }

    private fun collectSortFilterUiModels() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.sortFilterUiModel.collect { result ->
                when (result) {
                    is Success -> {
                        applySearchFilterTab(result.data)
                    }
                    is Fail -> {
                        logErrorException(
                            result.throwable,
                            TokofoodErrorLogger.ErrorType.ERROR_LOAD_FILTER,
                            TokofoodErrorLogger.ErrorDescription.ERROR_LOAD_FILTER
                        )
                        showToasterError(result.throwable.message.orEmpty())
                    }
                }
            }
        }
    }

    private fun collectVisitables() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.visitables.collect { result ->
                removeScrollListener()
                when (result) {
                    is Success -> {
                        updateAdapterVisitables(result.data)
                    }
                    is Fail -> {
                        logErrorException(
                            result.throwable,
                            TokofoodErrorLogger.ErrorType.ERROR_LOAD_SEARCH_RESULT_PAGE,
                            TokofoodErrorLogger.ErrorDescription.ERROR_LOAD_SEARCH_RESULT_PAGE
                        )
                        showToasterError(result.throwable.message.orEmpty())
                    }
                }
                addScrollListener()
            }
        }
    }

    private fun collectUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEventFlow.collect { event ->
                when (event.state) {
                    TokofoodSearchUiEvent.EVENT_OPEN_DETAIL_BOTTOMSHEET -> {
                        onOpenDetailFilterBottomSheet(event.data)
                    }
                    TokofoodSearchUiEvent.EVENT_SUCCESS_LOAD_DETAIL_FILTER -> {
                        onSuccessLoadDetailFilter(event.data)
                    }
                    TokofoodSearchUiEvent.EVENT_SUCCESS_EDIT_PINPOINT -> {
                        refreshAddressData()
                    }
                    TokofoodSearchUiEvent.EVENT_FAILED_LOAD_DETAIL_FILTER -> {
                        onFailedLoadDetailFilter(event.throwable)
                    }
                    TokofoodSearchUiEvent.EVENT_OPEN_QUICK_SORT_BOTTOMSHEET -> {
                        onOpenQuickSortBottomSheet(event.data)
                    }
                    TokofoodSearchUiEvent.EVENT_OPEN_QUICK_FILTER_PRICE_RANGE_BOTTOMSHEET -> {
                        onOpenQuickFilterPriceRangeBottomSheet(event.data)
                    }
                    TokofoodSearchUiEvent.EVENT_OPEN_QUICK_FILTER_NORMAL_BOTTOMSHEET -> {
                        onOpenQuickFilterNormalBottomSheet(event.data)
                    }
                    TokofoodSearchUiEvent.EVENT_FAILED_LOAD_MORE -> {
                        onShowLoadMoreErrorToaster(event.throwable)
                    }
                    TokofoodSearchUiEvent.EVENT_FAILED_LOAD_SEARCH_RESULT -> {
                        event.throwable?.let {
                            logErrorException(
                                it,
                                TokofoodErrorLogger.ErrorType.ERROR_LOAD_SEARCH_RESULT_PAGE,
                                TokofoodErrorLogger.ErrorDescription.ERROR_LOAD_SEARCH_RESULT_PAGE
                            )
                        }
                    }
                    TokofoodSearchUiEvent.EVENT_FAILED_LOAD_FILTER -> {
                        event.throwable?.let {
                            logErrorException(
                                it,
                                TokofoodErrorLogger.ErrorType.ERROR_LOAD_SEARCH_RESULT_PAGE,
                                TokofoodErrorLogger.ErrorDescription.ERROR_LOAD_SEARCH_RESULT_PAGE
                            )
                        }
                    }
                    TokofoodSearchUiEvent.EVENT_FAILED_EDIT_PINPOINT -> {
                        showPinpointErrorMessage(event.throwable)
                        refreshAddressData()
                    }
                }
            }
        }
    }

    private fun collectActiveFilterCount() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.appliedFilterCount.collect { count ->
                binding?.filterTokofoodSearchResult?.indicatorCounter = count
            }
        }
    }

    private fun refreshAddressData(shouldRefreshSearchResult: Boolean = true) {
        if (isChooseAddressWidgetDataUpdated()) {
            setLocalCacheModel()
            updateAddressWidget()
        }
        if (shouldRefreshSearchResult) {
            viewModel.getInitialMerchantSearchResult(searchParameter)
        }
    }

    private fun setLocalCacheModel() {
        context?.let {
            val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
            this.localCacheModel = localCacheModel
            viewModel.setLocalCacheModel(localCacheModel)
        }
    }

    private fun isChooseAddressWidgetDataUpdated(): Boolean {
        localCacheModel?.let {
            context?.apply {
                return ChooseAddressUtils.isLocalizingAddressHasUpdated(
                    this,
                    it
                )
            }
        }
        return false
    }

    private fun applySearchFilterTab(uiModels: List<TokofoodSortFilterItemUiModel>) {
        tokofoodSearchFilterTab?.setQuickFilter(uiModels)
    }

    private fun updateAdapterVisitables(visitables: List<Visitable<*>>) {
        binding?.rvTokofoodSearchResult?.post {
            merchantResultAdapter.submitList(visitables)
        }
    }

    private fun showToaster(message: String, actionText: String) {
        view?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                actionText
            ).show()
        }
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollProductList()
            }
        }
    }

    private fun addScrollListener() {
        binding?.rvTokofoodSearchResult?.addOnScrollListener(loadMoreListener)
    }

    private fun removeScrollListener() {
        binding?.rvTokofoodSearchResult?.removeOnScrollListener(loadMoreListener)
    }

    private fun onScrollProductList() {
        val layoutManager = binding?.rvTokofoodSearchResult?.layoutManager as? LinearLayoutManager
        val lastVisibleItemIndex = layoutManager?.findLastVisibleItemPosition().orZero()
        val itemCount = layoutManager?.itemCount.orZero()
        viewModel.onScrollProductList(lastVisibleItemIndex, itemCount)
    }

    private fun onOpenDetailFilterBottomSheet(data: Any?) {
        val dynamicFilterModel = data as? DynamicFilterModel
        showDetailFilterBottomSheet(dynamicFilterModel)
    }

    private fun onSuccessLoadDetailFilter(data: Any?) {
        (data as? DynamicFilterModel)?.let { dynamicFilterModel ->
            sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)
        }
    }

    private fun onFailedLoadDetailFilter(throwable: Throwable?) {
        sortFilterBottomSheet?.dismiss()

        throwable?.let {
            logErrorException(
                it,
                TokofoodErrorLogger.ErrorType.ERROR_LOAD_FILTER,
                TokofoodErrorLogger.ErrorDescription.ERROR_LOAD_FILTER
            )
        }

        throwable?.message?.let {
            showToasterError(it)
        }
    }

    private fun onOpenQuickSortBottomSheet(data: Any?) {
        hideKeyboard()
        (data as? List<*>)?.filterIsInstance(TokofoodQuickSortUiModel::class.java)?.let { uiModels ->
            TokofoodQuickSortBottomSheet.createInstance(ArrayList(uiModels), this)
                .show(parentFragmentManager)
        }
    }

    private fun onOpenQuickFilterPriceRangeBottomSheet(data: Any?) {
        hideKeyboard()
        (data as? PriceRangeChipUiModel)?.let { uiModel ->
            TokofoodQuickPriceRangeBottomsheet.createInstance(uiModel, this)
                .show(parentFragmentManager)
        }
    }

    private fun onOpenQuickFilterNormalBottomSheet(data: Any?) {
        hideKeyboard()
        (data as? Filter)?.let { filter ->
            FilterGeneralDetailBottomSheet().show(
                parentFragmentManager,
                filter,
                this
            )
        }
    }

    private fun onShowLoadMoreErrorToaster(throwable: Throwable?) {
        val errorMessage = throwable?.message.orEmpty()
        throwable?.let {
            logErrorException(
                it,
                TokofoodErrorLogger.ErrorType.ERROR_LOAD_SEARCH_RESULT_PAGE,
                TokofoodErrorLogger.ErrorDescription.ERROR_LOAD_SEARCH_RESULT_PAGE
            )
        }
        showToasterError(errorMessage)
    }

    private fun onResultFromAddAddress(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val addressDataModel = it.getParcelableExtra<SaveAddressDataModel>(
                    NEW_ADDRESS_PARCELABLE
                )
                addressDataModel?.let { model ->
                    setupChooseAddress(model)
                }
            }
        }
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val locationPass =
                    it.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
                locationPass?.let { pass ->
                    viewModel.updatePinpoint(pass.latitude, pass.longitude)
                }
            }
        }
    }

    private fun onResultFromChangeAddress(resultCode: Int) {
        if (resultCode == CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS) {
            showToaster(
                context?.getString(com.tokopedia.tokofood.R.string.search_srp_ooc_success_change_address).orEmpty(),
                getOkayMessage()
            )
            refreshAddressData()
        }
    }

    private fun setupChooseAddress(addressDataModel: SaveAddressDataModel) {
        context?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                it,
                addressDataModel.id.toString(),
                addressDataModel.cityId.toString(),
                addressDataModel.districtId.toString(),
                addressDataModel.latitude,
                addressDataModel.longitude,
                "${addressDataModel.addressName} ${addressDataModel.receiverName}",
                addressDataModel.postalCode,
                addressDataModel.shopId.toString(),
                addressDataModel.warehouseId.toString(),
                TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(addressDataModel.warehouses),
                addressDataModel.serviceType
            )
        }
        refreshAddressData()
    }

    private fun navigateToAddAddress() {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalLogistic.ADD_ADDRESS_V2
        )
        intent.putExtra(
            ChooseAddressBottomSheet.EXTRA_REF,
            ChooseAddressBottomSheet.SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER
        )
        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
    }

    private fun navigateToAddAddressRevamp() {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalLogistic.ADD_ADDRESS_V3
        )
        intent.putExtra(
            ChooseAddressBottomSheet.EXTRA_REF,
            ChooseAddressBottomSheet.SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER
        )
        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
        intent.putExtra(ApplinkConstInternalLogistic.PARAM_SOURCE, AddEditAddressSource.TOKOFOOD.source)
        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
    }

    private fun navigateToSetPinpoint() {
        view?.let {
            MapsAvailabilityHelper.onMapsAvailableState(it) {
                val locationPass = LocationPass().apply {
                    latitude = TOTO_LATITUDE
                    longitude = TOTO_LONGITUDE
                }
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
                val bundle = Bundle().apply {
                    putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
                    putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
                }
                intent.putExtras(bundle)
                startActivityForResult(intent, REQUEST_CODE_SET_PINPOINT)
            }
        }
    }

    private fun navigateToChangeAddress() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS).apply {
            putExtra(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true)
            putExtra(ApplinkConstInternalLogistic.PARAM_SOURCE, ManageAddressSource.TOKOFOOD.source)
        }
        startActivityForResult(intent, REQUEST_CODE_CHANGE_ADDRESS)
    }

    private fun showDetailFilterBottomSheet(dynamicFilterModel: DynamicFilterModel?) {
        if (!isAdded) return
        if (sortFilterBottomSheet == null) {
            val customTitle = context?.getString(com.tokopedia.tokofood.R.string.search_srp_filter_title).orEmpty()
            sortFilterBottomSheet = SortFilterBottomSheet.createInstance(customTitle)
        }
        hideKeyboard()
        sortFilterBottomSheet?.show(
            parentFragmentManager,
            searchParameter,
            dynamicFilterModel,
            this
        )
    }

    private fun showQuickSortBottomSheet(sort: List<Sort>) {
        viewModel.showQuickSortBottomSheet(sort)
    }

    private fun showQuickFilterBottomSheet(filter: Filter) {
        viewModel.showQuickFilterBottomSheet(filter)
    }

    private fun showDetailFilterApplyButton() {
        sortFilterBottomSheet?.setResultCountText(getApplyButtonText())
    }

    private fun getApplyButtonText(): String {
        return context?.getString(com.tokopedia.tokofood.R.string.search_srp_filter_apply).orEmpty()
    }

    private fun goToMerchantPage(applink: String) {
        TokofoodRouteManager.routePrioritizeInternal(context, applink)
    }

    private fun goToDiscoverySearchPage() {
        context?.let {
            val discoveryApplink =
                UriUtil.buildUriAppendParams(
                    ApplinkConstInternalDiscovery.SEARCH_RESULT,
                    mapOf(SearchApiConst.Q to keyword)
                )
            RouteManager.route(it, discoveryApplink)
        }
    }

    private fun updateAddressWidget() {
        binding?.addressTokofoodSearchResult?.updateWidget()
    }

    private fun hideKeyboard() {
        try {
            KeyboardHandler.hideSoftKeyboard(activity)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun showPinpointErrorMessage(throwable: Throwable?) {
        val errorMessage = throwable?.message
            ?: context?.getString(com.tokopedia.tokofood.R.string.search_srp_ooc_failed_edit_pinpoint)
                .orEmpty()
        showToasterError(errorMessage)
    }

    private fun removeAllScrollListener() {
        itemsScrollChangedListenerList.forEach {
            view?.viewTreeObserver?.removeOnScrollChangedListener(it)
        }
    }

    private fun getDestinationId(): String {
        return localCacheModel?.district_id.orEmpty()
    }

    private fun getCurrentSortFilterValue(): String {
        return searchParameter?.entries?.filter { it.key != SearchApiConst.Q }
            ?.joinToString(AND_SEPARATOR) { it.toString() }.orEmpty()
    }

    private fun getOkayMessage(): String = context?.getString(com.tokopedia.tokofood.R.string.search_srp_ooc_okay).orEmpty()

    private fun logErrorException(
        throwable: Throwable,
        errorType: String,
        description: String
    ) {
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.SEARCH,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            description
        )
    }

    private fun navigateToNewFragment(fragment: Fragment) {
        navigateToNewFragment(fragment)
    }

    companion object {
        private const val SOURCE = "tokofood"

        private const val REQUEST_CODE_SET_PINPOINT = 100
        private const val REQUEST_CODE_ADD_ADDRESS = 101
        private const val REQUEST_CODE_CHANGE_ADDRESS = 102

        private const val NEW_ADDRESS_PARCELABLE = "EXTRA_ADDRESS_NEW"

        private const val TOTO_LATITUDE = "-6.2216771"
        private const val TOTO_LONGITUDE = "106.8184023"

        private const val AND_SEPARATOR = "&"
    }
}
