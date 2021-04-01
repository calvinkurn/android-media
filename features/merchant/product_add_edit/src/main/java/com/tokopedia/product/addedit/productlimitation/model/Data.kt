package com.tokopedia.product.addedit.productlimitation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductLimitationData {
    @SerializedName("eligible")
    @Expose
    var eligible: Eligible? = null
}