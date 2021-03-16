package com.tokopedia.sellerorder.confirmshipping.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-22.
 */
data class SomCourierList (
        @SerializedName("data")
        @Expose
        val data: Data = Data()) {

    data class Data(
            @SerializedName("mpLogisticGetEditShippingForm")
            @Expose
            val mpLogisticGetEditShippingForm: MpLogisticGetEditShippingForm = MpLogisticGetEditShippingForm()) {

        data class MpLogisticGetEditShippingForm(
                @SerializedName("data")
                @Expose
                val dataShipment: DataShipment = DataShipment(),

                @SerializedName("status")
                @Expose
                val status: String = "") {

            data class DataShipment(
                    @SerializedName("shipment")
                    @Expose
                    val listShipment: List<Shipment> = listOf()) {

                data class Shipment(
                        @SerializedName("shipping_max_add_fee")
                        @Expose
                        val shippingMaxAddFee: String = "0",

                        @SerializedName("shipment_id")
                        @Expose
                        val shipmentId: String = "0",

                        @SerializedName("shipment_available")
                        @Expose
                        val shipmentAvailable: Int = 0,

                        @SerializedName("shipment_image")
                        @Expose
                        val shipmentImage: String = "",

                        @SerializedName("shipment_name")
                        @Expose
                        val shipmentName: String = "",

                        @SerializedName("shipment_package")
                        @Expose
                        val listShipmentPackage: List<ShipmentPackage> = listOf()) {

                    data class ShipmentPackage(
                            @SerializedName("desc")
                            @Expose
                            val desc: String = "",

                            @SerializedName("active")
                            @Expose
                            val active: Int = 0,

                            @SerializedName("name")
                            @Expose
                            val name: String = "",

                            @SerializedName("sp_id")
                            @Expose
                            val spId: String = "")
                }
            }
        }
    }
}