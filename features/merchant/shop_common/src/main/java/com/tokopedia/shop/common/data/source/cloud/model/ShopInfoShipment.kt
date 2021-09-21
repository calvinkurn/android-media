package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoShipment {
    @SerializedName("shipment_available")
    @Expose
    var shipmentAvailable: Long = 0

    @SerializedName("shipment_code")
    @Expose
    var shipmentCode: String? = null

    @SerializedName("shipment_id")
    @Expose
    var shipmentId: String? = null

    @SerializedName("shipment_image")
    @Expose
    var shipmentImage: String? = null

    @SerializedName("shipment_is_pickup")
    @Expose
    var shipmentIsPickup: Long = 0

    @SerializedName("shipment_name")
    @Expose
    var shipmentName: String? = null

    @SerializedName("shipment_package")
    @Expose
    var shipmentPackage: List<ShopInfoShipmentPackage>? = null

    @SerializedName("shipping_max_add_fee")
    @Expose
    var shippingMaxAddFee: Long = 0
}