package com.tokopedia.seller.action.order.domain.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SellerActionOrderListParam(
        @SerializedName("start_date")
        @Expose
        var startDate: String = "01/01/2010",
        @SerializedName("end_date")
        @Expose
        var endDate: String = "",
        @SerializedName("filter_status")
        @Expose
        var filterStatus: Int = 999,
        @SerializedName("status_list")
        @Expose
        var statusList: List<Int> = listOf(),
        @SerializedName("shipping_list")
        @Expose
        var shippingList: List<Int> = arrayListOf(),
        @SerializedName("lang")
        @Expose
        var lang: String = "id",
        @SerializedName("page")
        @Expose
        var page: Int = 1,
        @SerializedName("batch_page")
        @Expose
        var batchPage: Int = 0,
        @SerializedName("source")
        @Expose
        var source: String = "google-assistant"
)