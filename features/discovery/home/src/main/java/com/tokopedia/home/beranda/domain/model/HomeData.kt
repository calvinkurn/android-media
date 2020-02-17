package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel

data class HomeData(
    @SerializedName("dynamicHomeChannel")
    val dynamicHomeChannel: DynamicHomeChannel = DynamicHomeChannel(),
    @SerializedName("slides")
    val banner: BannerDataModel = BannerDataModel(),
    @SerializedName("ticker")
    val ticker: Ticker = Ticker(),
    @SerializedName("dynamicHomeIcon")
    val dynamicHomeIcon: DynamicHomeIcon = DynamicHomeIcon(),
    @SerializedName("spotlight")
    val spotlight: Spotlight = Spotlight(),
    @SerializedName("homeFlag")
    val homeFlag: HomeFlag = HomeFlag(),
    val isCache: Boolean = false
)