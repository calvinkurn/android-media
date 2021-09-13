package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/15/17.
 */
class ReputationDataDomain constructor(
    val revieweeScore: Int, val revieweeScoreStatus: Int,
    val isShowRevieweeScore: Boolean, val reviewerScore: Int,
    val reviewerScoreStatus: Int, val isEditable: Boolean,
    val isInserted: Boolean, val isLocked: Boolean,
    val isAutoScored: Boolean, val isCompleted: Boolean,
    val isShowLockingDeadline: Boolean, val lockingDeadlineDays: Int,
    val isShowBookmark: Boolean, val actionMessage: String?
)