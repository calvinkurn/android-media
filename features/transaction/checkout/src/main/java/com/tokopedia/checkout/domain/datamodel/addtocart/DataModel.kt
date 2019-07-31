package com.tokopedia.checkout.domain.datamodel.addtocart

/**
 * Created by Irfan Khoirul on 2019-07-02.
 */

data class DataModel(
        var cartId: Long = 0,
        var productId: Int = 0,
        var quantity: Int = 0,
        var notes: String = "",
        var shopId: Int = 0,
        var customerId: Int = 0
)