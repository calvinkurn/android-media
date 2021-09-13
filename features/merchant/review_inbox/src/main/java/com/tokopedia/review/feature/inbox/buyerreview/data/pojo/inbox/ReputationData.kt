package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReputationData {
    @SerializedName("reviewee_score")
    @Expose
    var revieweeScore = 0

    @SerializedName("reviewee_score_status")
    @Expose
    var revieweeScoreStatus = 0

    @SerializedName("show_reviewee_score")
    @Expose
    var isShowRevieweeScore = false

    @SerializedName("reviewer_score")
    @Expose
    var reviewerScore = 0

    @SerializedName("reviewer_score_status")
    @Expose
    var reviewerScoreStatus = 0

    @SerializedName("is_editable")
    @Expose
    var isIsEditable = false
        private set

    @SerializedName("is_inserted")
    @Expose
    var isIsInserted = false
        private set

    @SerializedName("is_locked")
    @Expose
    var isIsLocked = false
        private set

    @SerializedName("is_auto_scored")
    @Expose
    var isIsAutoScored = false
        private set

    @SerializedName("is_completed")
    @Expose
    var isIsCompleted = false
        private set

    @SerializedName("show_locking_deadline")
    @Expose
    var isShowLockingDeadline = false

    @SerializedName("locking_deadline_days")
    @Expose
    var lockingDeadlineDays = 0

    @SerializedName("show_bookmark")
    @Expose
    var isShowBookmark = false

    @SerializedName("action_message")
    @Expose
    var actionMessage: String? = null
    fun setIsEditable(isEditable: Boolean) {
        isIsEditable = isEditable
    }

    fun setIsInserted(isInserted: Boolean) {
        isIsInserted = isInserted
    }

    fun setIsLocked(isLocked: Boolean) {
        isIsLocked = isLocked
    }

    fun setIsAutoScored(isAutoScored: Boolean) {
        isIsAutoScored = isAutoScored
    }

    fun setIsCompleted(isCompleted: Boolean) {
        isIsCompleted = isCompleted
    }
}