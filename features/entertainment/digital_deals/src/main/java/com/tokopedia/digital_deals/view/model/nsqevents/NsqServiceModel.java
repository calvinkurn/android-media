package com.tokopedia.digital_deals.view.model.nsqevents;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NsqServiceModel implements Parcelable {

    @SerializedName("service")
    @Expose
    private String service;

    @SerializedName("message")
    @Expose
    private NsqMessage message;


    public static final Creator<NsqServiceModel> CREATOR = new Creator<NsqServiceModel>() {
        @Override
        public NsqServiceModel createFromParcel(Parcel in) {
            return new NsqServiceModel(in);
        }

        @Override
        public NsqServiceModel[] newArray(int size) {
            return new NsqServiceModel[size];
        }
    };

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public NsqMessage getMessage() {
        return message;
    }

    public void setMessage(NsqMessage message) {
        this.message = message;
    }

    protected NsqServiceModel(Parcel in) {
        service = in.readString();
        message = in.readParcelable(NsqMessage.class.getClassLoader());
    }

    public NsqServiceModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(service);
        dest.writeParcelable(message, flags);
    }
}