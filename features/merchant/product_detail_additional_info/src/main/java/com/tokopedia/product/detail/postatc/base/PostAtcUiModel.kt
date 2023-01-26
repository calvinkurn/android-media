package com.tokopedia.product.detail.postatc.base

interface PostAtcUiModel {
    object Type {
        const val PRODUCT_INFO = "post_atc"
        const val RECOMMENDATION = "product_list"
    }

    val name: String
}
