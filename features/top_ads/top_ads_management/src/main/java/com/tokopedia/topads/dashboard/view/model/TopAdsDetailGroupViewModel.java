package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;

/**
 * Created by Nathaniel on 2/23/2017.
 */

public class TopAdsDetailGroupViewModel extends TopAdsDetailProductViewModel {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public TopAdsDetailGroupViewModel() {
    }

    protected TopAdsDetailGroupViewModel(Parcel in) {
        super(in);
    }

    public static final Creator<TopAdsDetailGroupViewModel> CREATOR = new Creator<TopAdsDetailGroupViewModel>() {
        @Override
        public TopAdsDetailGroupViewModel createFromParcel(Parcel source) {
            return new TopAdsDetailGroupViewModel(source);
        }

        @Override
        public TopAdsDetailGroupViewModel[] newArray(int size) {
            return new TopAdsDetailGroupViewModel[size];
        }
    };
}
