package com.tokopedia.kol.feature.comment.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentHeaderViewModel extends KolCommentViewModel implements
        Visitable<KolCommentTypeFactory>, Parcelable {
    private boolean canLoadMore;
    private boolean isLoading;
    private String tagsLink;

    public KolCommentHeaderViewModel(String avatarUrl, String name, String review, String time,
                                     String userId, String userUrl, String tagsLink, String userBadges, boolean isShop) {
        super("0", userId, userUrl, avatarUrl, name, review, time, true, false, userBadges, isShop);
        this.canLoadMore = false;
        this.isLoading = false;
        this.tagsLink = tagsLink;
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getTagsLink() {
        return tagsLink;
    }

    public void setTagsLink(String tagsLink) {
        this.tagsLink = tagsLink;
    }

    @Override
    public int type(KolCommentTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.canLoadMore ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLoading ? (byte) 1 : (byte) 0);
        dest.writeString(this.tagsLink);
    }

    protected KolCommentHeaderViewModel(Parcel in) {
        super(in);
        this.canLoadMore = in.readByte() != 0;
        this.isLoading = in.readByte() != 0;
        this.tagsLink = in.readString();
    }

    public static final Creator<KolCommentHeaderViewModel> CREATOR = new
            Creator<KolCommentHeaderViewModel>() {
        @Override
        public KolCommentHeaderViewModel createFromParcel(Parcel source) {
            return new KolCommentHeaderViewModel(source);
        }

        @Override
        public KolCommentHeaderViewModel[] newArray(int size) {
            return new KolCommentHeaderViewModel[size];
        }
    };
}
