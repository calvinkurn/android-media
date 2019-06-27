package com.tokopedia.productdraftdatabase

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = DBMetaData.DB_TABLE)
data class ProductDraft(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "data") var `data`: String,
        @ColumnInfo(name = COLUMN_IS_UPLOADING) var isUploading: Boolean,
        var shopId: String?,
        var version: Int
){
    constructor(): this(null, "", false, null, 0)

    companion object{
        const val COLUMN_IS_UPLOADING = "is_uploading"
    }
}