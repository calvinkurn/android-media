package com.tokopedia.shopdiscount.manage.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus

data class GetSlashPriceProductListRequest(
    @SerializedName("request_header")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("filter")
    var filter: Filter = Filter()
) {
    data class Filter(
        @SerializedName("page")
        var page: Int = 0,

        @SerializedName("page_size")
        var pageSize: Int = 10,

        @SerializedName("keyword")
        var keyword: String = "",

        @SerializedName("category")
        var categoryIds: List<String> = emptyList(),

        @SuppressLint("Invalid Data Type")
        @SerializedName("etalase_id")
        var etalaseIds: List<String> = emptyList(),

        @SerializedName("warehouse_ids")
        var warehouseIds: List<String> = emptyList(),

        @SerializedName("status")
        var status: Int = DiscountStatus.ONGOING
    )
}
