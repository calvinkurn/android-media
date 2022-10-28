package com.tokopedia.kol.feature.post.view.viewmodel;

import android.text.style.ClickableSpan;

/**
 * @author by milhamj on 14/05/18.
 */

public class BaseKolViewModel {
    private String userId;
    private String activityType;
    private String cardType;
    private String title;
    private String name;
    private String avatar;
    private String label;
    private String kolProfileUrl;
    private boolean followed;
    private String review;
    private ClickableSpan reviewUrlClickableSpan;
    private boolean liked;
    private int totalLike;
    private int totalComment;
    private int page;
    private boolean temporarilyFollowed;
    private String contentId;
    private String time;
    private boolean reviewExpanded;
    private boolean isShowComment;
    private boolean isShowLike;
    private boolean editable;
    private boolean deletable;
    private boolean reportable;
    private boolean multipleContent;
    private boolean isKol = true;

    public BaseKolViewModel(String userId, String activityType, String cardType, String title,
                            String name, String avatar, String label, String kolProfileUrl,
                            boolean followed, String review, boolean liked, int totalLike,
                            int totalComment, int page, String contentId, String time,
                            boolean isShowComment, boolean isShowLike, boolean editable,
                            boolean deletable, boolean multipleContent) {
        this.userId = userId;
        this.cardType = cardType;
        this.activityType = activityType;
        this.title = title;
        this.name = name;
        this.avatar = avatar;
        this.label = label;
        this.kolProfileUrl = kolProfileUrl;
        this.followed = followed;
        this.review = review;
        this.liked = liked;
        this.totalLike = totalLike;
        this.totalComment = totalComment;
        this.page = page;
        this.contentId = contentId;
        this.time = time;
        this.isShowComment = isShowComment;
        this.isShowLike = isShowLike;
        this.editable = editable;
        this.deletable = deletable;
        this.multipleContent = multipleContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKolProfileUrl() {
        return kolProfileUrl;
    }

    public void setKolProfileUrl(String kolProfileUrl) {
        this.kolProfileUrl = kolProfileUrl;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public ClickableSpan getReviewUrlClickableSpan() {
        return reviewUrlClickableSpan;
    }

    public void setReviewUrlClickableSpan(ClickableSpan reviewUrlClickableSpan) {
        this.reviewUrlClickableSpan = reviewUrlClickableSpan;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isTemporarilyFollowed() {
        return temporarilyFollowed;
    }

    public void setTemporarilyFollowed(boolean temporarilyFollowed) {
        this.temporarilyFollowed = temporarilyFollowed;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public boolean isReviewExpanded() {
        return reviewExpanded;
    }

    public void setReviewExpanded(boolean reviewExpanded) {
        this.reviewExpanded = reviewExpanded;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isShowComment() {
        return isShowComment;
    }

    public void setShowComment(boolean showComment) {
        isShowComment = showComment;
    }

    public boolean isShowLike() {
        return isShowLike;
    }

    public void setShowLike(boolean showLike) {
        isShowLike = showLike;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isReportable() {
        return reportable;
    }

    public void setReportable(boolean reportable) {
        this.reportable = reportable;
    }

    public boolean isKol() {
        return isKol;
    }

    public void setKol(boolean kol) {
        isKol = kol;
    }

    public boolean isMultipleContent() {
        return multipleContent;
    }

    public void setMultipleContent(boolean multipleContent) {
        this.multipleContent = multipleContent;
    }
}
