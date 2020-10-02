package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewImageAttachment {
    @SerializedName("attachment_id")
    @Expose
    var attachmentId = 0
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("uri_thumbnail")
    @Expose
    var uriThumbnail: String? = null
    @SerializedName("uri_large")
    @Expose
    var uriLarge: String? = null

}