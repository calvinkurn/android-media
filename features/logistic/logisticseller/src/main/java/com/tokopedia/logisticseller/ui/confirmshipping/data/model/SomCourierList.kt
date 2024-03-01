package com.tokopedia.logisticseller.ui.confirmshipping.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-22.
 */
data class SomCourierList(
    @SerializedName("data")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("mpLogisticGetEditShippingForm")
        val mpLogisticGetEditShippingForm: MpLogisticGetEditShippingForm = MpLogisticGetEditShippingForm()
    ) {

        data class MpLogisticGetEditShippingForm(
            @SerializedName("data")
            val dataShipment: DataShipment = DataShipment(),

            @SerializedName("status")
            val status: String = ""
        ) {

            data class DataShipment(
                @SerializedName("shipment")
                val listShipment: List<Shipment> = listOf(),

                @SerializedName("ticker_unification_params")
                val tickerUnificationParams: TickerUnificationParams = TickerUnificationParams()
            ) {

                data class TickerUnificationParams(

                    @SerializedName("page")
                    val page: String = "",

                    @SerializedName("target")
                    val target: List<TickerUnificationTargets> = listOf(),

                    @SerializedName("template")
                    val template: Template = Template()

                ) {
                    data class Template(
                        @SerializedName("contents")
                        val contents: List<Content> = listOf()
                    ) {
                        data class Content(
                            @SerializedName("key")
                            val key: String = "",

                            @SerializedName("value")
                            val value: String = ""
                        )
                    }
                }

                data class TickerUnificationTargets(
                    @SerializedName("type")
                    val type: String = "",

                    @SerializedName("values")
                    val values: List<String> = listOf()
                )

                data class Shipment(
                    @SerializedName("shipping_max_add_fee")
                    val shippingMaxAddFee: String = "0",

                    @SerializedName("shipment_id")
                    val shipmentId: String = "0",

                    @SerializedName("shipment_available")
                    val shipmentAvailable: Int = 0,

                    @SerializedName("shipment_image")
                    val shipmentImage: String = "",

                    @SerializedName("shipment_name")
                    val shipmentName: String = "",

                    @SerializedName("shipment_package")
                    val listShipmentPackage: List<ShipmentPackage> = listOf()
                ) {
                    override fun toString(): String {
                        return shipmentName
                    }
                    data class ShipmentPackage(
                        @SerializedName("desc")
                        val desc: String = "",

                        @SerializedName("active")
                        val active: Int = 0,

                        @SerializedName("name")
                        val name: String = "",

                        @SerializedName("sp_id")
                        val spId: String = ""
                    ) {
                        override fun toString(): String {
                            return name
                        }
                    }
                }
            }
        }
    }
}
