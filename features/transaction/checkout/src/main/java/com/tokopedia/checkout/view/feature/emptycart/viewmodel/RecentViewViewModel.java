package com.tokopedia.checkout.view.feature.emptycart.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.domain.datamodel.recentview.RecentView;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class RecentViewViewModel implements Parcelable {

    private RecentView recentView;

    public RecentViewViewModel() {
    }

    protected RecentViewViewModel(Parcel in) {
        recentView = in.readParcelable(RecentView.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(recentView, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecentViewViewModel> CREATOR = new Creator<RecentViewViewModel>() {
        @Override
        public RecentViewViewModel createFromParcel(Parcel in) {
            return new RecentViewViewModel(in);
        }

        @Override
        public RecentViewViewModel[] newArray(int size) {
            return new RecentViewViewModel[size];
        }
    };

    public RecentView getRecentView() {
        return recentView;
    }

    public void setRecentView(RecentView recentView) {
        this.recentView = recentView;
    }
}
