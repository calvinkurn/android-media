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

