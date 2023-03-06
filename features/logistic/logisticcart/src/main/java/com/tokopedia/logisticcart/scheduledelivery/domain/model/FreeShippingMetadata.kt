package com.tokopedia.logisticcart.scheduledelivery.domain.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class FreeShippingMetadata(
    @SerializedName("sent_shipper_partner")
    val sentShipperPartner: Boolean = true,
    @SerializedName("benefit_class")
    val benefitClass: String = "",
    @SerializedName("shipping_subsidy")
    val shippingSubsidy: Long = 0,
    @SerializedName("additional_data")
    val additionalData: String = ""
) : Parcelable {

    fun toJson(): String {
        return Gson().toJson(this)
    }
}
