package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

/**
 * @author by nisie on 8/28/17.
 */
data class ReputationDataUiModel(
    var revieweeScore: Int = 0,
    var revieweeScoreStatus: Int = 0,
    var isShowRevieweeScore: Boolean = false,
    var reviewerScore: Int = 0,
    var reviewerScoreStatus: Int = 0,
    var isEditable: Boolean = false,
    var isInserted: Boolean = false,
    var isLocked: Boolean = false,
    var isAutoScored: Boolean = false,
    var isCompleted: Boolean = false,
    var isShowLockingDeadline: Boolean = false,
    var lockingDeadlineDays: Int = 0,
    var isShowBookmark: Boolean = false,
    var actionMessage: String = ""
)