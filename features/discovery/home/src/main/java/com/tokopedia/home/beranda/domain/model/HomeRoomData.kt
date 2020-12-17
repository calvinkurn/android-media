package com.tokopedia.home.beranda.domain.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class HomeRoomData (
    @PrimaryKey
    @NonNull
    val id: Int = 1,
    @SerializedName("home_data")
    val homeData: HomeData? = HomeData(),
    @ColumnInfo(name = "modification_date")
    @SerializedName(value = "modification_date")
    var modificationDate: Date = Date(System.currentTimeMillis())
)