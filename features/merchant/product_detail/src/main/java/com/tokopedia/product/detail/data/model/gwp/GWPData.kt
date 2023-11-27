package com.tokopedia.product.detail.data.model.gwp

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.data.model.ActionData

/**
 * Created by yovi.putra on 28/07/23"
 * Project name: android-tokopedia-core
 **/

data class GWPData(
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
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("action")
        @Expose
        val action: ActionData = ActionData(),
        @SerializedName("cards")
        @Expose
        val cards: List<Card> = emptyList(),
        @SerializedName("loadMoreText")
        @Expose
        val loadMoreText: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("productIDs")
        @Expose
        val productIDs: List<String> = emptyList(),
        @SerializedName("offerID")
        @Expose
        val offerId: String = ""
    ) {

        data class Card(
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("productName")
            @Expose
            val productName: String = "",
            @SerializedName("subtitle")
            @Expose
            val subtitle: String = "",
            @SerializedName("contents")
            @Expose
            val contents: List<Content> = emptyList()
        ) {

            data class Content(
                @SerializedName("imageUrl")
                @Expose
                val imageUrl: String = "",
                @SerializedName("loadMoreText")
                @Expose
                val loadMoreText: String = ""
            )
        }
    }
}
