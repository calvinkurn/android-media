package com.tokopedia.dynamicbanner.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayCardHome(
        @Expose @SerializedName("playGetCardHome") var playGetCardHome: PlayCardDataWrapper = PlayCardDataWrapper()
)