package com.tokopedia.dynamicbanner.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayCardData(
        @Expose @SerializedName("card") var card: PlayCard = PlayCard()
)