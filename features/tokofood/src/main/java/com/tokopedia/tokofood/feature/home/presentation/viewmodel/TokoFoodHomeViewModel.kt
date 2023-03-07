package com.tokopedia.tokofood.feature.home.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.MERCHANT_TITLE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutItemState
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodInputPinPoint
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.addErrorState
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.addLoadingIntoList
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.addMerchantTitle
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.addNoAddressState
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.addNoPinPointState
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.addProgressBar
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.getVisitableId
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.mapCategoryLayoutList
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.mapDynamicIcons
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.mapHomeLayoutList
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.mapTickerData
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.mapUSPData
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.removeItem
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.removeProgressBar
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeMapper.setStateToLoading
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeDynamicChannelUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeDynamicIconsUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeTickerUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodHomeUSPUseCase
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.feature.home.presentation.sharedpref.TokofoodHomeSharedPref
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLayoutUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeTickerUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodUiState
import com.tokopedia.tokofood.feature.home.presentation.uimodel.UiEvent.STATE_ERROR
import com.tokopedia.tokofood.feature.home.presentation.uimodel.UiEvent.STATE_FETCH_COMPONENT_DATA
import com.tokopedia.tokofood.feature.home.presentation.uimodel.UiEvent.STATE_FETCH_DYNAMIC_CHANNEL_DATA
import com.tokopedia.tokofood.feature.home.presentation.uimodel.UiEvent.STATE_FETCH_LOAD_MORE
import com.tokopedia.tokofood.feature.home.presentation.uimodel.UiEvent.STATE_REMOVE_TICKER
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class TokoFoodHomeViewModel @Inject constructor(
    private val tokoFoodDynamicChanelUseCase: TokoFoodHomeDynamicChannelUseCase,
    private val tokoFoodHomeUSPUseCase: TokoFoodHomeUSPUseCase,
    private val tokoFoodHomeDynamicIconsUseCase: TokoFoodHomeDynamicIconsUseCase,
    private val tokoFoodHomeTickerUseCase: TokoFoodHomeTickerUseCase,
    private val tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase,
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val eligibleForAddressUseCase: EligibleForAddressUseCase,
    private val searchCoachmarkSharedPref: TokofoodHomeSharedPref,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _inputState = MutableSharedFlow<TokoFoodUiState>(Int.ONE)
    private val _inputPinPointState = MutableSharedFlow<TokoFoodInputPinPoint>(Int.ONE)

    private val _flowChooseAddress = MutableSharedFlow<Result<GetStateChosenAddressResponse>>(Int.ONE)
    private val _flowEligibleForAnaRevamp = MutableSharedFlow<Result<EligibleForAddressFeature>>(Int.ONE)
    private val _flowShouldShowSearchCoachMark = MutableSharedFlow<Boolean>(Int.ONE)

    init {
        _inputState.tryEmit(TokoFoodUiState())
    }

    val flowLayoutList: SharedFlow<Result<TokoFoodListUiModel>> =
        _inputState.flatMapConcat { inputState ->
            getFlowHome(inputState).catch {
                if (inputState.uiState == STATE_FETCH_DYNAMIC_CHANNEL_DATA)  emit(Fail(it))
                else {
                    removeMerchantMainTitle()
                    emit(getRemovalProgressBar())
                }
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowUpdatePinPointState: SharedFlow<Result<Boolean>> =
        _inputPinPointState.flatMapConcat {
            flow {
                emit(updatePinPoin(it.addressId, it.latitude, it.longitude))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowEligibleForAnaRevamp: SharedFlow<Result<EligibleForAddressFeature>> = _flowEligibleForAnaRevamp
    val flowChooseAddress: SharedFlow<Result<GetStateChosenAddressResponse>> = _flowChooseAddress
    val flowShouldShowSearchCoachMark: SharedFlow<Boolean> = _flowShouldShowSearchCoachMark

    private val homeLayoutItemList = mutableListOf<TokoFoodItemUiModel>()
    private var pageKey = INITIAL_PAGE_KEY_MERCHANT
    private var hasTickerBeenRemoved = false
    var isAddressManuallyUpdated = false

    companion object {
        private const val INITIAL_PAGE_KEY_MERCHANT = "0"
        private const val EMPTY_LOCATION = "0.0"
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
        private const val SOURCE = "tokofood"
    }

    private fun getFlowHome(inputState: TokoFoodUiState): Flow<Result<TokoFoodListUiModel>> {
        return flow {
            when (inputState.uiState) {
                STATE_FETCH_DYNAMIC_CHANNEL_DATA -> {
                    emit(getLoadingState())
                    when {
                        hasNoAddress(inputState.isLoggedIn, inputState.localCacheModel) -> {
                            if (isAddressManuallyUpdate()){
                                emit(getNoAddressState())
                            } else {
                                getChooseAddress(SOURCE)
                            }
                        }
                        hasNoPinPoin(inputState.isLoggedIn, inputState.localCacheModel) -> {
                            if (isAddressManuallyUpdate()){
                                emit(getNoPinPointState())
                            } else {
                                getChooseAddress(SOURCE)
                            }
                        }
                        else -> emit(getHomeLayout(inputState.localCacheModel))
                    }
                }
                STATE_FETCH_COMPONENT_DATA -> {
                    homeLayoutItemList.filter { it.state == TokoFoodLayoutItemState.NOT_LOADED }
                        .forEach {
                            emit(getLayoutComponentData(it, inputState.localCacheModel))
                        }
                }
                STATE_FETCH_LOAD_MORE -> {
                    emit(getProgressBar())
                    emit(getMerchantList(inputState.localCacheModel))
                }
                STATE_ERROR -> {
                    emit(getErrorState(inputState.throwable))
                }
                STATE_REMOVE_TICKER -> {
                    emit(getRemovalTickerWidget(inputState.visitableId))
                }
            }
        }
    }

    fun setUpdatePinPoint(addressId: String, latitude: String, longitude: String) {
        _inputPinPointState.tryEmit(
            TokoFoodInputPinPoint(addressId, latitude, longitude)
        )
    }

    fun setErrorState(throwable: Throwable) {
        _inputState.tryEmit(TokoFoodUiState(uiState = STATE_ERROR, throwable = throwable))
    }

    fun setRemoveTicker(id: String) {
        _inputState.tryEmit(TokoFoodUiState(uiState = STATE_REMOVE_TICKER, visitableId = id))
    }

    fun setLayoutComponentData(localCacheModel: LocalCacheModel?) {
        localCacheModel?.let {
            _inputState.tryEmit(
                TokoFoodUiState(
                    uiState = STATE_FETCH_COMPONENT_DATA,
                    localCacheModel = it
                )
            )
        }
    }

    fun setMerchantList(localCacheModel: LocalCacheModel?) {
        localCacheModel?.let {
            _inputState.tryEmit(
                TokoFoodUiState(
                    uiState = STATE_FETCH_LOAD_MORE,
                    localCacheModel = it
                )
            )
        }
    }

    fun setHomeLayout(localCacheModel: LocalCacheModel?, isLoggedIn: Boolean) {
        localCacheModel?.let {
            _inputState.tryEmit(
                TokoFoodUiState(
                    uiState = STATE_FETCH_DYNAMIC_CHANNEL_DATA,
                    isLoggedIn = isLoggedIn,
                    localCacheModel = localCacheModel
                )
            )
        }
    }

    fun checkUserEligibilityForAnaRevamp() {
        eligibleForAddressUseCase.eligibleForAddressFeature(
            {
                setEligibleForAnaRevamp(it.eligibleForRevampAna)
            },
            {
                setEligibleForAnaRevamp(it)
            },
            AddressConstant.ANA_REVAMP_FEATURE_ID
        )
    }

    fun getChooseAddress(source: String) {
        isAddressManuallyUpdated = true
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress({
            setFlowChooseAddress(it)
        }, {
            setFlowChooseAddress(it)
        }, source)
    }

    fun getLoadingState(): Result<TokoFoodListUiModel> {
        setPageKey(INITIAL_PAGE_KEY_MERCHANT)
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = Success(
            TokoFoodListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodLayoutState.LOADING
            )
        )
        return data
    }

    fun getNoPinPointState(): Result<TokoFoodListUiModel> {
        homeLayoutItemList.clear()
        homeLayoutItemList.addNoPinPointState()
        val data = Success(
            TokoFoodListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodLayoutState.HIDE
            )
        )
        return data
    }

    fun getNoAddressState(): Result<TokoFoodListUiModel> {
        homeLayoutItemList.clear()
        homeLayoutItemList.addNoAddressState()
        val data = Success(
            TokoFoodListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodLayoutState.HIDE
            )
        )
        return data
    }

    fun getErrorState(throwable: Throwable): Result<TokoFoodListUiModel> {
        homeLayoutItemList.clear()
        homeLayoutItemList.addErrorState(throwable)
        val data = Success(
            TokoFoodListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodLayoutState.HIDE
            )
        )
        return data
    }

    fun getProgressBar(): Result<TokoFoodListUiModel> {
        homeLayoutItemList.addProgressBar()
        val data = TokoFoodListUiModel(
            getHomeVisitableList(),
            TokoFoodLayoutState.UPDATE
        )
        return Success(data)
    }

    fun getRemovalTickerWidget(id: String): Result<TokoFoodListUiModel> {
        hasTickerBeenRemoved = true
        homeLayoutItemList.removeItem(id)

        val data = Success(
            TokoFoodListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodLayoutState.UPDATE
            )
        )

        return data
    }

    suspend fun updatePinPoin(addressId: String, latitude: String, longitude: String): Result<Boolean> {
        val isSuccess = withContext(dispatchers.io) {
            keroEditAddressUseCase.execute(addressId, latitude, longitude)
        }
        return Success(isSuccess)
    }

    private suspend fun getHomeLayout(localCacheModel: LocalCacheModel): Result<TokoFoodListUiModel> {
        homeLayoutItemList.clear()

        val homeLayoutResponse = withContext(dispatchers.io) {
            tokoFoodDynamicChanelUseCase.execute(localCacheModel)
        }

        homeLayoutItemList.mapHomeLayoutList(
            homeLayoutResponse.response.data,
            hasTickerBeenRemoved
        )

        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.SHOW
        )
        return Success(data)
    }

    private suspend fun getLayoutComponentData(
        uiModel: TokoFoodItemUiModel,
        localCacheModel: LocalCacheModel?
    ): Result<TokoFoodListUiModel> {

        homeLayoutItemList.setStateToLoading(uiModel)

        if (uiModel.layout is TokoFoodHomeLayoutUiModel) {
            getTokoFoodHomeComponent(uiModel.layout, localCacheModel)
        }

        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.UPDATE
        )

        return Success(data)
    }

    private suspend fun getMerchantList(localCacheModel: LocalCacheModel): Result<TokoFoodListUiModel> {
        val categoryResponse = withContext(dispatchers.io) {
            tokoFoodMerchantListUseCase.execute(
                localCacheModel = localCacheModel,
                pageKey = pageKey
            )
        }

        if (isInitialPageKey()) {
            homeLayoutItemList.addMerchantTitle()
        }

        setPageKey(categoryResponse.data.nextPageKey)
        homeLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
        homeLayoutItemList.removeProgressBar()
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.LOAD_MORE
        )
        return Success(data)
    }

    private fun getRemovalProgressBar(): Result<TokoFoodListUiModel> {
        homeLayoutItemList.removeProgressBar()
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.LOAD_MORE
        )
        return Success(data)
    }

    fun onScrollProductList(
        containsLastItemIndex: Int,
        itemCount: Int,
        localCacheModel: LocalCacheModel
    ) {
        if (shouldLoadMore(containsLastItemIndex, itemCount)) {
            setMerchantList(localCacheModel = localCacheModel)
        }
    }

    fun isShownEmptyState(): Boolean {
        val layoutList = homeLayoutItemList.toMutableList()
        val isError = layoutList.firstOrNull { it.layout is TokoFoodErrorStateUiModel } != null
        val isEmptyStateShown =
            layoutList.firstOrNull { it.layout is TokoFoodHomeEmptyStateLocationUiModel } != null
        return isEmptyStateShown || isError
    }

    fun setPageKey(pageNew: String) {
        pageKey = pageNew
    }

    fun checkForSearchCoachMark() {
        val hasSearchCoachMarkShown = searchCoachmarkSharedPref.getHasSearchCoachmarkShown()
        _flowShouldShowSearchCoachMark.tryEmit(!hasSearchCoachMarkShown)
    }

    fun setSearchCoachMarkHasShown() {
        searchCoachmarkSharedPref.setHasSearchCoachmarkShown(true)
    }

    private suspend fun getTokoFoodHomeComponent(
        item: TokoFoodHomeLayoutUiModel,
        localCacheModel: LocalCacheModel?
    ) {
        when (item) {
            is TokoFoodHomeTickerUiModel -> getTickerDataAsync(item, localCacheModel).await()
            is TokoFoodHomeUSPUiModel -> getUSPDataAsync(item).await()
            is TokoFoodHomeIconsUiModel -> getIconListDataAsync(item).await()
            else -> removeUnsupportedLayout(item)
        }
    }

    private suspend fun getTickerDataAsync(
        item: TokoFoodHomeTickerUiModel,
        localCacheModel: LocalCacheModel?
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val tickerData = tokoFoodHomeTickerUseCase.execute(localCacheModel)
            homeLayoutItemList.mapTickerData(item, tickerData)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getUSPDataAsync(item: TokoFoodHomeUSPUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val uspData = tokoFoodHomeUSPUseCase.execute()
            homeLayoutItemList.mapUSPData(item, uspData)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getIconListDataAsync(item: TokoFoodHomeIconsUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val dynamicIcons = tokoFoodHomeDynamicIconsUseCase.execute(item.widgetParam)
            homeLayoutItemList.mapDynamicIcons(item, dynamicIcons)
        }) {
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private fun getHomeVisitableList(): List<Visitable<*>> {
        return homeLayoutItemList.mapNotNull { it.layout }
    }

    private fun removeUnsupportedLayout(item: TokoFoodHomeLayoutUiModel) {
        homeLayoutItemList.removeItem(item.getVisitableId())
    }

    private fun isInitialPageKey(): Boolean {
        return pageKey.equals(INITIAL_PAGE_KEY_MERCHANT)
    }

    private fun removeMerchantMainTitle() {
        if (isInitialPageKey()) {
            homeLayoutItemList.removeItem(MERCHANT_TITLE)
        }
    }

    private fun shouldLoadMore(containsLastItemIndex: Int, itemCount: Int): Boolean {
        val lastItemIndex = itemCount - Int.ONE
        val scrolledToLastItem = (containsLastItemIndex == lastItemIndex
                && containsLastItemIndex.isMoreThanZero())
        val hasNextPage = pageKey.isNotEmpty()
        val layoutList = homeLayoutItemList.toMutableList()
        val isLoading = layoutList.firstOrNull { it.layout is TokoFoodProgressBarUiModel } != null
        val isEmptyStateShown =
            layoutList.firstOrNull { it.layout is TokoFoodHomeEmptyStateLocationUiModel } != null
        val isError = layoutList.firstOrNull { it.layout is TokoFoodErrorStateUiModel } != null

        return scrolledToLastItem && hasNextPage && !isLoading && !isEmptyStateShown && !isError
    }

    private fun setFlowChooseAddress(response: GetStateChosenAddressResponse) {
        launch {
            _flowChooseAddress.emit(Success(response))
        }
    }

    private fun setFlowChooseAddress(throwable: Throwable) {
        launch {
            _flowChooseAddress.emit(Fail(throwable))
        }
    }

    private fun setEligibleForAnaRevamp(response: EligibleForAddressFeature) {
        launch {
            _flowEligibleForAnaRevamp.emit(Success(response))
        }
    }

    private fun setEligibleForAnaRevamp(throwable: Throwable) {
        launch {
            _flowEligibleForAnaRevamp.emit(Fail(throwable))
        }
    }

    private fun hasNoAddress(isLoggedIn: Boolean, localCacheModel: LocalCacheModel): Boolean {
        return isLoggedIn &&
                (localCacheModel.address_id.isEmpty() ||
                        localCacheModel.address_id == "0")
    }

    private fun hasNoPinPoin(isLoggedIn: Boolean, localCacheModel: LocalCacheModel): Boolean {
        return isLoggedIn &&
                (localCacheModel.lat.isEmpty() ||
                        localCacheModel.long.isEmpty() ||
                localCacheModel.lat.equals(EMPTY_LOCATION) ||
                        localCacheModel.long.equals(EMPTY_LOCATION))
    }

    private fun isAddressManuallyUpdate(): Boolean = isAddressManuallyUpdated
}
