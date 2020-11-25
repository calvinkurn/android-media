package com.tokopedia.hotel.cancellation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 07/05/20
 */

data class HotelCancellationParam(
        @SerializedName("invoiceID")
        @Expose
        val invoiceId: String = ""
)