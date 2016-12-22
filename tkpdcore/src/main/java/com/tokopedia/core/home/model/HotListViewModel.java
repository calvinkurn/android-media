package com.tokopedia.core.home.model;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 20/11/2015.
 */
@Parcel
public class HotListViewModel {
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
}
