package com.tokopedia.product.addedit.productlimitation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductAddRuleResponse {
    @SerializedName("ProductAddRule")
    @Expose
    var productAddRule: ProductAddRule = ProductAddRule()
}