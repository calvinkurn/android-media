package com.tokopedia.sellerorder.list.domain.model

import android.annotation.SuppressLint
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
            @SerializedName("quick_filter_list")
            @Expose
            val quickFilterList: List<QuickFilter> = listOf(),
            @SerializedName("status_list")
            @Expose
            val statusList: List<Status> = listOf(),
            @SerializedName("sort_by")
            @Expose
            val sortByList: List<SortBy> = listOf(),
            @Expose
            @SerializedName("highlighted_status_key")
            val highLightedStatusKey: String? = ""
        ) {
            data class QuickFilter(
                @SerializedName("id")
                @Expose
                val id: String = "0",
                @SerializedName("key")
                @Expose
                val key: String = "",
                @SerializedName("text")
                @Expose
                val text: String = "",
                @SerializedName("type")
                @Expose
                val type: String = ""
            )

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
                @SuppressLint("Invalid Data Type")
                @SerializedName("order_status_id")
                @Expose
                val id: List<Int> = listOf()
            ) {
                data class ChildStatus(
                    @SerializedName("amount")
                    @Expose
                    val amount: Int = 0,
                    @SuppressLint("Invalid Data Type")
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

            data class SortBy(
                @SerializedName("text")
                @Expose
                val text: String = "",
                @SerializedName("value")
                @Expose
                val value: Long = 0L
            )
        }
    }
}
