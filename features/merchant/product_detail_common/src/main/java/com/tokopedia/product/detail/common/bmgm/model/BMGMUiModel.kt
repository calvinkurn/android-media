package com.tokopedia.product.detail.common.bmgm.model

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/


data class BMGMUiModel(
    val titles: List<String>,
    val iconUrl: String,
    val products: List<Product>,
    val backgroundColor: String,
    val action: Action,
    val loadMoreText: String
) {

    data class Product(
        val imageUrl: String
    )

    data class Action(
        val type: String,
        val link: String
    ) {
        companion object {
            const val WEBVIEW = "webview"
            const val APPLINK = "applink"
        }
    }
}
