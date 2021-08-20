package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel

data class HomeData(
    @SerializedName("dynamicHomeChannel")
    var dynamicHomeChannel: DynamicHomeChannel = DynamicHomeChannel(),
    @SerializedName("slides")
    var banner: BannerDataModel = BannerDataModel(),
    @SerializedName("ticker")
    var ticker: Ticker = Ticker(),
    @SerializedName("dynamicHomeIcon")
    var dynamicHomeIcon: DynamicHomeIcon = DynamicHomeIcon(),
    @SerializedName("spotlight")
    val spotlight: Spotlight = Spotlight(),
    @SerializedName("homeFlag")
    var homeFlag: HomeFlag = HomeFlag(),
    var token: String = "",
    var atfData: HomeAtfData? = HomeAtfData(),
    /**
     * is processing dynamic channel means home still getting dynamic channel from repository
     * and no dynamic channel provided yet.
     *
     * Note that dynamic channel is different with ATF data
     *
     */
    var isProcessingDynamicChannel: Boolean = false
)