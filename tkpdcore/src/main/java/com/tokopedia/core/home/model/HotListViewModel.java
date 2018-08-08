package com.tokopedia.core.home.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by m.normansyah on 20/11/2015.
 */

public class HotListViewModel implements Parcelable {
    public static final int SWIPE_ = 0;
    public static final int LOAD_MORE = 1;
    public static final int RETRY_ = 2;

    boolean isSwipeShow;
    boolean isLoadMoreShow;
    boolean isRetryShow;

    public boolean isSwipeShow() {
        return isSwipeShow;
    }

    public void setIsSwipeShow(boolean isSwipeShow) {
        this.isSwipeShow = isSwipeShow;
    }

    public boolean isLoadMoreShow() {
        return isLoadMoreShow;
    }

    public void setIsLoadMoreShow(boolean isLoadMoreShow) {
        this.isLoadMoreShow = isLoadMoreShow;
    }

    public boolean isRetryShow() {
        return isRetryShow;
    }

    public void setIsRetryShow(boolean isRetryShow) {
        this.isRetryShow = isRetryShow;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSwipeShow ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLoadMoreShow ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRetryShow ? (byte) 1 : (byte) 0);
    }

    public HotListViewModel() {
    }

    protected HotListViewModel(Parcel in) {
        this.isSwipeShow = in.readByte() != 0;
        this.isLoadMoreShow = in.readByte() != 0;
        this.isRetryShow = in.readByte() != 0;
    }

    public static final Parcelable.Creator<HotListViewModel> CREATOR = new Parcelable.Creator<HotListViewModel>() {
        @Override
        public HotListViewModel createFromParcel(Parcel source) {
            return new HotListViewModel(source);
        }

        @Override
        public HotListViewModel[] newArray(int size) {
            return new HotListViewModel[size];
        }
    };
}
