package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TelcoPromo(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("img_url")
        @Expose
        val urlBannerPromo: String,
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("subtitle")
        @Expose
        val subtitle: String,
        @SerializedName("promo_code")
        @Expose
        val promoCode: String,
        var voucherCodeCopied: Boolean = false
)