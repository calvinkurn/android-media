package com.tokopedia.sellerappwidget.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerappwidget.common.Const

data class InputParameterModel(
        @SerializedName("search")
        val search: String = "",
        @SerializedName("start_date")
        val startDate: String = "",
        @SerializedName("end_date")
        val endDate: String = "",
        @SerializedName("filter_status")
        val filterStatus: Int = 999,
        @SerializedName("status_list")
        val statusList: List<Int> = listOf(),
        @SerializedName("shipping_list")
        val shippingList: List<Int> = arrayListOf(),
        @SerializedName("order_type_list")
        val orderTypeList: List<Int> = arrayListOf(),
        @SerializedName("sort_by")
        val sortBy: Int = Const.OrderListSortBy.SORT_BY_PAYMENT_DATE_ASCENDING,
        @SerializedName("is_mobile")
        val isMobile: Boolean = true,
        @SerializedName("next_order_id")
        val nextOrderId: Int = 0,
        @SerializedName("lang")
        val lang: String = "id",
        @SerializedName("page")
        val page: Int = 1,
        @SerializedName("batch_page")
        val batchPage: Int = 0,
        @SerializedName("is_shipping_printed")
        val isShippingPrinted: Int = 0,
        @SerializedName("deadline")
        val deadline: Int = 0,
        @SerializedName("source")
        val source: String = "widget"
)