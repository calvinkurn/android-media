package com.tokopedia.dynamicbanner.entity

import com.google.gson.annotations.SerializedName

data class PlayCardDataWrapper(
        @SerializedName("data") var data: PlayCardData = PlayCardData()
)