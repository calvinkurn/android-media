package com.tokopedia.expresscheckout.domain.model.checkout

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class ItemModel(
        var id: Int = 0,
        var name: String? = null,
        var quantity: Int = 0,
        var price: Int = 0
)