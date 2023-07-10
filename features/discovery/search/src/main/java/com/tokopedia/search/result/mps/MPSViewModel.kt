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
import com.tokopedia.discovery.common.utils.MpsLocalCache
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.search.result.mps.addtocart.AddToCartViewModel
import com.tokopedia.search.result.mps.analytics.GeneralSearchTrackingMPS
import com.tokopedia.search.result.mps.analytics.MPSTracking
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.filter.bottomsheet.BottomSheetFilterViewModel
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterDataView
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterViewModel
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetProductDataView
import com.tokopedia.search.utils.ChooseAddressWrapper
import com.tokopedia.search.utils.mvvm.SearchViewModel
import com.tokopedia.search.utils.toSearchParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
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
    private val mpsLocalCache: MpsLocalCache,
    private val userSession: UserSessionInterface,
    private val mpsTracking: MPSTracking,
) : ViewModel(),
    SearchViewModel<MPSState>,
    QuickFilterViewModel,
    BottomSheetFilterViewModel,
    AddToCartViewModel {

    private val _stateFlow = MutableStateFlow(mpsState)

    override val stateFlow: StateFlow<MPSState>
        get() = _stateFlow.asStateFlow()

    private val mpsState: MPSState
        get() = stateFlow.value

    private val chooseAddressModel
        get() = mpsState.chooseAddressModel

    override val paramater: Map<String, String>
        get() = stateFlow.value.parameter

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
            onSuccess = { mpsModel ->
                trackGeneralSearchAttempt(mpsModel)
                markFirstMpsSuccess(mpsModel)
                updateState { it.success(mpsModel) }
            },
            onError = { throwable -> updateState { it.error(throwable) } },
            useCaseRequestParams = mpsUseCaseRequestParams(),
        )
    }

    private fun markFirstMpsSuccess(mpsModel: MPSModel) {
        // should change to check response code when it become available
        if (mpsModel.shopList.isNotEmpty() && !mpsLocalCache.isFirstMpsSuccess()) {
            mpsLocalCache.markFirstMpsSuccess()
        }
    }

    private fun mpsUseCaseRequestParams(): RequestParams = RequestParams.create().apply {
        putAll(mandatoryParams())
        putString(START, mpsState.startFrom.toString())
        putString(ROWS, DEFAULT_VALUE_OF_PARAMETER_ROWS)
    }

    private fun mandatoryParams(): Map<String, String> = mpsState.parameter +
        chooseAddressParams() +
        uniqueIdParams() +
        deviceParams()

    private fun uniqueIdParams() = mapOf(
        SearchApiConst.UNIQUE_ID to getUniqueId(),
    )

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn) AuthHelper.getMD5Hash(userSession.userId)
        else AuthHelper.getMD5Hash(userSession.deviceId)
    }

    private fun deviceParams() = mapOf(
        SearchApiConst.DEVICE to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
    )

    private fun chooseAddressParams() = chooseAddressModel?.toSearchParams() ?: mapOf()

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

    override fun onQuickFilterSelected(quickFilterDataView: QuickFilterDataView) {
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

    override fun onAddToCartMessageDismissed() {
        updateState { it.addToCartMessageDismissed() }
    }

    override fun openBottomSheetFilter() {
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

    override fun closeBottomSheetFilter() {
        updateState { it.closeBottomSheetFilter() }
    }

    override fun applyFilter(parameter: Map<String, String>) {
        updateState { it.applyFilter(parameter).reload() }

        multiProductSearch()
    }

    fun resetFilter() {
        updateState { it.resetFilter().reload() }

        multiProductSearch()
    }

    private fun trackGeneralSearchAttempt(mpsModel: MPSModel) {
        val generalSearchTracking = GeneralSearchTrackingMPS.create(
            mpsModel,
            mpsState.parameter,
            userSession.userId,
        )
        mpsTracking.trackGeneralSearch(generalSearchTracking)
    }
}
