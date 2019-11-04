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
data class HomeData(
    @PrimaryKey
    @NonNull
    val id: Int = 1,

    @Expose
    @ColumnInfo(name = "dynamicHomeChannel")
    @SerializedName("dynamicHomeChannel")
    val dynamicHomeChannel: DynamicHomeChannel?,

    @Expose
    @ColumnInfo(name = "slides")
    @SerializedName("slides")
    val slides: BannerDataModel?,

    @Expose
    @ColumnInfo(name = "ticker")
    @SerializedName("ticker")
    val ticker: Ticker?,

    @Expose
    @ColumnInfo(name = "dynamicHomeIcon")
    @SerializedName("dynamicHomeIcon")
    val dynamicHomeIcon: DynamicHomeIcon?,

    @Expose
    @ColumnInfo(name = "spotlight")
    @SerializedName("spotlight")
    val spotlight: Spotlight?,

    @Expose
    @ColumnInfo(name = "homeFlag")
    @SerializedName("homeFlag")
    var homeFlag: HomeFlag?
)

