package com.tokopedia.sellerorder.reschedule_pickup.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetReschedulePickupResponse(
    @Expose
    @SerializedName("data")
    val data: Data = Data()

) {
    data class Data(
        @Expose
        @SerializedName("mpLogisticGetReschedulePickup")
        val mpLogisticGetReschedulePickup: MpLogisticGetReschedulePickup = MpLogisticGetReschedulePickup()

    ) {
        data class MpLogisticGetReschedulePickup(
            @Expose
            @SerializedName("order_detail_ticker")
            val orderDetailTicker: String = "",

            @Expose
            @SerializedName("data")
            val data: List<DataItem> = emptyList()

        ) {
            data class DataItem(
                @Expose
                @SerializedName("order_data")
                val orderData: List<OrderData> = emptyList(),

                @SuppressLint("Invalid Data Type")
                @Expose
                @SerializedName("shipper_id")
                val shipperId: Long = 0,

                @Expose
                @SerializedName("shipper_name")
                val shipperName: String = ""

            ) {
                data class OrderData(

                    @Expose
                    @SerializedName("error_message")
                    val errorMessage: String = "",

                    @Expose
                    @SerializedName("shipper_product_name")
                    val shipperProductName: String = "",

                    @SuppressLint("Invalid Data Type")
                    @Expose
                    @SerializedName("shipper_product_id")
                    val shipperProductId: Long = 0,

                    @Expose
                    @SerializedName("order_item")
                    val orderItem: List<OrderItem> = emptyList(),

                    @Expose
                    @SerializedName("choose_day")
                    val chooseDay: List<DayOption> = emptyList(),

                    @Expose
                    @SerializedName("invoice")
                    val invoice: String = "",

                    @SuppressLint("Invalid Data Type")
                    @Expose
                    @SerializedName("order_id")
                    val orderId: Long = 0,

                    @Expose
                    @SerializedName("choose_reason")
                    val chooseReason: List<ReasonOption> = listOf()

                ) {
                    data class OrderItem(
                        @Expose
                        @SerializedName("qty")
                        val qty: Long = 0,

                        @Expose
                        @SerializedName("name")
                        val name: String = ""

                    )

                    data class DayOption(
                        @Expose
                        @SerializedName("day")
                        val day: String = "",

                        @Expose
                        @SerializedName("choose_time")
                        val chooseTime: List<TimeOption> = emptyList()
                    ) {
                        data class TimeOption(
                            @Expose
                            @SerializedName("time")
                            val time: String = "",

                            @Expose
                            @SerializedName("eta_pickup")
                            val etaPickup: String = "",
                        )
                    }

                    data class ReasonOption(
                        @Expose
                        @SerializedName("reason")
                        val reason: String = ""
                    ) {
                    }
                }
            }
        }

    }
}
