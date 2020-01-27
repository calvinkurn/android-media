package com.tokopedia.sellerorder.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-09-18.
 */
data class SomListAllFilter (
        @SerializedName("data")
        @Expose
        val data: Data = Data()
) {
    data class Data (
            @SerializedName("orderShippingList")
            @Expose
            val orderShippingList: List<ShippingList> = listOf(),

            @SerializedName("order_filter_som_single")
            @Expose
            val orderFilterSomSingle: OrderFilterSomSingle = OrderFilterSomSingle(),

            @SerializedName("orderTypeList")
            @Expose
            val orderTypeList: List<OrderType> = listOf()
    ) {
        data class OrderFilterSomSingle (
                @SerializedName("status_list")
                @Expose
                val statusList: List<StatusList> = listOf()
        ) {
            data class StatusList (
                    @SerializedName("id")
                    @Expose
                    val id: Int = 0,

                    @SerializedName("value")
                    @Expose
                    val orderStatusIdList: List<Int> = listOf(),

                    @SerializedName("key")
                    @Expose
                    val key: String = "",

                    @SerializedName("text")
                    @Expose
                    val text: String = "",

                    @SerializedName("counter")
                    @Expose
                    val orderStatusAmount: Int = 0,

                    @SerializedName("type")
                    @Expose
                    val type: String = ""
            )
        }

        data class OrderType (
                @SerializedName("id")
                @Expose
                val id: Int = 0,

                @SerializedName("key")
                @Expose
                val key: String = "",

                @SerializedName("name")
                @Expose
                val name: String = ""
        )

        data class ShippingList (
                @SerializedName("shipping_id")
                @Expose
                val shippingId: Int = 0,

                @SerializedName("shipping_code")
                @Expose
                val shippingCode: String = "",

                @SerializedName("shipping_name")
                @Expose
                val shippingName: String = ""
        )
    }
}