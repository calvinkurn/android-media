package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Suppress("EnumEntryName")
enum class CheckoutDisabledFeatures {
    @SerializedName("dropshipper")
    @Expose
    dropshipper,
    @SerializedName("multi_address")
    @Expose
    multiAddress,
    @SerializedName("egold")
    @Expose
    egold,
    @SerializedName("ppp")
    @Expose
    ppp,
    @SerializedName("order_prioritas")
    @Expose
    orderPrioritas,
    @SerializedName("donation")
    @Expose
    donation
}