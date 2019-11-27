package com.tokopedia.purchase_platform.features.cart.data.model.response.recentview;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class GqlRecentView implements Parcelable {

    @SerializedName("items")
    @Expose
    private List<RecentView> recentViewList;

    public GqlRecentView() {
    }

    protected GqlRecentView(Parcel in) {
        recentViewList = in.createTypedArrayList(RecentView.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(recentViewList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GqlRecentView> CREATOR = new Creator<GqlRecentView>() {
        @Override
        public GqlRecentView createFromParcel(Parcel in) {
            return new GqlRecentView(in);
        }

        @Override
        public GqlRecentView[] newArray(int size) {
            return new GqlRecentView[size];
        }
    };

    public List<RecentView> getRecentViewList() {
        return recentViewList;
    }

    public void setRecentViewList(List<RecentView> recentViewList) {
        this.recentViewList = recentViewList;
    }
}
