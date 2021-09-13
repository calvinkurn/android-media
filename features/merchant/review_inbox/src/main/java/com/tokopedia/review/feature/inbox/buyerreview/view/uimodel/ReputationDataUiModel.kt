package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 8/28/17.
 */
class ReputationDataUiModel : Parcelable {
    var revieweeScore: Int
        private set
    var revieweeScoreStatus: Int
        private set
    var isShowRevieweeScore: Boolean
        private set
    var reviewerScore: Int
    var reviewerScoreStatus: Int
        private set
    var isEditable: Boolean
    var isInserted: Boolean
    var isLocked: Boolean
        private set
    var isAutoScored: Boolean
        private set
    var isCompleted: Boolean
        private set
    var isShowLockingDeadline: Boolean
        private set
    var lockingDeadlineDays: Int
        private set
    var isShowBookmark: Boolean
        private set
    var actionMessage: String?
        private set

    constructor(
        revieweeScore: Int, revieweeScoreStatus: Int,
        showRevieweeScore: Boolean, reviewerScore: Int,
        reviewerScoreStatus: Int, isEditable: Boolean,
        isInserted: Boolean, isLocked: Boolean, isAutoScored: Boolean,
        isCompleted: Boolean, showLockingDeadline: Boolean,
        lockingDeadlineDays: Int, showBookmark: Boolean,
        actionMessage: String?
    ) {
        this.revieweeScore = revieweeScore
        this.revieweeScoreStatus = revieweeScoreStatus
        isShowRevieweeScore = showRevieweeScore
        this.reviewerScore = reviewerScore
        this.reviewerScoreStatus = reviewerScoreStatus
        this.isEditable = isEditable
        this.isInserted = isInserted
        this.isLocked = isLocked
        this.isAutoScored = isAutoScored
        this.isCompleted = isCompleted
        isShowLockingDeadline = showLockingDeadline
        this.lockingDeadlineDays = lockingDeadlineDays
        isShowBookmark = showBookmark
        this.actionMessage = actionMessage
    }

    protected constructor(`in`: Parcel) {
        revieweeScore = `in`.readInt()
        revieweeScoreStatus = `in`.readInt()
        isShowRevieweeScore = `in`.readByte().toInt() != 0
        reviewerScore = `in`.readInt()
        reviewerScoreStatus = `in`.readInt()
        isEditable = `in`.readByte().toInt() != 0
        isInserted = `in`.readByte().toInt() != 0
        isLocked = `in`.readByte().toInt() != 0
        isAutoScored = `in`.readByte().toInt() != 0
        isCompleted = `in`.readByte().toInt() != 0
        isShowLockingDeadline = `in`.readByte().toInt() != 0
        lockingDeadlineDays = `in`.readInt()
        isShowBookmark = `in`.readByte().toInt() != 0
        actionMessage = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(revieweeScore)
        dest.writeInt(revieweeScoreStatus)
        dest.writeByte((if (isShowRevieweeScore) 1 else 0).toByte())
        dest.writeInt(reviewerScore)
        dest.writeInt(reviewerScoreStatus)
        dest.writeByte((if (isEditable) 1 else 0).toByte())
        dest.writeByte((if (isInserted) 1 else 0).toByte())
        dest.writeByte((if (isLocked) 1 else 0).toByte())
        dest.writeByte((if (isAutoScored) 1 else 0).toByte())
        dest.writeByte((if (isCompleted) 1 else 0).toByte())
        dest.writeByte((if (isShowLockingDeadline) 1 else 0).toByte())
        dest.writeInt(lockingDeadlineDays)
        dest.writeByte((if (isShowBookmark) 1 else 0).toByte())
        dest.writeString(actionMessage)
    }

    companion object {
        val CREATOR: Parcelable.Creator<ReputationDataUiModel> =
            object : Parcelable.Creator<ReputationDataUiModel?> {
                override fun createFromParcel(`in`: Parcel): ReputationDataUiModel? {
                    return ReputationDataUiModel(`in`)
                }

                override fun newArray(size: Int): Array<ReputationDataUiModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}