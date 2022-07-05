package com.tokopedia.tokofood.feature.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLayoutUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeTickerUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoFoodHomeViewModel @Inject constructor(
    private val tokoFoodDynamicChanelUseCase: TokoFoodHomeDynamicChannelUseCase,
    private val tokoFoodHomeUSPUseCase: TokoFoodHomeUSPUseCase,
    private val tokoFoodHomeDynamicIconsUseCase: TokoFoodHomeDynamicIconsUseCase,
    private val tokoFoodHomeTickerUseCase: TokoFoodHomeTickerUseCase,
    private val tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase,
    private val keroEditAddressUseCase: KeroEditAddressUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val eligibleForAddressUseCase: EligibleForAddressUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val layoutList: LiveData<Result<TokoFoodListUiModel>>
        get() = _homeLayoutList
    val updatePinPointState: LiveData<Boolean>
        get() = _updatePinPointState
    val errorMessage:LiveData<String>
        get() = _errorMessage
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress
    val eligibleForAnaRevamp: LiveData<Result<EligibleForAddressFeature>>
        get() = _eligibleForAnaRevamp

    private val _homeLayoutList = MutableLiveData<Result<TokoFoodListUiModel>>()
    private val _updatePinPointState = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    private val _eligibleForAnaRevamp = MutableLiveData<Result<EligibleForAddressFeature>>()

    private val homeLayoutItemList = mutableListOf<TokoFoodItemUiModel>()
    private var pageKey = INITIAL_PAGE_KEY_MERCHANT
    private var hasTickerBeenRemoved = false
    var isAddressManuallyUpdated = false

    companion object {
        private const val INITIAL_PAGE_KEY_MERCHANT = "0"
    }

    fun updatePinPoin(addressId: String, latitude: String, longitude: String) {
        launchCatchError(block = {
            val isSuccess = withContext(dispatchers.io) {
                keroEditAddressUseCase.execute(addressId, latitude, longitude)
            }
            _updatePinPointState.postValue(isSuccess)
        }){
            _errorMessage.postValue(it.message)
        }
    }

    fun checkUserEligibilityForAnaRevamp() {
        eligibleForAddressUseCase.eligibleForAddressFeature(
            {
                _eligibleForAnaRevamp.postValue(Success(it.eligibleForRevampAna))
            },
            {
                _eligibleForAnaRevamp.postValue(Fail(it))
            },
            AddressConstant.ANA_REVAMP_FEATURE_ID
        )
    }

    fun getChooseAddress(source: String){
        isAddressManuallyUpdated = true
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress( {
            _chooseAddress.postValue(Success(it))
        },{
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun showLoadingState() {
        setPageKey(INITIAL_PAGE_KEY_MERCHANT)
        homeLayoutItemList.clear()
        homeLayoutItemList.addLoadingIntoList()
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.LOADING
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun showNoPinPointState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addNoPinPointState()
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.HIDE
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun showNoAddressState() {
        homeLayoutItemList.clear()
        homeLayoutItemList.addNoAddressState()
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.HIDE
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun showErrorState(throwable: Throwable) {
        homeLayoutItemList.clear()
        homeLayoutItemList.addErrorState(throwable)
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.HIDE
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun showProgressBar() {
        homeLayoutItemList.addProgressBar()
        val data = TokoFoodListUiModel(
            getHomeVisitableList(),
            TokoFoodLayoutState.UPDATE
        )
        _homeLayoutList.postValue(Success(data))
    }

    fun removeTickerWidget(id: String) {
        launch(block = {
            hasTickerBeenRemoved = true
            homeLayoutItemList.removeItem(id)

            val data = TokoFoodListUiModel(
                items = getHomeVisitableList(),
                state = TokoFoodLayoutState.UPDATE
            )

            _homeLayoutList.postValue(Success(data))
        })
    }

    fun getHomeLayout(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {
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

            _homeLayoutList.postValue(Success(data))
            }){
            _homeLayoutList.postValue(Fail(it))
        }
    }

    fun getLayoutComponentData(localCacheModel: LocalCacheModel?){
        launch {
            homeLayoutItemList.filter { it.state == TokoFoodLayoutItemState.NOT_LOADED }.forEach {
                homeLayoutItemList.setStateToLoading(it)

                if (it.layout is TokoFoodHomeLayoutUiModel) {
                    getTokoFoodHomeComponent(it.layout, localCacheModel)
                }

                val data = TokoFoodListUiModel(
                    items = getHomeVisitableList(),
                    state = TokoFoodLayoutState.UPDATE
                )

                _homeLayoutList.postValue(Success(data))
            }
        }
    }

    fun onScrollProductList(containsLastItemIndex: Int, itemCount: Int, localCacheModel: LocalCacheModel) {
        if(shouldLoadMore(containsLastItemIndex, itemCount)) {
            showProgressBar()
            getMerchantList(localCacheModel = localCacheModel)
        }
    }

    fun isShownEmptyState(): Boolean {
        val layoutList = homeLayoutItemList.toMutableList()
        val isEmptyStateShown = layoutList.firstOrNull { it.layout is TokoFoodHomeEmptyStateLocationUiModel } != null
        return isEmptyStateShown
    }

    fun setPageKey(pageNew:String) {
        pageKey = pageNew
    }

    private fun getMerchantList(localCacheModel: LocalCacheModel) {
        launchCatchError(block = {
            val categoryResponse = withContext(dispatchers.io) {
                tokoFoodMerchantListUseCase.execute(
                    localCacheModel = localCacheModel,
                    pageKey = pageKey)
            }

            if (isInitialPageKey()){
                homeLayoutItemList.addMerchantTitle()
            }

            setPageKey(categoryResponse.data.nextPageKey)
            homeLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
            merchantListUpdate()
        }){
            removeMerchantMainTitle()
            merchantListUpdate()
        }
    }

    private suspend fun getTokoFoodHomeComponent(item: TokoFoodHomeLayoutUiModel, localCacheModel: LocalCacheModel?) {
        when (item) {
            is TokoFoodHomeTickerUiModel -> getTickerDataAsync(item, localCacheModel).await()
            is TokoFoodHomeUSPUiModel -> getUSPDataAsync(item).await()
            is TokoFoodHomeIconsUiModel -> getIconListDataAsync(item).await()
            else -> removeUnsupportedLayout(item)
        }
    }

    private suspend fun getTickerDataAsync(item: TokoFoodHomeTickerUiModel, localCacheModel: LocalCacheModel?): Deferred<Unit?> {
        return asyncCatchError(block = {
            val tickerData = tokoFoodHomeTickerUseCase.execute(localCacheModel)
            homeLayoutItemList.mapTickerData(item, tickerData)
        }){
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getUSPDataAsync(item: TokoFoodHomeUSPUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val uspData = tokoFoodHomeUSPUseCase.execute()
            homeLayoutItemList.mapUSPData(item, uspData)
        }){
            homeLayoutItemList.removeItem(item.id)
        }
    }

    private suspend fun getIconListDataAsync(item: TokoFoodHomeIconsUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val dynamicIcons = tokoFoodHomeDynamicIconsUseCase.execute(item.widgetParam)
            homeLayoutItemList.mapDynamicIcons(item, dynamicIcons)
        }){
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

    private fun removeMerchantMainTitle(){
        if (isInitialPageKey()){
            homeLayoutItemList.removeItem(MERCHANT_TITLE)
        }
    }

    private fun merchantListUpdate() {
        homeLayoutItemList.removeProgressBar()
        val data = TokoFoodListUiModel(
            items = getHomeVisitableList(),
            state = TokoFoodLayoutState.LOAD_MORE
        )

        _homeLayoutList.postValue(Success(data))
    }

    private fun shouldLoadMore(containsLastItemIndex: Int, itemCount: Int): Boolean {
        val lastItemIndex = itemCount - Int.ONE
        val scrolledToLastItem = (containsLastItemIndex == lastItemIndex
                && containsLastItemIndex.isMoreThanZero())
        val hasNextPage = pageKey.isNotEmpty()
        val layoutList = homeLayoutItemList.toMutableList()
        val isLoading = layoutList.firstOrNull { it.layout is TokoFoodProgressBarUiModel } != null
        val isEmptyStateShown = layoutList.firstOrNull { it.layout is TokoFoodHomeEmptyStateLocationUiModel } != null
        val isError = layoutList.firstOrNull { it.layout is TokoFoodErrorStateUiModel } != null

        return scrolledToLastItem && hasNextPage && !isLoading && !isEmptyStateShown && !isError
    }
}