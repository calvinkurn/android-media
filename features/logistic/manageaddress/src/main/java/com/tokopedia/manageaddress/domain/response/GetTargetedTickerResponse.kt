package com.tokopedia.manageaddress.domain.response

import com.google.gson.annotations.SerializedName

data class GetTargetedTickerResponse(

    @SerializedName("GetTargetedTicker")
    val getTargetedTickerData: GetTargetedTickerData = GetTargetedTickerData()
) {
    data class GetTargetedTickerData(

        @SerializedName("List")
        val list: List<ListItem> = listOf()
    ) {
        data class ListItem(

            @SerializedName("Action")
            val action: Action = Action(),

            @SerializedName("Type")
            val type: String = "",

            @SerializedName("Content")
            val content: String = "",

            @SerializedName("Priority")
            val priority: Long = 0,

            @SerializedName("Metadata")
            val metadata: List<MetadataItem> = listOf(),

            @SerializedName("Title")
            val title: String = "",

            @SerializedName("ID")
            val id: Long = 0
        ) {
            data class Action(

                @SerializedName("Type")
                val type: String = "",

                @SerializedName("AppURL")
                val appURL: String = "",

                @SerializedName("Label")
                val label: String = "",

                @SerializedName("WebURL")
                val webURL: String = ""
            )

            data class MetadataItem(

                @SerializedName("Type")
                val type: String = "",

                @SerializedName("Values")
                val values: List<String> = listOf()
            )
        }
    }
}
