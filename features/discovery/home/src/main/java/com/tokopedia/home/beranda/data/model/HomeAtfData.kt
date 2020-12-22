package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeAtfData (
        @SerializedName("dynamicPosition")
        @Expose
        val dataList: List<AtfData> = mutableListOf()
)
