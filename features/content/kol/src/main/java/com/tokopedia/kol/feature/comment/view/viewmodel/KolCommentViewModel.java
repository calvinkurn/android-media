package com.tokopedia.kol.feature.comment.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentViewModel implements Visitable<KolCommentTypeFactory>, Parcelable{
    protected String id;
    protected String avatarUrl;
    protected String name;
    protected String review;
    protected String time;
    @Nullable
    protected String userUrl;
    protected boolean isOfficial;
    protected String userId;
    protected boolean canDeleteComment;
    protected String userBadges;
    protected boolean isShop;

    public KolCommentViewModel(String id, String userId, @Nullable String userUrl, String avatarUrl, String name,
                               String review, String time, boolean isOfficial,
                               boolean canDeleteComment, String userBadges, boolean isShop) {
        this.id = id;
        this.userId = userId;
        this.userUrl = userUrl;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.review = review;
        this.time = time;
        this.isOfficial = isOfficial;
        this.canDeleteComment = canDeleteComment;
        this.userBadges = userBadges;
        this.isShop = isShop;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Nullable
    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(@Nullable String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserBadges() {
        return userBadges;
    }

    @Override
    public int type(KolCommentTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        this.isOfficial = official;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean canDeleteComment() {
        return canDeleteComment;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isShop() {
        return isShop;
    }

    public void setShop(boolean shop) {
        isShop = shop;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.name);
        dest.writeString(this.review);
        dest.writeString(this.time);
        dest.writeString(this.userUrl);
        dest.writeByte(this.isOfficial ? (byte) 1 : (byte) 0);
        dest.writeString(this.userId);
        dest.writeByte(this.canDeleteComment ? (byte) 1 : (byte) 0);
        dest.writeString(this.userBadges);
        dest.writeByte(this.isShop ? (byte) 1 : (byte) 0);
    }

    protected KolCommentViewModel(Parcel in) {
        this.id = in.readString();
        this.avatarUrl = in.readString();
        this.name = in.readString();
        this.review = in.readString();
        this.time = in.readString();
        this.userUrl = in.readString();
        this.isOfficial = in.readByte() != 0;
        this.userId = in.readString();
        this.canDeleteComment = in.readByte() != 0;
        this.userBadges = in.readString();
        this.isShop = in.readByte() != 0;
    }

    public static final Creator<KolCommentViewModel> CREATOR = new Creator<KolCommentViewModel>() {
        @Override
        public KolCommentViewModel createFromParcel(Parcel source) {
            return new KolCommentViewModel(source);
        }

        @Override
        public KolCommentViewModel[] newArray(int size) {
            return new KolCommentViewModel[size];
        }
    };
}
