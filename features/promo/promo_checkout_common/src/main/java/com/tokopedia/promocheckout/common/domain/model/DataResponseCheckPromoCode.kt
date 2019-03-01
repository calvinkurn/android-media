package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataResponseCheckPromoCode {

    @SerializedName("check_promo_cart_v2")
    @Expose
    var checkPromoCartV2: ResponseCheckPromoCode = ResponseCheckPromoCode()
}