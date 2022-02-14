package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReputationData(
    @SerializedName("reviewee_score")
    @Expose
    val revieweeScore: Int = 0,

    @SerializedName("reviewee_score_status")
    @Expose
    val revieweeScoreStatus: Int = 0,

    @SerializedName("show_reviewee_score")
    @Expose
    val isShowRevieweeScore: Boolean = false,

    @SerializedName("reviewer_score")
    @Expose
    val reviewerScore: Int = 0,

    @SerializedName("reviewer_score_status")
    @Expose
    val reviewerScoreStatus: Int = 0,

    @SerializedName("is_editable")
    @Expose
    val isIsEditable: Boolean = false,

    @SerializedName("is_inserted")
    @Expose
    val isIsInserted: Boolean = false,

    @SerializedName("is_locked")
    @Expose
    val isIsLocked: Boolean = false,

    @SerializedName("is_auto_scored")
    @Expose
    val isIsAutoScored: Boolean = false,

    @SerializedName("is_completed")
    @Expose
    val isIsCompleted: Boolean = false,

    @SerializedName("show_locking_deadline")
    @Expose
    val isShowLockingDeadline: Boolean = false,

    @SerializedName("locking_deadline_days")
    @Expose
    val lockingDeadlineDays: Int = 0,

    @SerializedName("show_bookmark")
    @Expose
    val isShowBookmark: Boolean = false,

    @SerializedName("action_message")
    @Expose
    val actionMessage: String = ""
)