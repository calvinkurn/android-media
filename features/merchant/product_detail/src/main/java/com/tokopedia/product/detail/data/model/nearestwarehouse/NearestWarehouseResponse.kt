package com.tokopedia.product.detail.data.model.nearestwarehouse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.variant_common.model.WarehouseInfo

/**
 * Created by Yehezkiel on 28/07/20
 */
data class NearestWarehouseResponse(
        @SerializedName("product_id")
        @Expose
        var productId: String = "",

        @SerializedName("warehouse_info")
        @Expose
        var warehouseInfo: WarehouseInfo = WarehouseInfo()
)