package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewImageUrl(
    @SerializedName("attachment_id")
    @Expose
    val attachmentId: Long = 0,

    @SerializedName("description")
    @Expose
    val description: String = "",

    @SerializedName("uri_thumbnail")
    @Expose
    val uriThumbnail: String = "",

    @SerializedName("uri_large")
    @Expose
    val uriLarge: String = ""
)