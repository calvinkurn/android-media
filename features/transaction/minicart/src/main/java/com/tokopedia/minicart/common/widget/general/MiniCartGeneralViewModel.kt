package com.tokopedia.minicart.common.widget.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.chatlist.MiniCartChatListViewModel
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.GlobalEvent
import javax.inject.Inject

class MiniCartGeneralViewModel @Inject constructor(
    executorDispatchers: CoroutineDispatchers,
    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    private val getMiniCartListUseCase: GetMiniCartListUseCase,
    private val miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper
) : BaseViewModel(executorDispatchers.main), MiniCartChatListViewModel {

    // Global Data
    private val _currentShopIds = MutableLiveData<List<String>>()
    val currentShopIds: LiveData<List<String>>
        get() = _currentShopIds
    var isShopDirectPurchase = false

    var currentSource: MiniCartSource = MiniCartSource.ShopPage
    var currentPage: MiniCartAnalytics.Page = MiniCartAnalytics.Page.SHOP_PAGE

    // Widget Data
    private val _globalEvent = MutableLiveData<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    // Bottom Sheet Data
    private val _miniCartChatListBottomSheetUiModel = MutableLiveData<MiniCartListUiModel>()

    fun initializeShopIds(shopIds: List<String>) {
        _currentShopIds.value = shopIds
    }

    fun initializeGlobalState() {
        _globalEvent.value = GlobalEvent()
    }

    fun updateMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
    }

    private fun onSuccessGetCartList(isFirstLoad: Boolean, miniCartData: MiniCartData) {
        if (isFirstLoad && miniCartData.data.outOfService.id.isNotBlank() && miniCartData.data.outOfService.id != "0") {
            _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET,
                data = miniCartData
            )
        } else {
            val tmpMiniCartChatListUiModel = miniCartChatListUiModelMapper.mapUiModel(miniCartData)

            tmpMiniCartChatListUiModel.isFirstLoad = isFirstLoad

            _miniCartChatListBottomSheetUiModel.value = tmpMiniCartChatListUiModel
        }
    }

    private fun onErrorGetCartList(isFirstLoad: Boolean, throwable: Throwable) {
        if (isFirstLoad) {
            _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET,
                throwable = throwable
            )
        }
    }

    // API Call

    fun getLatestWidgetState(shopIds: List<String>? = null, delay: Long = 0) {
        if (shopIds != null) {
            initializeShopIds(shopIds)
            getMiniCartListSimplifiedUseCase.setParams(shopIds, currentSource, isShopDirectPurchase, delay)
        } else {
            val tmpShopIds = getCurrentShopIds()
            getMiniCartListSimplifiedUseCase.setParams(tmpShopIds, currentSource, isShopDirectPurchase, delay)
        }
        getMiniCartListSimplifiedUseCase.execute(
            onSuccess = {
                it.isShowMiniCartWidget = it.miniCartWidgetData.totalProductCount > 0 ||
                        it.miniCartWidgetData.containsOnlyUnavailableItems
                _miniCartSimplifiedData.value = it
            },
            onError = {
                if (miniCartSimplifiedData.value != null) {
                    _miniCartSimplifiedData.value = miniCartSimplifiedData.value
                } else {
                    _miniCartSimplifiedData.value = MiniCartSimplifiedData()
                }
            }
        )
    }

    override fun getCartList(isFirstLoad: Boolean) {
        val shopIds = getCurrentShopIds()
        getMiniCartListUseCase.setParams(shopIds, isShopDirectPurchase)
        getMiniCartListUseCase.execute(
            onSuccess = {
                onSuccessGetCartList(isFirstLoad, it)
            },
            onError = {
                onErrorGetCartList(isFirstLoad, it)
            }
        )
    }

    override fun getCurrentShopIds(): List<String> {
        return _currentShopIds.value ?: emptyList()
    }

    override fun getMiniCartChatListBottomSheetUiModel(): LiveData<MiniCartListUiModel> {
        return _miniCartChatListBottomSheetUiModel
    }
}