package com.tokopedia.shopdiscount.manage.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader

data class DeleteDiscountRequest(
    @SerializedName("request_header")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("status")
    var status: Int,
    @SerializedName("product_ids")
    var productIds: List<String>,
    @SerializedName("product_data")
    var productData: List<ProductData> = emptyList(),
) {
    data class ProductData(
        @SerializedName("product_id")
        var productId: String = "",

        @SerializedName("warehouse_ids")
        var warehouseIds: List<String> = emptyList(),
    )
}
