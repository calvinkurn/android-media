package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Ratio(
        @SerializedName("x")
        val x: Float = 2f,
        @SerializedName("y")
        val y: Float = 1f
)