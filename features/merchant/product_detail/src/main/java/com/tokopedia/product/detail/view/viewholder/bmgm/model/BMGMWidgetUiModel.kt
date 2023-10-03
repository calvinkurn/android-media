package com.tokopedia.product.detail.view.viewholder.bmgm.model

import com.tokopedia.product.detail.data.util.Separator

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

data class BMGMWidgetUiModel(
    val title: String = "",
    val iconUrl: String = "",
    val products: List<Product> = emptyList(),
    val backgroundColor: String = "",
    val action: Action = Action(),
    val titleColor: String = "",
    val separator: String = "",
    val offerId: String = ""
) {

    data class Product(
        val imageUrl: String = "",
        val loadMoreText: String = ""
    )

    data class Action(
        val type: String = "",
        val link: String = ""
    ) {
        companion object {
            const val APPLINK = "applink"
            const val WEBVIEW = "webview"
        }
    }

    val showSeparatorBottom
        get() = separator == Separator.BOTTOM || separator == Separator.BOTH
}
