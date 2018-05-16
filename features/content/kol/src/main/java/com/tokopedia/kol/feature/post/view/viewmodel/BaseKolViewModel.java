package com.tokopedia.kol.feature.post.view.viewmodel;

/**
 * @author by milhamj on 14/05/18.
 */

public class BaseKolViewModel {
    private final int userId;
    private final String cardType;
    private String title;
    private String name;
    private String avatar;
    private String label;
    private String kolProfileUrl;
    private boolean followed;
    private String review;
    private boolean liked;
    private int totalLike;
    private int totalComment;
    private int page;
    private boolean temporarilyFollowed;
    private int kolId;
    private boolean reviewExpanded;
    private String time;
    private boolean wishlisted;
    private boolean isShowComment;

    public BaseKolViewModel(int userId, String cardType, String title, String name, String
            avatar, String label, String kolProfileUrl, boolean followed, String review, boolean
            liked, int totalLike, int totalComment, int page, int kolId, String time, boolean
            wishlisted, boolean isShowComment) {
        this.userId = userId;
        this.cardType = cardType;
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
        this.kolId = kolId;
        this.time = time;
        this.wishlisted = wishlisted;
        this.isShowComment = isShowComment;
    }

    public int getUserId() {
        return userId;
    }

    public String getCardType() {
        return cardType;
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

    public int getKolId() {
        return kolId;
    }

    public void setKolId(int kolId) {
        this.kolId = kolId;
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

    public boolean isWishlisted() {
        return wishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        this.wishlisted = wishlisted;
    }

    public boolean isShowComment() {
        return isShowComment;
    }

    public void setShowComment(boolean showComment) {
        isShowComment = showComment;
    }
}
