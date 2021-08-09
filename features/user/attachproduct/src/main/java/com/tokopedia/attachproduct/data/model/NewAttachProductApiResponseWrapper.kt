package com.tokopedia.attachproduct.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewAttachProductAPIResponseWrapper {
    @SerializedName("products")
    @Expose
    var products: List<NewDataProductResponse>? = null
}
