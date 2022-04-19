package com.tokopedia.home.beranda.domain.model.banner

import com.google.gson.annotations.SerializedName

data class HomeBannerData(
        @SerializedName("slides")
        var banner: BannerDataModel = BannerDataModel()
)