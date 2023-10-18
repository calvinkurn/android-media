package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/23/17.
 */
class ReviewDataDomain constructor(
    @SerializedName("feedbackID") @Expose val feedbackID: String = "",
    @SerializedName("message") @Expose val reviewMessage: String,
    @SerializedName("rating") @Expose val reviewRating: Int,
    @SerializedName("imageAttachments") @Expose val imageAttachments: List<ImageAttachmentDomain> = listOf(),
    @SerializedName("videoAttachments") @Expose val videoAttachments: List<VideoAttachmentDomain> = listOf(),
    @SerializedName("createTime") @Expose val reviewCreateTime: String,
    @SerializedName("updateTime") @Expose val reviewUpdateTime: String,
    @SerializedName("anonymity") @Expose val isReviewAnonymity: Boolean,
    @SerializedName("responseMessage") @Expose val responseMessage: String,
    @SerializedName("responseTime") @Expose val responseTime: String
)
