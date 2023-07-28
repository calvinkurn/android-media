package com.tokopedia.product.detail.common.bmgm.ui.model

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/


data class BMGMUiModel(
    val titles: List<String> = emptyList(),
    val iconUrl: String = "",
    val products: List<Product> = emptyList(),
    val backgroundColor: String = "",
    val action: Action = Action(),
    val loadMoreText: String = "",
    val titleColor: String = ""
) {

    data class Product(
        val imageUrl: String = ""
    )

    data class Action(
        val type: String = "",
        val link: String = ""
    ) {
        companion object {
            const val APPLINK = "applink"
        }
    }
}
