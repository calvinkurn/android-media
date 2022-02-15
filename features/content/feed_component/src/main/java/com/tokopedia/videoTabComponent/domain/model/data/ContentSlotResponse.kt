package com.tokopedia.videoTabComponent.domain.model.data

import com.google.gson.annotations.SerializedName

data class ContentSlotResponse(
        @SerializedName("playGetContentSlot")
        val playGetContentSlot : PlayGetContentSlotResponse
)
