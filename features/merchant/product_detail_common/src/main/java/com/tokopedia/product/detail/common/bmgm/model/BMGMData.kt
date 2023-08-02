package com.tokopedia.product.detail.common.bmgm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yovi.putra on 28/07/23"
 * Project name: android-tokopedia-core
 **/

data class BMGMData(
    @SerializedName("separator")
    @Expose
    val separator: String = "",
    @SerializedName("data")
    @Expose
    val data: List<Data> = emptyList()
) {

    data class Data(
        @SerializedName("backgroundColor")
        @Expose
        val backgroundColor: String = "",
        @SerializedName("titleColor")
        @Expose
        val titleColor: String = "",
        @SerializedName("iconUrl")
        @Expose
        val iconUrl: String = "",
        @SerializedName("titles")
        @Expose
        val titles: List<String> = emptyList(),
        @SerializedName("action")
        @Expose
        val action: Action = Action(),
        @SerializedName("contents")
        @Expose
        val contents: List<Content> = emptyList(),
        @SerializedName("loadMoreText")
        @Expose
        val loadMoreText: String = "",
        @SerializedName("productIDs")
        @Expose
        val productIDs: List<String> = emptyList()
    ) {

        data class Action(
            @SerializedName("type")
            @Expose
            val type: String = "",
            @SerializedName("link")
            @Expose
            val link: String = ""
        )

        data class Content(
            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = ""
        )
    }
}
