package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

/**
 * Created by Nathaniel on 2/23/2017.
 */

public class TopAdsDetailShopViewModel extends TopAdsDetailProductViewModel {

    @Override
    public int getType() {
        return TopAdsNetworkConstant.TYPE_PRODUCT_SHOP;
    }

    @Override
    public long getItemId() {
        return getShopId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public TopAdsDetailShopViewModel() {
    }

    protected TopAdsDetailShopViewModel(Parcel in) {
        super(in);
    }

    public static final Creator<TopAdsDetailShopViewModel> CREATOR = new Creator<TopAdsDetailShopViewModel>() {
        @Override
        public TopAdsDetailShopViewModel createFromParcel(Parcel source) {
            return new TopAdsDetailShopViewModel(source);
        }

        @Override
        public TopAdsDetailShopViewModel[] newArray(int size) {
            return new TopAdsDetailShopViewModel[size];
        }
    };
}
