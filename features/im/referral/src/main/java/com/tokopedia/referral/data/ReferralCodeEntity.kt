package com.tokopedia.referral.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ashwanityagi on 08/11/17.
 */

data class ReferralCodeEntity (

    @SerializedName("promo_content")
    @Expose
    var promoContent: PromoContent? = null,
    @SerializedName("promo_benefit")
    @Expose
    var promoBenefit: PromoBenefit? = null,
    @SerializedName("error")
    @Expose
    var erorMessage: String? = null
)