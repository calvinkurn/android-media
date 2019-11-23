package com.tokopedia.home.beranda.domain.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel

@Entity
data class HomeData(
    @PrimaryKey
    @NonNull
    val id: Int = 1,
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
    var isCache: Boolean = false
)