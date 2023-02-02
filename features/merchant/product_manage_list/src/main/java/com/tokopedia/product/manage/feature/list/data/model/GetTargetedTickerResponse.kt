package com.tokopedia.product.manage.feature.list.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetTargetedTickerResponse(
    @SerializedName("GetTargetedTicker")
    @Expose
    val getTargetedTicker: GetTargetedTicker?
) {
    data class GetTargetedTicker(
        @SerializedName("List")
        @Expose
        val tickers: List<TickerResponse>
    ) {
        data class TickerResponse(
            @SerializedName("Action")
            @Expose
            val action: Action?,
            @SerializedName("Content")
            @Expose
            val content: String,
            @SerializedName("ID")
            @Expose
            val iD: Int,
            @SerializedName("Metadata")
            @Expose
            val metadata: List<Metadata>,
            @SerializedName("Priority")
            @Expose
            val priority: Int,
            @SerializedName("Title")
            @Expose
            val title: String,
            @SerializedName("Type")
            @Expose
            val type: String
        ) {
            data class Action(
                @SerializedName("AppURL")
                @Expose
                val appURL: String,
                @SerializedName("Label")
                @Expose
                val label: String,
                @SerializedName("Type")
                @Expose
                val type: String,
                @SerializedName("WebURL")
                @Expose
                val webURL: String
            )

            data class Metadata(
                @SerializedName("Type")
                @Expose
                val type: String,
                @SerializedName("Values")
                @Expose
                val values: List<String>
            )
        }
    }
}
