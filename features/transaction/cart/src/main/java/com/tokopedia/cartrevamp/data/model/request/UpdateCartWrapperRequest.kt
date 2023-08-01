package com.tokopedia.cartrevamp.data.model.request

import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest

data class UpdateCartWrapperRequest(
    val updateCartRequestList: List<UpdateCartRequest> = emptyList(),
    val source: String = "",
    val getLastApplyPromoRequest: ValidateUsePromoRequest = ValidateUsePromoRequest(),
    val cartId: String = "",
    val getCartState: Int = 0
)
