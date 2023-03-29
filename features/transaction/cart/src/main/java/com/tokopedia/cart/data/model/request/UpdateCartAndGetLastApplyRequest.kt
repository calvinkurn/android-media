package com.tokopedia.cart.data.model.request

import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest

data class UpdateCartWrapperRequest(
    val updateCartRequestList: List<com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest> = emptyList(),
    val source: String = "",
    val getLastApplyPromoRequest: ValidateUsePromoRequest = ValidateUsePromoRequest(),
    val cartId: String = "",
    val getCartState: Int = 0,
) {
    fun getUpdateCartRequest(): List<UpdateCartRequest> {
        return updateCartRequestList.map {
            UpdateCartRequest(
                it.cartId,
                it.quantity,
                it.notes
            )
        }
    }
}
