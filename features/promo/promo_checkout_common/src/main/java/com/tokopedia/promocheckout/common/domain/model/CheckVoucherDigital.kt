package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckVoucherDigital (

    @SerializedName("data")
    @Expose
    val voucherData: CheckVoucherDigitalData = CheckVoucherDigitalData(),

    @SerializedName("errors")
    @Expose
    val errors: List<Error> = listOf()

) {
    class Response(
            @SerializedName("rechargeCheckVoucher")
            @Expose
            val response: CheckVoucherDigital = CheckVoucherDigital()
    )

    class Error(
            @SerializedName("status")
            @Expose
            val status: String = "",

            @SerializedName("title")
            @Expose
            val title: String = ""
    )
}