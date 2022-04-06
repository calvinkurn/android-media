package com.tokopedia.variant_common.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo

/**
 * Created by Yehezkiel on 08/03/20
 */

data class VariantMultiOriginResponse(
        @SerializedName("GetNearestWarehouse")
        @Expose
        val result: MultiOriginData = MultiOriginData()
)

data class VariantMultiOriginWarehouse(
        @SerializedName("product_id")
        @Expose
        val productId: String = "",

        @SerializedName("stock")
        @Expose
        val stock: Int = 0,

        @SerializedName("stock_wording")
        @Expose
        val stockWording: String = "",

        @SerializedName("price")
        @Expose
        val price: java.lang.Double = (0.0 as java.lang.Double),

        @SerializedName("warehouse_info")
        @Expose
        val warehouseInfo: WarehouseInfo = WarehouseInfo()
)

data class MultiOriginData(
        @SerializedName("data")
        @Expose
        val data: List<VariantMultiOriginWarehouse> = listOf()
)


