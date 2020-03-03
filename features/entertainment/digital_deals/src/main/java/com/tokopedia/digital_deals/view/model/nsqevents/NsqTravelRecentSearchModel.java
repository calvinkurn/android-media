package com.tokopedia.digital_deals.view.model.nsqevents;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NsqTravelRecentSearchModel implements Parcelable {

    @SerializedName("service")
    @Expose
    private String service;

    @SerializedName("message")
    @Expose
    private NsqMessage nsqMessage;

    @SerializedName("travel_recent_search")
    @Expose
    private NsqRecentSearchModel nsqRecentSearchModel;

    protected NsqTravelRecentSearchModel(Parcel in) {
        service = in.readString();
        nsqMessage = in.readParcelable(NsqMessage.class.getClassLoader());
        nsqRecentSearchModel = in.readParcelable(NsqRecentSearchModel.class.getClassLoader());
    }

    public NsqTravelRecentSearchModel() {}

    public static final Creator<NsqTravelRecentSearchModel> CREATOR = new Creator<NsqTravelRecentSearchModel>() {
        @Override
        public NsqTravelRecentSearchModel createFromParcel(Parcel in) {
            return new NsqTravelRecentSearchModel(in);
        }

        @Override
        public NsqTravelRecentSearchModel[] newArray(int size) {
            return new NsqTravelRecentSearchModel[size];
        }
    };

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public NsqMessage getNsqMessage() {
        return nsqMessage;
    }

    public void setNsqMessage(NsqMessage nsqMessage) {
        this.nsqMessage = nsqMessage;
    }

    public NsqRecentSearchModel getNsqRecentSearchModel() {
        return nsqRecentSearchModel;
    }

    public void setNsqRecentSearchModel(NsqRecentSearchModel nsqRecentSearchModel) {
        this.nsqRecentSearchModel = nsqRecentSearchModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(service);
        dest.writeParcelable(nsqMessage, flags);
        dest.writeParcelable(nsqRecentSearchModel, flags);
    }
}
