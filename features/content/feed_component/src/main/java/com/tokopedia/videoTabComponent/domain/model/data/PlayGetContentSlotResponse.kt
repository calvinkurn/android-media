package com.tokopedia.videoTabComponent.domain.model.data

import com.google.gson.annotations.SerializedName

data class Data(
    val playGetContentSlot: PlayGetContentSlotResponse
)
data class PlayGetContentSlotResponse(
    @SerializedName("data")
    var data: List<PlaySlot> = mutableListOf(),
    @SerializedName("meta")
    var meta: PlayPagingProperties = PlayPagingProperties(),

    var appendeList: List<PlaySlot> = mutableListOf()
)
