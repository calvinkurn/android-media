package com.tokopedia.purchase_platform.common.feature.addonsproduct.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class AddOnsProductResponse(
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("bottomsheet")
    val bottomsheet: Bottomsheet = Bottomsheet(),
    @SerializedName("data")
    val data: List<Data> = listOf()
) {
    data class Bottomsheet(
        @SerializedName("title")
        val title: String = String.EMPTY,
        @SerializedName("applink")
        val applink: String = String.EMPTY,
        @SerializedName("is_shown")
        val isShown: Boolean = false
    )
    data class Data(
        @SerializedName("id")
        val id: String = String.EMPTY,
        @SerializedName("unique_id")
        val uniqueId: String = String.EMPTY,
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Long = 0L,
        @SerializedName("info_link")
        val infoLink: String = String.EMPTY,
        @SerializedName("name")
        val name: String = String.EMPTY,
        @SerializedName("status")
        val status: Int = 1,
        @SerializedName("type")
        val type: Int = 1
    )
}
