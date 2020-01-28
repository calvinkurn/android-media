package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckUmrahPromoCode(
        @SerializedName("data")
        @Expose
        val umrahPromoData: CheckUmrahPromoCodeData = CheckUmrahPromoCodeData()
) {
    data class Response(
            @SerializedName("umrahPromoCheck")
            @Expose
            val umrahPromoCheck: CheckUmrahPromoCode = CheckUmrahPromoCode()
    )
}