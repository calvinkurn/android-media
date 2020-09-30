package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Paging {
    @SerializedName("uri_next")
    @Expose
    var uriNext: String? = null
    @SerializedName("uri_previous")
    @Expose
    var uriPrevious: String? = null

}