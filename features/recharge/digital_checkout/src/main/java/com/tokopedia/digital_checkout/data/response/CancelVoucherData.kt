package com.tokopedia.digital_checkout.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 14/01/21
 */

data class CancelVoucherData(
        @SerializedName("Success")
        @Expose
        val success: Boolean = false
) {
    data class Response(
            @SerializedName("clearCacheAutoApplyV2")
            @Expose
            val response: CancelVoucherData
    )
}