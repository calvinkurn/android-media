package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 8/23/17.
 */
class ReviewDataDomain constructor(
    @SerializedName("reviewIdStr") @Expose val reviewId: String = "",
    @SerializedName("reputationIdStr") @Expose val reputationId: String = "",
    @SerializedName("message") @Expose val reviewMessage: String,
    @SerializedName("rating") @Expose val reviewRating: Int,
    @SerializedName("attachments") @Expose val imageAttachments: List<ImageAttachmentDomain> = listOf(),
    @SerializedName("videoAttachments") @Expose val videoAttachments: List<VideoAttachmentDomain> = listOf(),
    @SerializedName("createTime") @Expose val reviewCreateTime: ReviewCreateTimeDomain,
    @SerializedName("updateTime") @Expose val reviewUpdateTime: ReviewUpdateTimeDomain,
    @SerializedName("anonymity") @Expose val isReviewAnonymity: Boolean,
    @SerializedName("response") @Expose val reviewResponse: ReviewResponseDomain
)