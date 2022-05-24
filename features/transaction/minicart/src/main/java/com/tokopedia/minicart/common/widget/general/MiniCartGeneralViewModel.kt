package com.tokopedia.minicart.common.widget.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.GlobalEvent
import javax.inject.Inject

class MiniCartGeneralViewModel @Inject constructor(
    executorDispatchers: CoroutineDispatchers,
    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val miniCartListUiModelMapper: MiniCartListUiModelMapper,
    private val miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper
) : BaseViewModel(executorDispatchers.main) {

    // Global Data
    private val _currentShopIds = MutableLiveData<List<String>>()
    val currentShopIds: LiveData<List<String>>
        get() = _currentShopIds
    var isShopDirectPurchase = false
    var currentSource: MiniCartSource = MiniCartSource.TokonowHome // TODO: Ask: Should default source be MiniCartSource.TokonowHome?

    // Widget Data
    private val _globalEvent = MutableLiveData<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    // Bottom Sheet Data
    private val _miniCartChatListBottomSheetUiModel = MutableLiveData<MiniCartListUiModel>()
    val miniCartChatListBottomSheetUiModel: LiveData<MiniCartListUiModel>
        get() = _miniCartChatListBottomSheetUiModel

    val tempHiddenUnavailableItems = mutableListOf<Visitable<*>>()

    var lastDeletedProductItems: List<MiniCartProductUiModel>? = null
        private set

    fun initializeShopIds(shopIds: List<String>) {
        _currentShopIds.value = shopIds
    }

    private fun getShopIds(): List<String> {
        return currentShopIds.value ?: emptyList()
    }

    fun initializeGlobalState() {
        _globalEvent.value = GlobalEvent()
    }

    fun updateMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
    }

    fun getLatestMiniCartSimplifiedData(): MiniCartSimplifiedData {
        TODO("Not yet implemented")
    }

    fun resetTemporaryHiddenUnavailableItems() {
        tempHiddenUnavailableItems.clear()
    }

    fun goToCheckout(observer: Int) {

    }

    // API Call

    fun getLatestWidgetState(shopIds: List<String>? = null) {
        if (shopIds != null) {
            initializeShopIds(shopIds)
            getMiniCartListSimplifiedUseCase.setParams(shopIds, currentSource)
        } else {
            val tmpShopIds = getShopIds()
            getMiniCartListSimplifiedUseCase.setParams(tmpShopIds, currentSource)
        }
        getMiniCartListSimplifiedUseCase.execute(
            onSuccess = {
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
}