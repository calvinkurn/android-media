package com.tokopedia.discovery2.data.discoverypageresponse

import com.google.gson.annotations.SerializedName

data class DiscoveryInjectCouponResponse(

        @SerializedName("SetInjectCouponTimeBasedResponse")
        val setInjectCouponData: SetInjectCouponModel? = null
) {
    data class SetInjectCouponModel(
            @SerializedName("is_success")
            val isSuccess: Boolean? =  false,
            @SerializedName("error_message")
            val rewardPoints: String? = ""
    )
}