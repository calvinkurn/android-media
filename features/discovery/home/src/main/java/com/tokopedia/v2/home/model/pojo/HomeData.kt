package com.tokopedia.v2.home.model.pojo

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.v2.home.data.datasource.local.converter.*

@Entity
class HomeData(
    @PrimaryKey
    @NonNull
    val id: Int = 1,

    @Expose
    @SerializedName("dynamicHomeChannel")
    val dynamicHomeChannel: DynamicHomeChannel,

    @Expose
    @SerializedName("slides")
    val banner: Banner,

    @Expose
    @SerializedName("ticker")
    val ticker: Ticker,

    @Expose
    @SerializedName("dynamicHomeIcon")
    val dynamicHomeIcon: DynamicHomeIcon,

    @Expose
    @SerializedName("spotlight")
    val spotlight: Spotlight,

    @Expose
    @SerializedName("homeFlag")
    var homeFlag: HomeFlag
)

