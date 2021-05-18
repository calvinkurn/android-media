package com.tokopedia.product.addedit.productlimitation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductAddRule {
    @SerializedName("header")
    @Expose
    var header: Header = Header()

    @SerializedName("data")
    @Expose
    var data: ProductLimitationData = ProductLimitationData()
}