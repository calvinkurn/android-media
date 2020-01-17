package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutSummaryParams (
        @SerializedName("productVariantId")
        @Expose
        val productVariantId: String = "",
        @SerializedName("pilgrimsCount")
        @Expose
        val pilgrimsCount: Int = 0
)