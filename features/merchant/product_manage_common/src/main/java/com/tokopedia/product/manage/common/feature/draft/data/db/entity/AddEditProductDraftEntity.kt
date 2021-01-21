package com.tokopedia.product.manage.common.feature.draft.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant

@Entity(tableName = AddEditProductDraftConstant.DB_TABLE)
data class AddEditProductDraftEntity(
        @PrimaryKey(autoGenerate = AddEditProductDraftConstant.DB_AUTOGENERATE)
        var id: Long = 0,
        @ColumnInfo(name = AddEditProductDraftConstant.DB_COLUMN_DATA)
        var data: String = "",
        @ColumnInfo(name = AddEditProductDraftConstant.DB_COLUMN_IS_UPLOADING)
        var isUploading: Boolean = false,
        var shopId: String? = null,
        var version: Int = 0
)