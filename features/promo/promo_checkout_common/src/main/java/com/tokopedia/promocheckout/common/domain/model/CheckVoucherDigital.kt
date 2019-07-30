package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckVoucherDigital (

    @SerializedName("data")
    @Expose
    var voucherData: CheckVoucherDigitalData = CheckVoucherDigitalData(),

    @SerializedName("errors")
    @Expose
    var errors: List<Error> = listOf()

) {
    class Response(
            @SerializedName("rechargeCheckVoucher")
            @Expose
            var response: CheckVoucherDigital = CheckVoucherDigital()
    )

    class Error(
            @SerializedName("status")
            @Expose
            var status: String = "",

            @SerializedName("title")
            @Expose
            var title: String = ""
    )
}