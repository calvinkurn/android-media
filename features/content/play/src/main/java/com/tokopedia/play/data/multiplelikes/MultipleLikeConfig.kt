package com.tokopedia.play.data.multiplelikes

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 09/09/21
 */
data class MultipleLikeConfig(
    @SerializedName("icon")
    val icon: String = "",

    @SerializedName("background_color")
    val bgColor: String = "",
)