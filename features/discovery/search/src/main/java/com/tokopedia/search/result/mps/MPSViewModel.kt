package com.tokopedia.search.result.mps

import androidx.lifecycle.ViewModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_ROWS
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ROWS
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.START
import com.tokopedia.discovery.common.constants.SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_SHOP_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.MPS.MPS_FIRST_PAGE_USE_CASE
import com.tokopedia.discovery.common.constants.SearchConstant.MPS.MPS_LOAD_MORE_USE_CASE
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetProductDataView
import com.tokopedia.search.utils.ChooseAddressWrapper
import com.tokopedia.search.utils.mvvm.SearchViewModel
import com.tokopedia.search.utils.toSearchParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

class MPSViewModel @Inject constructor(
    mpsState: MPSState = MPSState(),
    @param:Named(MPS_FIRST_PAGE_USE_CASE)
    private val mpsFirstPageUseCase: UseCase<MPSModel>,
    @param:Named(MPS_LOAD_MORE_USE_CASE)
    private val mpsLoadMoreUseCase: UseCase<MPSModel>,
    private val addToCartUseCase: AddToCartUseCase,
    @param:Named(GET_DYNAMIC_FILTER_SHOP_USE_CASE)
    private val getDynamicFilterUseCase: UseCase<DynamicFilterModel>,
    private val chooseAddressWrapper: ChooseAddressWrapper,
): ViewModel(), SearchViewModel<MPSState> {

    private val _stateFlow = MutableStateFlow(mpsState)

    override val stateFlow: StateFlow<MPSState>
        get() = _stateFlow.asStateFlow()

    private val mpsState: MPSState
        get() = stateFlow.value

    private val chooseAddressModel
        get() = mpsState.chooseAddressModel

    private fun updateState(function: (MPSState) -> MPSState) {
        _stateFlow.update(function)
    }

    fun onViewCreated() {
        updateChooseAddress()
        onViewReloadData()
    }

    private fun updateChooseAddress() {
        updateState { it.chooseAddress(chooseAddressWrapper.getChooseAddressData()) }
    }

    fun onViewReloadData() {
        updateState { it.reload() }

        multiProductSearch()
    }

    private fun multiProductSearch() {
        mpsFirstPageUseCase.execute(
            onSuccess = { mpsModel -> updateState { it.success(mpsModel) } },
            onError = { throwable -> updateState { it.error(throwable) } },
            useCaseRequestParams = mpsUseCaseRequestParams(),
        )
    }

    private fun mpsUseCaseRequestParams(): RequestParams = RequestParams.create().apply {
        putAll(mandatoryParams())
        putString(START, mpsState.startFrom.toString())
        putString(ROWS, DEFAULT_VALUE_OF_PARAMETER_ROWS)
    }

    private fun mandatoryParams(): Map<String, String> =
        mpsState.parameter + chooseAddressParams()

    private fun chooseAddressParams() =
        chooseAddressModel?.toSearchParams() ?: mapOf()

    fun onLocalizingAddressSelected() {
        updateChooseAddress()
        onViewReloadData()
    }

    fun onViewResumed() {
        if (!isChooseAddressUpdated()) return

        updateChooseAddress()
        onViewReloadData()
    }

    private fun isChooseAddressUpdated(): Boolean {
        val chooseAddressModel = chooseAddressModel ?: return false

        return chooseAddressWrapper.isChooseAddressUpdated(chooseAddressModel)
    }

    fun onQuickFilterSelected(quickFilterDataView: QuickFilterDataView) {
        quickFilterDataView.firstOption ?: return

        updateState { it.applyQuickFilter(quickFilterDataView) }

        onViewReloadData()
    }

    fun onViewLoadMore() {
        if (!mpsState.hasNextPage) return

        updateState { it.loadMore() }

        mpsLoadMoreUseCase.execute(
            onSuccess = { mpsModel -> updateState { it.successLoadMore(mpsModel) } },
            onError = { throwable -> updateState { it.errorLoadMore(throwable) } },
            useCaseRequestParams = mpsUseCaseRequestParams()
        )
    }

    fun onAddToCart(
        mpsShopWidget: MPSShopWidgetDataView,
        mpsShopWidgetProduct: MPSShopWidgetProductDataView,
    ) {
        addToCartUseCase.run {
            setParams(AddToCartUseCase.getMinimumParams(
                mpsShopWidgetProduct.id,
                mpsShopWidget.id,
                mpsShopWidgetProduct.minOrder,
            ))

            execute(
                { addToCartDataModel -> updateState { it.successAddToCart(addToCartDataModel) } },
                { throwable -> updateState { it.errorAddToCart(throwable) } }
            )
        }
    }

    fun onAddToCartMessageDismissed() {
        updateState { it.addToCartMessageDismissed() }
    }

    fun openBottomSheetFilter() {
        updateState { it.openBottomSheetFilter() }

        if (mpsState.bottomSheetFilterModel != null) return

        getDynamicFilterUseCase.execute(
            onSuccess = { dynamicFilterModel ->
                updateState { it.setBottomSheetFilterModel(dynamicFilterModel) }
            },
            onError = { },
            useCaseRequestParams = mpsDynamicFilterRequestParams()
        )
    }

    private fun mpsDynamicFilterRequestParams() = RequestParams.create().apply {
        putAll(mandatoryParams())
        putString(SearchApiConst.SOURCE, SearchApiConst.MPS)
    }

    fun closeBottomSheetFilter() {
        updateState { it.closeBottomSheetFilter() }
    }

    fun applyFilter(parameter: Map<String, String>) {
        updateState { it.applyFilter(parameter).reload() }

        multiProductSearch()
    }
}
