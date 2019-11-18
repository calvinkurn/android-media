package com.tokopedia.v2.home.model.pojo.home

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tokopedia.v2.home.model.pojo.home.*

@Entity
class HomeData(
        @PrimaryKey
    @NonNull
    val id: Int = 1,

        @SerializedName("dynamicHomeChannel")
    val dynamicHomeChannel: DynamicHomeChannel,

        @SerializedName("slides")
    val banner: Banner,

        @SerializedName("ticker")
    val ticker: Ticker,

        @SerializedName("dynamicHomeIcon")
    val dynamicHomeIcon: DynamicHomeIcon,

        @SerializedName("spotlight")
    val spotlight: Spotlight,

        @SerializedName("homeFlag")
    var homeFlag: HomeFlag
)

