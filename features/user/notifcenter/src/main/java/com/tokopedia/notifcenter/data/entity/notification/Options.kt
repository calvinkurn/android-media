package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Options(
    @SerializedName("longer_content")
    val longerContent: Int = 150
)