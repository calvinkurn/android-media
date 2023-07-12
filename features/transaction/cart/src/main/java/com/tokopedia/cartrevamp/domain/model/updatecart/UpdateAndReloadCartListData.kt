package com.tokopedia.cartrevamp.domain.model.updatecart

data class UpdateAndReloadCartListData(
    var updateCartData: UpdateCartData = UpdateCartData(),
    var cartId: String = "",
    var getCartState: Int = 0
)
