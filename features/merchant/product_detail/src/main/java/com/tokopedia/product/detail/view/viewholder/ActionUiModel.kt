package com.tokopedia.product.detail.view.viewholder

/**
 * Created by yovi.putra on 28/11/23"
 * Project name: android-tokopedia-core
 **/

data class ActionUiModel(
    val type: String = "",
    val link: String = ""
) {

    companion object {
        const val APPLINK = "applink"
        const val WEBVIEW = "webview"
    }
}
