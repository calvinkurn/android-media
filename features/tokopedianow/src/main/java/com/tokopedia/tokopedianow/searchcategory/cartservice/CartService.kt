package com.tokopedia.tokopedianow.searchcategory.cartservice

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem2
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData2
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.tokopedianow.searchcategory.utils.NO_VARIANT_PARENT_PRODUCT_ID
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

open class CartService @Inject constructor (
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val userSession: UserSessionInterface,
) {
    private var allMiniCartItemList: Map<MiniCartItemKey, MiniCartItem2>? = null
//    private var cartItemsVariantGrouped: Map<String, List<MiniCartItem>>? = null

    open fun updateMiniCartItems(miniCartSimplifiedData: MiniCartSimplifiedData2) {
//        val cartItemsPartition =
//            splitCartItemsVariantAndNonVariant(miniCartSimplifiedData.miniCartItems)

        allMiniCartItemList = miniCartSimplifiedData.miniCartItems
//        cartItemsVariantGrouped =
//            cartItemsPartition.second.groupBy { it.productParentId }
    }

//    private fun splitCartItemsVariantAndNonVariant(miniCartItems: List<MiniCartItem>) =
//        miniCartItems.partition { it.productParentId == NO_VARIANT_PARENT_PRODUCT_ID }

    open fun getProductQuantity(
        productId: String,
        parentProductId: String = NO_VARIANT_PARENT_PRODUCT_ID
    ) = if (parentProductId == NO_VARIANT_PARENT_PRODUCT_ID)
        getProductNonVariantQuantity(productId)
    else
        getProductVariantTotalQuantity(parentProductId)

    private fun getProductNonVariantQuantity(productId: String) =
        allMiniCartItemList?.getMiniCartItemProduct(productId)?.quantity ?: 0

    private fun getProductVariantTotalQuantity(parentProductId: String) =
        allMiniCartItemList?.getMiniCartItemParentProduct(parentProductId)?.totalQuantity ?: 0

    open fun handleCart(
        cartProductItem: CartProductItem,
        quantity: Int,
        onSuccessAddToCart: (AddToCartDataModel) -> Unit,
        onSuccessUpdateCart: (UpdateCartV2Data) -> Unit,
        onSuccessDeleteCart: (RemoveFromCartData) -> Unit,
        onError: (Throwable) -> Unit,
        handleCartEventNonLogin: () -> Unit,
    ) {
        if (userSession.isLoggedIn)
            handleCartEvent(
                cartProductItem,
                quantity,
                onSuccessAddToCart,
                onSuccessUpdateCart,
                onSuccessDeleteCart,
                onError,
            )
        else handleCartEventNonLogin()
    }

    private fun handleCartEvent(
        cartProductItem: CartProductItem,
        quantity: Int,
        onSuccessAddToCart: (AddToCartDataModel) -> Unit,
        onSuccessUpdateCart: (UpdateCartV2Data) -> Unit,
        onSuccessDeleteCart: (RemoveFromCartData) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        if (cartProductItem.quantity == quantity) return

        when {
            cartProductItem.quantity == 0 -> addToCart(cartProductItem, quantity, onSuccessAddToCart, onError)
            quantity == 0 -> deleteCart(cartProductItem, onSuccessDeleteCart, onError)
            else -> updateCart(cartProductItem, quantity, onSuccessUpdateCart, onError)
        }
    }

    private fun addToCart(
        cartProductItem: CartProductItem,
        quantity: Int,
        onSuccess: (AddToCartDataModel) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = cartProductItem.productId,
            shopId = cartProductItem.shopId,
            quantity = quantity,
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute(onSuccess, onError)
    }

    private fun updateCart(
        cartProductItem: CartProductItem,
        quantity: Int,
        onSuccess: (UpdateCartV2Data) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        val productId = cartProductItem.productId
        val miniCartItem = getMiniCartItem(productId) ?: return

        val updateCartRequest = UpdateCartRequest(
            cartId = miniCartItem.cartId,
            quantity = quantity,
            notes = miniCartItem.notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )
        updateCartUseCase.execute(onSuccess, onError)
    }

    private fun getMiniCartItem(productId: String)
        = allMiniCartItemList?.getMiniCartItemProduct(productId)

    private fun deleteCart(
        cartProductItem: CartProductItem,
        onSuccess: (RemoveFromCartData) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        val productId = cartProductItem.productId
        val miniCartItem = getMiniCartItem(productId) ?: return

        deleteCartUseCase.setParams(listOf(miniCartItem.cartId))
        deleteCartUseCase.execute(onSuccess, onError)
    }
}