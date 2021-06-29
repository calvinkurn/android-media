package com.tokopedia.play.data.engagement

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 28/06/21
 */
data class GetCurrentInteractiveResponse(
        @SerializedName("playInteractiveGetCurrentInteractive")
        val data: Data = Data()
) {

    data class Data(
            @SerializedName("interactive")
            val interactive: Interactive = Interactive()
    )

    data class Interactive(
            @SerializedName("interactiveID")
            val interactiveID: Long = 0,

            @SerializedName("interactiveType")
            val interactiveType: Int = -1,

            @SerializedName("title")
            val title: String = "",

            @SerializedName("status")
            val status: Int = 0,

            @SerializedName("countdownStart")
            val countdownStart: Int = 0,

            @SerializedName("countdownEnd")
            val countdownEnd: Int = 0,
    )
}