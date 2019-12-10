package com.tokopedia.purchase_platform.features.cart.domain.model

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData

/**
 * @author anggaprasetiyo on 02/03/18.
 */

data class DeleteAndRefreshCartListData(
        var deleteCartData: DeleteCartData? = null,
        var cartListData: CartListData? = null
)
