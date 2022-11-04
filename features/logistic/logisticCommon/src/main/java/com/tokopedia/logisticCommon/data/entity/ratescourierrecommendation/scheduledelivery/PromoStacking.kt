package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

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
    @SerializedName("shipping_subsidy")
    val shippingSubsidy: Int = 0,
) : Parcelable
