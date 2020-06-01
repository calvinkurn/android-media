package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

data class AttachmentItem(
        @SerializedName("thumbnail")
        var thumbnail: String? = null,
        @SerializedName("url")
        var url: String? = null)

