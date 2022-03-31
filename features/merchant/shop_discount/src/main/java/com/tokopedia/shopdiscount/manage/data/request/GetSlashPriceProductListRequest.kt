package com.tokopedia.shopdiscount.manage.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetSlashPriceProductListRequest(
    @SerializedName("request_header")
    @Expose
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("filter")
    @Expose
    var filter: Filter = Filter(),
) {
    data class RequestHeader(
        @SerializedName("source")
        @Expose
        var source: String = "",
        @SerializedName("ip")
        @Expose
        var ip: String = "",
        @SerializedName("usecase")
        @Expose
        var usecase: String = ""
    )

    data class Filter(
        @SerializedName("page")
        @Expose
        var page: Int = 0,

        @SerializedName("page_size")
        @Expose
        var pageSize: Int = 10,

        @SerializedName("keyword")
        @Expose
        var keyword: String = "",

        @SerializedName("category")
        @Expose
        var categoryIds: List<String> = emptyList(),

        @SuppressLint("Invalid Data Type")
        @SerializedName("etalase_id")
        @Expose
        var etalaseIds: List<String> = emptyList(),

        @SerializedName("warehouse_ids")
        @Expose
        var warehouseIds: List<String> = emptyList()
    )
}
