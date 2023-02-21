package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/23/17.
 */
class ImageAttachmentDomain constructor(
    @SerializedName("attachmentID") @Expose val attachmentId: String,
    @SerializedName("imageThumbnailUrl") @Expose val uriThumbnail: String,
    @SerializedName("imageUrl") @Expose val uriLarge: String
)
