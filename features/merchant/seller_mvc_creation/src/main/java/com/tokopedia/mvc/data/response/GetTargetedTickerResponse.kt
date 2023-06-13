package com.tokopedia.mvc.data.response


import com.google.gson.annotations.SerializedName

data class GetTargetedTickerResponse(
    @SerializedName("GetTargetedTicker")
    val getTargetedTicker: GetTargetedTicker
) {
    data class GetTargetedTicker(
        @SerializedName("List")
        val list: List<TickerList> = emptyList()
    ) {
        data class TickerList (
            @SerializedName("Action")
            val action: Action,
            @SerializedName("Content")
            val content: String,
            @SerializedName("ID")
            val ID: Int,
            @SerializedName("Priority")
            val priority: Int,
            @SerializedName("Title")
            val title: String,
            @SerializedName("Type")
            val type: String
        ) {
            data class Action(
                @SerializedName("AppURL")
                val appURL: String,
                @SerializedName("Label")
                val label: String,
                @SerializedName("Type")
                val type: String,
                @SerializedName("WebURL")
                val webURL: String
            )
        }
    }
}
