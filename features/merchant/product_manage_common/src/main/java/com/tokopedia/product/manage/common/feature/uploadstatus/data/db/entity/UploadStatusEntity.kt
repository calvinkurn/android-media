package com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusConstant.DB_COLUMN_PRODUCT_ID
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusConstant.DB_COLUMN_STATUS
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusConstant.DB_ID
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusConstant.DB_TABLE

@Entity(tableName = DB_TABLE)
data class UploadStatusEntity(
    @Expose
    @SerializedName(DB_ID)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Expose
    @SerializedName(DB_COLUMN_STATUS)
    @ColumnInfo(name = DB_COLUMN_STATUS)
    val status: String,
    @Expose
    @SerializedName(DB_COLUMN_PRODUCT_ID)
    @ColumnInfo(name = DB_COLUMN_PRODUCT_ID)
    val productId: String,
)