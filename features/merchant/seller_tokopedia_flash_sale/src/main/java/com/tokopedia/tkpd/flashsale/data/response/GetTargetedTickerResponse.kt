package com.tokopedia.tkpd.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class GetTargetedTickerResponse(
    @SerializedName("GetTargetedTicker")
    val getTargetedTicker: GetTargetedTicker
) {
    data class GetTargetedTicker(
        @SerializedName("List")
        val list: List<TickerData>
    )

    data class TickerData(
        @SerializedName("ID")
        val id: Int = 0,

        @SerializedName("Title")
        val title: String = "",

        @SerializedName("Content")
        val content: String = "",

        @SerializedName("Action")
        val action: ActionData,

        @SerializedName("Type")
        val type: String = "",

        @SerializedName("Priority")
        val priority: Int = 0,

        @SerializedName("Metadata")
        val metadata: MetaData
    )

    data class ActionData(
        @SerializedName("Label")
        val label: String = "",

        @SerializedName("Type")
        val type: String = "",

        @SerializedName("AppURL")
        val appUrl: String = "",

        @SerializedName("WebURL")
        val webUrl: String = ""
    )

    data class MetaData(
        @SerializedName("Type")
        val types: String = "",

        @SerializedName("Values")
        val values: List<String> = listOf()
    )
}

//
// {
//    "GetTargetedTicker": {
//        "List": [
//        {
//            "ID": 128,
//            "Title": "Info Pengembangan System AFTER REFACTOR",
//            "Content": "Notification content baru sedang berkendala, Mohon cek berkala.",
//            "Action": {
//                "Label": "Baca Selengkapnya",
//                "Type": "link",
//                "AppURL": "tokopedia://webview?url=https://www.tokopedia.com",
//                "WebURL": "https://www.tokopedia.com"
//            },
//            "Type": "info",
//            "Priority": 1,
//            "Metadata": [
//                {
//                    "Type": "layout",
//                    "Values": [
//                    "layout_A"
//                    ]
//                }
//            ]
//        }
//    }
// }
