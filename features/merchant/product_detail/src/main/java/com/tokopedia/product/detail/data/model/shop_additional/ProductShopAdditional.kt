package com.tokopedia.product.detail.data.model.shop_additional

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by yovi.putra on 25/10/22"
 * Project name: android-tokopedia-core
 **/

data class ProductShopAdditional(
    @SerializedName("icon")
    @Expose
    val icon: String = String.EMPTY,
    @SerializedName("title")
    @Expose
    val title: String = String.EMPTY,
    @SerializedName("subtitle")
    @Expose
    val subTitle: String = String.EMPTY,
    @SerializedName("applink")
    @Expose
    val appLink: String = String.EMPTY,
    @SerializedName("linkText")
    @Expose
    val linkText: String = String.EMPTY,
    @SerializedName("label")
    @Expose
    val label: List<String> = emptyList()
) {

    fun isEmpty() = title.isEmpty()
}
