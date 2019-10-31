package com.tokopedia.digital_deals.view.model.nsqevents;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NsqRecentSearchModel implements Parcelable {

    @SerializedName("data_type")
    @Expose
    private String dataType;

    @SerializedName("recent_data")
    @Expose
    private NsqRecentDataModel nsqRecentDataModel;

    protected NsqRecentSearchModel(Parcel in) {
        dataType = in.readString();
        nsqRecentDataModel = in.readParcelable(NsqRecentDataModel.class.getClassLoader());
    }

    public NsqRecentSearchModel() {}

    public static final Creator<NsqRecentSearchModel> CREATOR = new Creator<NsqRecentSearchModel>() {
        @Override
        public NsqRecentSearchModel createFromParcel(Parcel in) {
            return new NsqRecentSearchModel(in);
        }

        @Override
        public NsqRecentSearchModel[] newArray(int size) {
            return new NsqRecentSearchModel[size];
        }
    };

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public NsqRecentDataModel getNsqRecentDataModel() {
        return nsqRecentDataModel;
    }

    public void setNsqRecentDataModel(NsqRecentDataModel nsqRecentDataModel) {
        this.nsqRecentDataModel = nsqRecentDataModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dataType);
        dest.writeParcelable(nsqRecentDataModel, flags);
    }
}
