package com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusConstant.DB_COLUMN_PRODUCT_ID
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusConstant.DB_COLUMN_STATUS
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusConstant.DB_TABLE

@Entity(tableName = DB_TABLE)
data class UploadStatusEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = DB_COLUMN_STATUS)
    val status: String,
    @ColumnInfo(name = DB_COLUMN_PRODUCT_ID)
    val productId: String,
)