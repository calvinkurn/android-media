package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    var subtitle: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("infoLink")
    val infoLink: String = "",

    // For new product info only
    @SerializedName("showAtFront")
    val showAtFront: Boolean = false,
    @SerializedName("isAnnotation")
    val isAnnotation: Boolean = false,
    @SerializedName("showAtBottomsheet")
    val showAtBottomSheet: Boolean = false,

    // as identifier for tracker or mapping other action
    @SerializedName("key")
    val key: String = "",
    // types is action, applink, default
    @SerializedName("type")
    val type: String = "",
    // currently still one action is open_detail_product
    @SerializedName("action")
    val action: String = "",
    // param for type is action, currently for open_detail_product
    @SerializedName("extParam")
    val extParam: String = ""
) {

    companion object {
        const val KEY_ETALASE = "etalase"
        const val KEY_CATEGORY = "kategori"
        const val KEY_CATALOG = "katalog"
        const val KEY_PANDUAN_UKURAN = "panduan_ukuran"

        const val TYPE_ACTION = "action"
        const val TYPE_APPLINK = "applink"
        const val TYPE_WEBLINK = "weblink"

        const val ACTION_EMPTY = ""
        const val ACTION_OPEN_DETAIL_PRODUCT = "open_detail_product"
    }
}
