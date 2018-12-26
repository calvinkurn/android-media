package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataResponseCheckPromoCodeFinal {

    @SerializedName("check_promo_final_v2")
    @Expose
    var checkPromoCartV2: ResponseCheckPromoCode? = null
}