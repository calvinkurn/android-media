package com.tokopedia.product.detail.postatc.base

import com.tokopedia.kotlin.model.ImpressHolder

interface PostAtcUiModel {
    object Type {
        const val PRODUCT_INFO = "post_atc"
        const val RECOMMENDATION = "product_list"
    }

    val name: String
    val type: String
    val id: Int
    val impressHolder: ImpressHolder
}
