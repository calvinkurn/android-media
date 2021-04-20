package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListFilterResponse(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("orderFilterSom")
            @Expose
            val orderFilterSom: OrderFilterSom = OrderFilterSom()
    ) {
        data class OrderFilterSom(
                @SerializedName("status_list")
                @Expose
                val statusList: List<Status> = listOf()
        ) {
            data class Status(
                    @SerializedName("child_status")
                    @Expose
                    val childStatuses: List<ChildStatus> = listOf(),
                    @SerializedName("is_checked")
                    @Expose
                    val isChecked: Boolean = false,
                    @SerializedName("key")
                    @Expose
                    val key: String = "",
                    @SerializedName("order_status")
                    @Expose
                    val status: String = "",
                    @SerializedName("order_status_amount")
                    @Expose
                    val amount: Int = 0,
                    @SerializedName("order_status_id")
                    @Expose
                    val id: List<Int> = listOf()
            ) {
                data class ChildStatus(
                        @SerializedName("amount")
                        @Expose
                        val amount: Int = 0,
                        @SerializedName("id")
                        @Expose
                        val id: List<Int> = listOf(),
                        @SerializedName("is_checked")
                        @Expose
                        val isChecked: Boolean = false,
                        @SerializedName("key")
                        @Expose
                        val key: String = "",
                        @SerializedName("text")
                        @Expose
                        val text: String = ""
                )
            }
        }
    }
}