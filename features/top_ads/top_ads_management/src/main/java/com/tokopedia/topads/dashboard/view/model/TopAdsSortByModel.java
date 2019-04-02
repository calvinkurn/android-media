package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.base.list.seller.common.util.ItemIdType;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;

/**
 * Created by nakama on 10/04/18.
 */

public class TopAdsSortByModel implements ItemIdType, Parcelable {
    public static final int TYPE = 223;

    @SortTopAdsOption
    private String sortId;

    private String titleSort;

    public TopAdsSortByModel() {
    }

    public TopAdsSortByModel(String sortId, String titleSort) {
        this.sortId = sortId;
        this.titleSort = titleSort;
    }

    protected TopAdsSortByModel(Parcel in) {
        this.sortId = in.readString();
        this.titleSort = in.readString();
    }

    public static final Creator<TopAdsSortByModel> CREATOR = new Creator<TopAdsSortByModel>() {
        @Override
        public TopAdsSortByModel createFromParcel(Parcel in) {
            return new TopAdsSortByModel(in);
        }

        @Override
        public TopAdsSortByModel[] newArray(int size) {
            return new TopAdsSortByModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.sortId);
        parcel.writeString(this.titleSort);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    @SortTopAdsOption
    public String getItemId() {
        return sortId;
    }

    @SortTopAdsOption
    public String getId(){
        return sortId;
    }

    public String getTitleSort() {
        return titleSort;
    }
}
