package com.tokopedia.play.widget.sample.data

import com.google.gson.annotations.SerializedName

data class ContentSlotResponse(
        val data : Data
)

data class Data(
        val playGetContentSlot : PlayGetContentSlotResponse
)
data class PlayGetContentSlotResponse(
        @SerializedName("data")
        var data: List<PlaySlot> = emptyList(),
        @SerializedName("meta")
        var meta: PlayPagingProperties = PlayPagingProperties(),
)
