package com.tokopedia.sellerorder.filter.domain


import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomFilterResponse(
    @Expose
    @SerializedName("orderFilterSom")
    val orderFilterSom: OrderFilterSom = OrderFilterSom()
) {
    data class OrderFilterSom(
        @SerializedName("order_types")
        @Expose
        val orderTypeList: List<OrderType> = listOf(),
        @Expose
        @SerializedName("shipping_list")
        val shippingList: List<Shipping> = listOf(),
        @Expose
        @SerializedName("status_list")
        val statusList: List<StatusList> = listOf(),
        @SerializedName("sort_by")
        @Expose
        val sortList: List<SortBy> = listOf()
    ) {

        data class OrderType(
            @Expose
            @SerializedName("id")
            val id: Long = 0,
            @Expose
            @SerializedName("key")
            val key: String? = "",
            @Expose
            @SerializedName("name")
            val name: String? = ""
        )

        data class Shipping(
            @Expose
            @SerializedName("img_logo")
            val imgLogo: String = "",
            @Expose
            @SerializedName("shipping_code")
            val shippingCode: String? = "",
            @Expose
            @SerializedName("shipping_desc")
            val shippingDesc: String? = "",
            @Expose
            @SerializedName("shipping_id")
            val shippingId: String = "0",
            @Expose
            @SerializedName("shipping_name")
            val shippingName: String? = "",
            @Expose
            @SerializedName("status")
            val status: Int = 0
        )

        data class StatusList(
            @Expose
            @SerializedName("child_status")
            val childStatus: List<ChildStatus> = listOf(),
            @Expose
            @SerializedName("is_checked")
            val isChecked: Boolean = false,
            @Expose
            @SerializedName("key")
            val key: String = "",
            @Expose
            @SerializedName("order_status")
            val orderStatus: String = "",
            @Expose
            @SerializedName("order_status_amount")
            val orderStatusAmount: Int = 0,
            @SuppressLint("Invalid Data Type")
            @Expose
            @SerializedName("order_status_id")
            val orderStatusId: List<Int> = listOf()
        ) {
            data class ChildStatus(
                @Expose
                @SerializedName("amount")
                val amount: Int = 0,
                @SuppressLint("Invalid Data Type")
                @Expose
                @SerializedName("id")
                val id: List<Int> = listOf(),
                @Expose
                @SerializedName("is_checked")
                val isChecked: Boolean = false,
                @Expose
                @SerializedName("key")
                val key: String? = "",
                @Expose
                @SerializedName("text")
                val text: String? = ""
            )
        }

        data class SortBy(
            @SerializedName("text")
            @Expose
            val text: String = "",
            @SerializedName("value")
            @Expose
            val value: Long = 0
        )

    }
}