package com.tokopedia.product.detail.view.viewholder.bmgm.model

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

data class BMGMUiModel(
    val title: String = "",
    val iconUrl: String = "",
    val products: List<Product> = emptyList(),
    val backgroundColor: String = "",
    val action: Action = Action(),
    val titleColor: String = "",
    val separator: String = ""
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
}
