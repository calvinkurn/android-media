package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Ratio(
        @SerializedName("x")
        val x: Int = 2,
        @SerializedName("y")
        val y: Int = 1
)