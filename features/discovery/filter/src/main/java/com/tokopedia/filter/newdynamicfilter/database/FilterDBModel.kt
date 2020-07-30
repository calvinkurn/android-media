package com.tokopedia.filter.newdynamicfilter.database

import androidx.annotation.NonNull
import java.io.Serializable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FilterDBModel(
        @field:PrimaryKey
        @field:ColumnInfo(name = "filter_id")
        @field:NonNull
        val filterId: String, @field:ColumnInfo(name = "filter_data")
        val filterData: String
) : Serializable
