package com.tokopedia.recommendation_widget_common.widget.cart

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationWidgetScope
import javax.inject.Inject

@RecommendationWidgetScope
class CartService @Inject constructor(
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
) {

    fun getMiniCart(onSuccess: (MiniCartSimplifiedData) -> Unit) {
        getMiniCartUseCase.execute(onSuccess, onError = { })
    }

    fun handleCart(
        productId: String,
        shopId: String,
        currentQuantity: Int,
        updatedQuantity: Int,
        miniCartItem: MiniCartItem.MiniCartItemProduct?,
        onSuccess: (MiniCartSimplifiedData) -> Unit
    ) {
        if (currentQuantity == 0) addToCart(productId, shopId, updatedQuantity, onSuccess)
        else if (updatedQuantity == 0) deleteCart(miniCartItem, onSuccess)
        else updateCart(miniCartItem, updatedQuantity, onSuccess)
    }

    private fun addToCart(
        productId: String,
        shopId: String,
        updatedQuantity: Int,
        onSuccess: (MiniCartSimplifiedData) -> Unit,
    ) {
        addToCartUseCase.setParams(AddToCartRequestParams(productId, shopId, updatedQuantity))
        addToCartUseCase.execute(
            onSuccess = { onSuccessCart(onSuccess) },
            onError = { },
        )
    }

    private fun onSuccessCart(onSuccess: (MiniCartSimplifiedData) -> Unit) {
        getMiniCartUseCase.execute(onSuccess = onSuccess, onError = {})
    }

    private fun deleteCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct?,
        onSuccess: (MiniCartSimplifiedData) -> Unit,
    ) {
        miniCartItem ?: return

        deleteCartUseCase.setParams(listOf(miniCartItem.cartId))
        deleteCartUseCase.execute({ onSuccessCart(onSuccess) }, {})
    }

    private fun updateCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct?,
        updatedQuantity: Int,
        onSuccess: (MiniCartSimplifiedData) -> Unit,
    ) {
        miniCartItem ?: return

        val updateCartRequest = UpdateCartRequest(
            cartId = miniCartItem.cartId,
            quantity = updatedQuantity,
            notes = miniCartItem.notes,
        )

        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )

        updateCartUseCase.execute({ onSuccessCart(onSuccess) }, {})
    }
}
