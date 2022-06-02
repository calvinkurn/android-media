package com.tokopedia.product.manage.common.feature.draft.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_COLUMN_DATA
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_COLUMN_ID
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_COLUMN_IS_UPLOADING
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_SHOP_ID
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DB_VERSION

@Entity(tableName = AddEditProductDraftConstant.DB_TABLE)
data class AddEditProductDraftEntity(
        @Expose
        @SerializedName(DB_COLUMN_ID)
        @PrimaryKey(autoGenerate = AddEditProductDraftConstant.DB_AUTOGENERATE)
        var id: Long = 0,
        @Expose
        @SerializedName(DB_COLUMN_DATA)
        @ColumnInfo(name = DB_COLUMN_DATA)
        var data: String = "",
        @Expose
        @SerializedName(DB_COLUMN_IS_UPLOADING)
        @ColumnInfo(name = DB_COLUMN_IS_UPLOADING)
        var isUploading: Boolean = false,
        @Expose
        @SerializedName(DB_SHOP_ID)
        var shopId: String? = null,
        @Expose
        @SerializedName(DB_VERSION)
        var version: Int = 0
)