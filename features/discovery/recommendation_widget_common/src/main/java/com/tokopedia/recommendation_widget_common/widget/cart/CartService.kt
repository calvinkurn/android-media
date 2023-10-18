package com.tokopedia.recommendation_widget_common.widget.cart

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationWidgetScope
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMiniCart
import javax.inject.Inject

@RecommendationWidgetScope
class CartService @Inject constructor(
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
) {

    internal fun getMiniCart(
        shopId: String,
        miniCartSource: MiniCartSource,
        onSuccess: (MiniCartSimplifiedData) -> Unit,
    ) {
        getMiniCartUseCase.setParams(listOf(shopId), miniCartSource)
        getMiniCartUseCase.execute(onSuccess, onError = { })
    }

    internal fun handleCart(
        productId: String,
        shopId: String,
        currentQuantity: Int,
        updatedQuantity: Int,
        miniCartItem: MiniCartItem.MiniCartItemProduct?,
        miniCartSource: MiniCartSource,
        onSuccessAddToCart: (AddToCartDataModel, MiniCartSimplifiedData) -> Unit,
        onSuccessUpdateCart: (UpdateCartV2Data, MiniCartSimplifiedData) -> Unit,
        onSuccessDeleteCart: (RemoveFromCartData, MiniCartSimplifiedData) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        if (currentQuantity == updatedQuantity) return

        if (currentQuantity == 0)
            addToCart(
                productId,
                shopId,
                updatedQuantity,
                miniCartSource,
                onSuccessAddToCart,
                onError
            )
        else if (updatedQuantity == 0)
            deleteCart(
                shopId,
                miniCartItem,
                miniCartSource,
                onSuccessDeleteCart,
                onError,
            )
        else
            updateCart(
                shopId,
                miniCartItem,
                miniCartSource,
                updatedQuantity,
                onSuccessUpdateCart,
                onError,
            )
    }

    private fun addToCart(
        productId: String,
        shopId: String,
        updatedQuantity: Int,
        miniCartSource: MiniCartSource,
        onSuccessAddToCart: (AddToCartDataModel, MiniCartSimplifiedData) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        addToCartUseCase.setParams(AddToCartRequestParams(productId, shopId, updatedQuantity))
        addToCartUseCase.execute(
            onSuccess = { atcResponse ->
                getMiniCart(shopId, miniCartSource) { miniCart ->
                    onSuccessAddToCart(atcResponse, miniCart)
                }
            },
            onError = onError,
        )
    }

    private fun deleteCart(
        shopId: String,
        miniCartItem: MiniCartItem.MiniCartItemProduct?,
        miniCartSource: MiniCartSource,
        onSuccess: (RemoveFromCartData, MiniCartSimplifiedData) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        miniCartItem ?: return

        deleteCartUseCase.setParams(listOf(miniCartItem.cartId))
        deleteCartUseCase.execute(
            onSuccess = { deleteCartResponse ->
                getMiniCart(shopId, miniCartSource) { miniCart ->
                    onSuccess(deleteCartResponse, miniCart)
                }
            },
            onError = onError
        )
    }

    private fun updateCart(
        shopId: String,
        miniCartItem: MiniCartItem.MiniCartItemProduct?,
        miniCartSource: MiniCartSource,
        updatedQuantity: Int,
        onSuccess: (UpdateCartV2Data, MiniCartSimplifiedData) -> Unit,
        onError: (Throwable) -> Unit,
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

        updateCartUseCase.execute(
            onSuccess = { updateCartResponse ->
                getMiniCart(shopId, miniCartSource) { miniCart ->
                    onSuccess(updateCartResponse, miniCart)
                }
            },
            onError = onError,
        )
    }
}
