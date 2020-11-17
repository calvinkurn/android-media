package com.tokopedia.sellerappwidget.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InputParameterModel(
        @Expose
        @SerializedName("batch_page")
        val batchPage: Int = 0,
        @Expose
        @SerializedName("end_date")
        val endDate: String = "",
        @Expose
        @SerializedName("filter_status")
        val filterStatus: Int = 0,
        @Expose
        @SerializedName("is_buyer_request_cancel")
        val isBuyerRequestCancel: Int = 0,
        @Expose
        @SerializedName("is_mobile")
        val isMobile: Boolean = false,
        @Expose
        @SerializedName("lang")
        val lang: String = "",
        @Expose
        @SerializedName("next_order_id")
        val nextOrderId: Int = 0,
        @Expose
        @SerializedName("order_type_list")
        val orderTypeList: List<Int> = emptyList(),
        @Expose
        @SerializedName("page")
        val page: Int = 0,
        @Expose
        @SerializedName("search")
        val search: String = "",
        @Expose
        @SerializedName("shipping_list")
        val shippingList: List<Int> = emptyList(),
        @Expose
        @SerializedName("sort_by")
        val sortBy: Int = 0,
        @Expose
        @SerializedName("start_date")
        val startDate: String = "",
        @Expose
        @SerializedName("status_list")
        val statusList: List<Int> = emptyList()
)