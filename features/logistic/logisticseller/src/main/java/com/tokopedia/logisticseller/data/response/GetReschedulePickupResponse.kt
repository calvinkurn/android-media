package com.tokopedia.logisticseller.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetReschedulePickupResponse(
    @SerializedName("data")
    val data: Data = Data()

) {
    data class Data(
        @SerializedName("mpLogisticGetReschedulePickup")
        val mpLogisticGetReschedulePickup: MpLogisticGetReschedulePickup = MpLogisticGetReschedulePickup()

    ) {
        data class MpLogisticGetReschedulePickup(
            @SerializedName("order_detail_ticker")
            val orderDetailTicker: String = "",

            @SerializedName("app_link")
            val appLink: String = "",

            @SerializedName("data")
            val data: List<DataItem> = emptyList()

        ) {
            data class DataItem(
                @SerializedName("order_data")
                val orderData: List<OrderData> = emptyList(),

                @SuppressLint("Invalid Data Type")
                @SerializedName("shipper_id")
                val shipperId: Long = 0,

                @SerializedName("shipper_name")
                val shipperName: String = ""

            ) {
                data class OrderData(

                    @SerializedName("error_message")
                    val errorMessage: String = "",

                    @SerializedName("shipper_product_name")
                    val shipperProductName: String = "",

                    @SuppressLint("Invalid Data Type")
                    @SerializedName("shipper_product_id")
                    val shipperProductId: Long = 0,

                    @SerializedName("order_item")
                    val orderItem: List<OrderItem> = emptyList(),

                    @SerializedName("choose_day")
                    val chooseDay: List<DayOption> = emptyList(),

                    @SerializedName("invoice")
                    val invoice: String = "",

                    @SuppressLint("Invalid Data Type")
                    @SerializedName("order_id")
                    val orderId: Long = 0,

                    @SerializedName("choose_reason")
                    val chooseReason: List<ReasonOption> = listOf()

                ) {
                    data class OrderItem(
                        @SerializedName("qty")
                        val qty: Long = 0,

                        @SerializedName("name")
                        val name: String = ""

                    )

                    data class DayOption(
                        @SerializedName("day")
                        val day: String = "",

                        @SerializedName("choose_time")
                        val chooseTime: List<TimeOption> = emptyList()
                    ) {
                        data class TimeOption(
                            @SerializedName("time")
                            val time: String = "",

                            @SerializedName("eta_pickup")
                            val etaPickup: String = "",
                        )
                    }

                    data class ReasonOption(
                        @SerializedName("reason")
                        val reason: String = ""
                    ) {
                    }
                }
            }
        }

    }
}
