package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class FreeShippingMetadata(
    @SerializedName("sent_shipper_partner")
    val sentShipperPartner: Boolean = false,
    @SerializedName("benefit_class")
    val benefitClass: String = "",
    @SerializedName("shipping_subsidy")
    val shippingSubsidy: Long = 0,
    @SerializedName("additional_data")
    val additionalData: String = ""
)
