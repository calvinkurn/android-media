package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.SerializedName

data class AttachmentItem(
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("url")
    var url: String? = null
)
