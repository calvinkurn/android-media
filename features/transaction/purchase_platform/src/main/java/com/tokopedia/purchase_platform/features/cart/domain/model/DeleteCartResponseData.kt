package com.tokopedia.purchase_platform.features.cart.domain.model

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData

/**
 * @author anggaprasetiyo on 02/03/18.
 */

data class DeleteCartResponseData(
        var deleteCartData: DeleteCartData? = null
)
