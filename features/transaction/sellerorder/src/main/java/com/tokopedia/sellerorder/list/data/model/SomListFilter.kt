package com.tokopedia.sellerorder.list.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-29.
 */
data class SomListFilter (
        @SerializedName("data")
        @Expose
        val data: Data = Data()
) {
    data class Data (
            @SerializedName("orderFilterSom")
            @Expose
            val orderFilterSom: OrderFilterSom = OrderFilterSom()
    ) {
        data class OrderFilterSom (
                @SerializedName("status_list")
                @Expose
                val statusList: List<StatusList> = listOf()
        ) {
            data class StatusList (
                    @SerializedName("order_status_id")
                    @Expose
                    val orderStatusIdList: List<Int> = listOf(),

                    @SerializedName("key")
                    @Expose
                    val key: String = "",

                    @SerializedName("order_status")
                    @Expose
                    val orderStatus: String = "",

                    @SerializedName("order_status_amount")
                    @Expose
                    val orderStatusAmount: Int = 0,

                    @SerializedName("is_checked")
                    @Expose
                    val isChecked: Boolean = false,

                    @SerializedName("child_status")
                    @Expose
                    val childStatusList: List<ChildStatus> = listOf()
            ) {
                data class ChildStatus (
                       @SerializedName("id")
                       @Expose
                       val childId: List<Int> = listOf(),

                       @SerializedName("key")
                       @Expose
                       val childKey: String = "",

                       @SerializedName("text")
                       @Expose
                       val childText: String = "",

                       @SerializedName("amount")
                       @Expose
                       val childAmount: Int = 0
                )
            }
        }
    }
}