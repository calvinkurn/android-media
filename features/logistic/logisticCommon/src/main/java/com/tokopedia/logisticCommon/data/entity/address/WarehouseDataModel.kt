package com.tokopedia.logisticCommon.data.entity.address

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WarehouseDataModel (
        @Expose
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0,

        @Expose
        @SerializedName("service_type")
        val serviceType: String = ""
) : Parcelable