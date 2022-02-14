package com.tokopedia.videoTabComponent.domain.model.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.widget.sample.data.PlayGetContentSlotResponse

data class ContentSlotResponse(
        @SerializedName("playGetContentSlot")
        val playGetContentSlot : PlayGetContentSlotResponse
)
