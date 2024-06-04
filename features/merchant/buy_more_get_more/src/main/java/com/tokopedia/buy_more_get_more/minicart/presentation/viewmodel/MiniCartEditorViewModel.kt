package com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.GetGroupProductTickerUseCase
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.GetMiniCartEditorDataUseCase
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.effect.MiniCartEditorEffect
import com.tokopedia.buy_more_get_more.minicart.presentation.model.event.MiniCartEditorEvent
import com.tokopedia.buy_more_get_more.minicart.presentation.model.state.MiniCartEditorState
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

class MiniCartEditorViewModel @Inject constructor(
    private val getMiniCartUseCase: Lazy<GetMiniCartEditorDataUseCase>,
    private val updateCartUseCase: Lazy<UpdateCartUseCase>,
    private val deleteCartUseCase: Lazy<DeleteCartUseCase>,
    private val getGroupProductTickerUseCase: Lazy<GetGroupProductTickerUseCase>,
    private val dispatchers: Lazy<CoroutineDispatchers>,
    private val userSession: Lazy<UserSessionInterface>,
    setCartListCheckboxStateUseCase: Lazy<SetCartlistCheckboxStateUseCase>
) : BaseCartCheckboxViewModel(setCartListCheckboxStateUseCase.get(), dispatchers.get()) {

    private var miniCartParam = MiniCartParam()

    private val _effect = MutableSharedFlow<MiniCartEditorEffect>()
    val effect: SharedFlow<MiniCartEditorEffect> = _effect.asSharedFlow()

    private val _cartData = MutableStateFlow(MiniCartEditorState())
    val cartData: StateFlow<MiniCartEditorState> = _cartData.asStateFlow()

    fun setEvent(event: MiniCartEditorEvent) {
        when (event) {
            is MiniCartEditorEvent.SetCartListCheckboxState -> setCartListCheckboxState()
            is MiniCartEditorEvent.FetchData -> fetchMiniCartData(event.param)
            is MiniCartEditorEvent.AdjustQuantity -> adjustQuantity(event.product, event.newQty)
            is MiniCartEditorEvent.DeleteCart -> deleteCart(event.product)
        }
    }

    fun getUserId(): String {
        return userSession.get().userId
    }

    private fun setCartListCheckboxState() {
        val cartIds =
            cartData.value.data.tiers.filterIsInstance<BmgmMiniCartVisitable.ProductUiModel>().map {
                it.cartId
            }
        super.setCartListCheckboxState(cartIds)
    }

    private fun fetchMiniCartData(param: MiniCartParam) {
        miniCartParam = param
        viewModelScope.launch {
            runCatching {
                _cartData.update { it.updateLoading() }
                val data = withContext(dispatchers.get().io) {
                    getMiniCartUseCase.get().invoke(param)
                }
                _cartData.update {
                    it.updateSuccess(data)
                }
            }.onFailure { t ->
                _cartData.update { it.updateError(t) }
            }
        }
    }

    private fun deleteCart(product: BmgmMiniCartVisitable.ProductUiModel) {
        viewModelScope.launch {
            runCatching {
                showPartialLoadingState()
                withContext(dispatchers.get().io) {
                    deleteCartUseCase.get().setParams(listOf(product.cartId))
                    deleteCartUseCase.get().executeOnBackground()
                }

                getGroupProductTickerOnProductRemoved(product)
            }.onFailure { t ->
                dismissPartialLoadingState()
                emitEffect(MiniCartEditorEffect.OnRemoveFailed(t))
            }
        }
    }

    private fun emitEffect(effect: MiniCartEditorEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun getGroupProductTickerOnProductRemoved(product: BmgmMiniCartVisitable.ProductUiModel) {
        val cartData = _cartData.value.data
        val isSingleProductOnCart =
            cartData.tiers.filterIsInstance<BmgmMiniCartVisitable.ProductUiModel>().size == Int.ONE
        if (isSingleProductOnCart) {
            emitEffect(MiniCartEditorEffect.DismissBottomSheet)
        } else {
            getGroupProductTicker(product)
        }
    }

    private fun adjustQuantity(product: BmgmMiniCartVisitable.ProductUiModel, newQty: Int) {
        viewModelScope.launch {
            runCatching {
                showPartialLoadingState()
                val params = listOf(
                    UpdateCartRequest(
                        quantity = newQty,
                        cartId = product.cartId,
                        productId = product.productId
                    )
                )
                updateCartUseCase.get().setParams(
                    params,
                    UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
                )
                withContext(dispatchers.get().io) {
                    updateCartUseCase.get().executeOnBackground()
                }
            }.onFailure {
                dismissPartialLoadingState()
            }

//            coz `updateCartUseCase` is fire-forget, so we put `getGroupProductTickerOnProductQtyAdjusted`
//            outside the `runCatching` scope
            getGroupProductTicker(product, newQty)
        }
    }

    private fun getGroupProductTicker(
        product: BmgmMiniCartVisitable.ProductUiModel,
        newQty: Int = -1
    ) {
        viewModelScope.launch {
            runCatching {
                val data = _cartData.value.data
                val shopIds = miniCartParam.shopIds
                val updatedData = withContext(dispatchers.get().io) {
                    getGroupProductTickerUseCase.get().invoke(data, product, shopIds, newQty)
                }
                _cartData.update {
                    it.updateSuccess(updatedData)
                }
            }.onFailure { t ->
                _cartData.update {
                    it.updateError(t)
                }
            }
        }
    }

    private fun showPartialLoadingState() {
        _cartData.update {
            it.updatePartiallyLoading()
        }
    }

    private fun dismissPartialLoadingState() {
        _cartData.update {
            it.dismissPartiallyLoading()
        }
    }
}
