package com.tokopedia.product.manage.item.main.draft.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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