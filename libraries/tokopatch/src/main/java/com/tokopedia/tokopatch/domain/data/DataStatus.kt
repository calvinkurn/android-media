package com.tokopedia.tokopatch.domain.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "status")
data class DataStatus(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "env")
    var env: String = "",
    @ColumnInfo(name = "expired")
    var expired: String = "",
    @ColumnInfo(name = "id_token")
    var tokenId: String = ""
)