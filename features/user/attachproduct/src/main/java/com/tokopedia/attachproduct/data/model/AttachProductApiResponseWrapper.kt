package com.tokopedia.attachproduct.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttachProductApiResponseWrapper {
    @SerializedName("products")
    @Expose
    var products: List<DataProductResponse> = listOf()
}
