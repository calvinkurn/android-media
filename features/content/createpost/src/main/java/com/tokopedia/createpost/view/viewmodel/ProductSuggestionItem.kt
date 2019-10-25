package com.tokopedia.createpost.view.viewmodel

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by milhamj on 2019-09-17.
 */
data class ProductSuggestionItem(
        val productId: String = "",
        val adId: String = "",
        val title: String = "",
        val price: String = "",
        val imageUrl: String = "",
        val type: String = "",
        val impressHolder: ImpressHolder = ImpressHolder()
) {
    companion object {
        const val TYPE_AFFILIATE = "affiliate"
        const val TYPE_SHOP = "content-shop"
    }
}