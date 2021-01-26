package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName

data class HomeIconData(
        @SerializedName("dynamicHomeIcon")
        var dynamicHomeIcon: DynamicHomeIcon = DynamicHomeIcon()
)