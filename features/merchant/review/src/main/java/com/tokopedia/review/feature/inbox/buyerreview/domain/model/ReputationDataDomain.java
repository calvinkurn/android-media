package com.tokopedia.review.feature.inbox.buyerreview.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 8/15/17.
 */

public class ReputationDataDomain {

    private int revieweeScore;
    private int revieweeScoreStatus;
    private boolean showRevieweeScore;
    private int reviewerScore;
    private int reviewerScoreStatus;
    private boolean isEditable;
    private boolean isInserted;
    private boolean isLocked;
    private boolean isAutoScored;
    private boolean isCompleted;
    private boolean showLockingDeadline;
    private int lockingDeadlineDays;
    private boolean showBookmark;
    private String actionMessage;

    public ReputationDataDomain(int revieweeScore, int revieweeScoreStatus,
                                boolean showRevieweeScore, int reviewerScore,
                                int reviewerScoreStatus, boolean isEditable,
                                boolean isInserted, boolean isLocked,
                                boolean isAutoScored, boolean isCompleted,
                                boolean showLockingDeadline, int lockingDeadlineDays,
                                boolean showBookmark, String actionMessage) {
        this.revieweeScore = revieweeScore;
        this.revieweeScoreStatus = revieweeScoreStatus;
        this.showRevieweeScore = showRevieweeScore;
        this.reviewerScore = reviewerScore;
        this.reviewerScoreStatus = reviewerScoreStatus;
        this.isEditable = isEditable;
        this.isInserted = isInserted;
        this.isLocked = isLocked;
        this.isAutoScored = isAutoScored;
        this.isCompleted = isCompleted;
        this.showLockingDeadline = showLockingDeadline;
        this.lockingDeadlineDays = lockingDeadlineDays;
        this.showBookmark = showBookmark;
        this.actionMessage = actionMessage;
    }

    public int getRevieweeScore() {
        return revieweeScore;
    }

    public int getRevieweeScoreStatus() {
        return revieweeScoreStatus;
    }

    public boolean isShowRevieweeScore() {
        return showRevieweeScore;
    }

    public int getReviewerScore() {
        return reviewerScore;
    }

    public int getReviewerScoreStatus() {
        return reviewerScoreStatus;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public boolean isInserted() {
        return isInserted;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isAutoScored() {
        return isAutoScored;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isShowLockingDeadline() {
        return showLockingDeadline;
    }

    public int getLockingDeadlineDays() {
        return lockingDeadlineDays;
    }

    public boolean isShowBookmark() {
        return showBookmark;
    }

    public String getActionMessage() {
        return actionMessage;
    }
}
