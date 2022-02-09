package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel

data class HomeData(
    @SerializedName("dynamicHomeChannel")
    @Expose
    var dynamicHomeChannel: DynamicHomeChannel = DynamicHomeChannel(),
    @SerializedName("slides")
    @Expose
    var banner: BannerDataModel = BannerDataModel(),
    @SerializedName("ticker")
    @Expose
    var ticker: Ticker = Ticker(),
    @SerializedName("dynamicHomeIcon")
    @Expose
    var dynamicHomeIcon: DynamicHomeIcon = DynamicHomeIcon(),
    @SerializedName("spotlight")
    @Expose
    val spotlight: Spotlight = Spotlight(),
    @SerializedName("homeFlag")
    @Expose
    var homeFlag: HomeFlag = HomeFlag(),
    @SerializedName("token")
    @Expose
    var token: String = "",
    @SerializedName("homeAtfData")
    @Expose
    var atfData: HomeAtfData? = HomeAtfData(),
    /**
     * is processing dynamic channel means home still getting dynamic channel from repository
     * and no dynamic channel provided yet.
     *
     * Note that dynamic channel is different with ATF data
     *
     */
    @SerializedName("isProcessingDynamicChannel")
    @Expose
    var isProcessingDynamicChannel: Boolean = false
)