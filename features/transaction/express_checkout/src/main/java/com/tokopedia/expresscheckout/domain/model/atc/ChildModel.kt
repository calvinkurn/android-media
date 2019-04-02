package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ChildModel(
        var productId: Int = 0,
        var price: Int = 0,
        var stock: Int = 0,
        var minOrder: Int = 0,
        var maxOrder: Int = 0,
        var sku: String? = null,
        var optionIds: ArrayList<Int>? = null,
        var isEnabled: Boolean? = false,
        var name: String? = null,
        var url: String? = null,
        var isBuyable: Boolean? = false,
        var stockWording: String? = null
)