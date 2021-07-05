package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName

data class HomeFlagData(
        @SerializedName("homeFlag")
        var homeFlag: HomeFlag = HomeFlag()
)