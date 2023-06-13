package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoAttachmentDomain(
    @SerializedName("attachmentID") @Expose val attachmentId: String = "",
    @SerializedName("videoUrl") @Expose val videoUrl: String = ""
)
