package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 24/04/20
 */

data class TokopointsSumCoupon (
    @SerializedName("sumCoupon")
    @Expose
    val sumCoupon: Int = 0,

    @SerializedName("sumCouponStr")
    @Expose
    val sumCouponStr: String = "",

    @SerializedName("sumCouponUnitOpt")
    @Expose
    val sumCouponUnitOpt: String = ""
){
    data class Response(
            @SerializedName("tokopointsSumCoupon")
            @Expose
            val response: TokopointsSumCoupon = TokopointsSumCoupon()
    )
}