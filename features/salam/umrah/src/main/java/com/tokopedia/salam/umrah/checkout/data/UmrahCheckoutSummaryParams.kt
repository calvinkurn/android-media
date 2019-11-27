package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UmrahCheckoutSummaryParams (
        val productVariantId: String = "",
        val pilgrimsCount: Int = 0
)