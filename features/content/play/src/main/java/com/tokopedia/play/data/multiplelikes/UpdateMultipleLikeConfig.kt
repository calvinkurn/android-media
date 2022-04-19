package com.tokopedia.play.data.multiplelikes

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 15/09/21
 */
data class UpdateMultipleLikeConfig(
    @SerializedName("channel_id")
    val channelId: Long = 0L,

    @SerializedName("configuration")
    val configuration: List<MultipleLikeConfig> = emptyList(),
)