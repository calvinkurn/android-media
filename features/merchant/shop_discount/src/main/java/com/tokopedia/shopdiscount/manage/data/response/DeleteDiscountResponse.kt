package com.tokopedia.shopdiscount.manage.data.response


import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class DeleteDiscountResponse(
    @SerializedName("doSlashPriceStop")
    val doSlashPriceStop: DoSlashPriceStop = DoSlashPriceStop()
) {
    data class DoSlashPriceStop(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader()
    )
}