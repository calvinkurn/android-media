package com.tokopedia.kol.feature.post.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by milhamj on 14/05/18.
 */

public class BaseKolViewModel implements Parcelable {
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
    private boolean isShowComment;
    private boolean isShowLike;

    public BaseKolViewModel(int userId, String cardType, String title, String name, String avatar,
                            String label, String kolProfileUrl, boolean followed, String review,
                            boolean liked, int totalLike, int totalComment, int page, int kolId,
                            String time, boolean isShowComment, boolean isShowLike) {
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
        this.isShowComment = isShowComment;
        this.isShowLike = isShowLike;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.cardType);
        dest.writeString(this.title);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.label);
        dest.writeString(this.kolProfileUrl);
        dest.writeByte(this.followed ? (byte) 1 : (byte) 0);
        dest.writeString(this.review);
        dest.writeByte(this.liked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalLike);
        dest.writeInt(this.totalComment);
        dest.writeInt(this.page);
        dest.writeByte(this.temporarilyFollowed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.kolId);
        dest.writeByte(this.reviewExpanded ? (byte) 1 : (byte) 0);
        dest.writeString(this.time);
        dest.writeByte(this.isShowComment ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowLike ? (byte) 1 : (byte) 0);
    }

    protected BaseKolViewModel(Parcel in) {
        this.userId = in.readInt();
        this.cardType = in.readString();
        this.title = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
        this.label = in.readString();
        this.kolProfileUrl = in.readString();
        this.followed = in.readByte() != 0;
        this.review = in.readString();
        this.liked = in.readByte() != 0;
        this.totalLike = in.readInt();
        this.totalComment = in.readInt();
        this.page = in.readInt();
        this.temporarilyFollowed = in.readByte() != 0;
        this.kolId = in.readInt();
        this.reviewExpanded = in.readByte() != 0;
        this.time = in.readString();
        this.isShowComment = in.readByte() != 0;
        this.isShowLike = in.readByte() != 0;
    }

    public static final Parcelable.Creator<BaseKolViewModel> CREATOR = new Parcelable.Creator<BaseKolViewModel>() {
        @Override
        public BaseKolViewModel createFromParcel(Parcel source) {
            return new BaseKolViewModel(source);
        }

        @Override
        public BaseKolViewModel[] newArray(int size) {
            return new BaseKolViewModel[size];
        }
    };
}
