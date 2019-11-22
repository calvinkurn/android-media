package com.tokopedia.purchase_platform.features.cart.data.model.response.recentview;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class GqlRecentViewResponse implements Parcelable {

    @SerializedName("get_recent_view")
    @Expose
    private GqlRecentView gqlRecentView;

    public GqlRecentViewResponse() {
    }

    protected GqlRecentViewResponse(Parcel in) {
        gqlRecentView = in.readParcelable(GqlRecentView.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(gqlRecentView, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GqlRecentViewResponse> CREATOR = new Creator<GqlRecentViewResponse>() {
        @Override
        public GqlRecentViewResponse createFromParcel(Parcel in) {
            return new GqlRecentViewResponse(in);
        }

        @Override
        public GqlRecentViewResponse[] newArray(int size) {
            return new GqlRecentViewResponse[size];
        }
    };

    public GqlRecentView getGqlRecentView() {
        return gqlRecentView;
    }

    public void setGqlRecentView(GqlRecentView gqlRecentView) {
        this.gqlRecentView = gqlRecentView;
    }
}
