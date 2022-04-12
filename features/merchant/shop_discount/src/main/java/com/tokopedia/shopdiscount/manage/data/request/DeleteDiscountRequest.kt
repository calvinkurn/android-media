package com.tokopedia.shopdiscount.manage.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader

data class DeleteDiscountRequest(
    @SerializedName("request_header")
    @Expose
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("status")
    @Expose
    var status: Int,
    @SerializedName("product_ids")
    @Expose
    var productIds: List<String>,
    @SerializedName("product_data")
    @Expose
    var productData: List<ProductData> = emptyList(),
) {
    data class ProductData(
        @SerializedName("product_id")
        @Expose
        var productId: String = "",

        @SerializedName("warehouse_ids")
        @Expose
        var warehouseIds: List<String> = emptyList(),
    )
}
