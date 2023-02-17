package com.tokopedia.logisticcart.scheduledelivery.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PromoStacking(
    @SerializedName("promo_code")
    val promoCode: String = "",
    @SerializedName("promo_chargeable")
    val promoChargeable: Boolean = false,
    @SerializedName("benefit_class")
    val benefitClass: String = "",
    @SerializedName("is_bebas_ongkir_extra")
    val isBebasOngkirExtra: Boolean = false,
    @SerializedName("benefit_amount")
    val benefitAmount: Long = 0,
    @SerializedName("bo_campaign_id")
    val boCampaignId: Long = 0,
    @SerializedName("free_shipping_metadata")
    val freeShippingMetadata: FreeShippingMetadata = FreeShippingMetadata(),
    @SerializedName("disabled")
    val disabled: Boolean = false
) : Parcelable
