package com.tokopedia.sellerappwidget.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerappwidget.common.Utils
import java.util.*

data class InputParameterModel(
        @SerializedName("search")
        @Expose
        val search: String = "",
        @SerializedName("start_date")
        @Expose
        val startDate: String = "",
        @SerializedName("end_date")
        @Expose
        val endDate: String = "",
        @SerializedName("filter_status")
        @Expose
        val filterStatus: Int = 999,
        @SerializedName("status_list")
        @Expose
        val statusList: List<Int> = listOf(),
        @SerializedName("shipping_list")
        @Expose
        val shippingList: List<Int> = arrayListOf(),
        @SerializedName("order_type_list")
        @Expose
        val orderTypeList: List<Int> = arrayListOf(),
        @SerializedName("sort_by")
        @Expose
        val sortBy: Int = SORT_BY_PAYMENT_DATE_DESCENDING,
        @SerializedName("is_mobile")
        @Expose
        val isMobile: Boolean = true,
        @SerializedName("next_order_id")
        @Expose
        val nextOrderId: Int = 0,
        @SerializedName("lang")
        @Expose
        val lang: String = "id",
        @SerializedName("page")
        @Expose
        val page: Int = 1,
        @SerializedName("batch_page")
        @Expose
        val batchPage: Int = 0,
        @SerializedName("is_shipping_printed")
        @Expose
        val isShippingPrinted: Int = 0,
        @SerializedName("deadline")
        @Expose
        val deadline: Int = 0
) {
    companion object {
        private const val SORT_BY_PAYMENT_DATE_DESCENDING = 2
    }
}