package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.SerializedName

data class TapTapBaseEntity(
        @SerializedName("gamiTapEggHome")
        val gamiTapEggHome: GamiTapEggHome? = null
)