package com.tokopedia.product.addedit.productlimitation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Eligible {
    @SerializedName("value")
    @Expose
    var value: Boolean? = null

    @SerializedName("totalProduct")
    @Expose
    var totalProduct: Int? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("isUnlimited")
    @Expose
    var isUnlimited: Boolean? = null

    @SerializedName("actionItems")
    @Expose
    var actionItems: List<String> = emptyList()
}