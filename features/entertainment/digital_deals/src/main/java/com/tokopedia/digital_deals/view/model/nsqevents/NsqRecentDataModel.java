package com.tokopedia.digital_deals.view.model.nsqevents;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NsqRecentDataModel implements Parcelable {

    @SerializedName("entertainment")
    @Expose
    private NsqEntertainmentModel nsqEntertainmentModel;

    protected NsqRecentDataModel(Parcel in) {
        nsqEntertainmentModel = in.readParcelable(NsqEntertainmentModel.class.getClassLoader());
    }

    public NsqRecentDataModel() {}

    public static final Creator<NsqRecentDataModel> CREATOR = new Creator<NsqRecentDataModel>() {
        @Override
        public NsqRecentDataModel createFromParcel(Parcel in) {
            return new NsqRecentDataModel(in);
        }

        @Override
        public NsqRecentDataModel[] newArray(int size) {
            return new NsqRecentDataModel[size];
        }
    };

    public NsqEntertainmentModel getNsqEntertainmentModel() {
        return nsqEntertainmentModel;
    }

    public void setNsqEntertainmentModel(NsqEntertainmentModel nsqEntertainmentModel) {
        this.nsqEntertainmentModel = nsqEntertainmentModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(nsqEntertainmentModel, flags);
    }
}
