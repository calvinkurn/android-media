package com.tokopedia.logisticcart.scheduledelivery.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticcart.scheduledelivery.utils.StringFormatterHelper.appendHtmlBoldText
import com.tokopedia.logisticcart.scheduledelivery.utils.StringFormatterHelper.appendHtmlStrikethroughText
import kotlinx.parcelize.Parcelize

@Parcelize
class DeliveryProduct(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("text_eta")
    val textEta: String = "",
    @SerializedName("promo_text")
    val promoText: String = "",
    @SerializedName("timeslot_id")
    val timeslotId: Long = 0L,
    @SerializedName("available")
    val available: Boolean = false,
    @SerializedName("hidden")
    val hidden: Boolean = false,
    @SerializedName("recommend")
    val recommend: Boolean = false,
    @SerializedName("service_id")
    val serviceId: Long = 0L,
    @SerializedName("service_name")
    val serviceName: String = "",
    @SerializedName("shipper_id")
    val shipperId: Long = 0L,
    @SerializedName("shipper_name")
    val shipperName: String = "",
    @SerializedName("shipper_product_id")
    val shipperProductId: Long = 0L,
    @SerializedName("shipper_product_name")
    val shipperProductName: String = "",
    @SerializedName("final_price")
    val finalPrice: Double = 0.0,
    @SerializedName("real_price")
    val realPrice: Double = 0.0,
    @SerializedName("text_final_price")
    val textFinalPrice: String = "",
    @SerializedName("text_real_price")
    val textRealPrice: String = "",
    @SerializedName("checksum")
    val checksum: String = "",
    @SerializedName("insurance")
    val insurance: InsuranceData = InsuranceData(),
    @SerializedName("features")
    val features: Features = Features(),
    @SerializedName("ut")
    val ut: String = "",
    @SerializedName("promo_stacking")
    val promoStacking: PromoStacking = PromoStacking(),
    @SerializedName("validation_metadata")
    val validationMetadata: String = ""
) : Parcelable {
    fun getFormattedPrice(): String {
        return StringBuilder().apply {
            if (realPrice != finalPrice) {
                appendHtmlBoldText(" (${textFinalPrice} ")
                appendHtmlStrikethroughText(textRealPrice)
                appendHtmlBoldText(")")
            } else {
                appendHtmlBoldText(" (${textFinalPrice})")
            }

        }.toString()
    }
}
