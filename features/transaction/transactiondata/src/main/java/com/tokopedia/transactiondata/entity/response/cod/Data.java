package com.tokopedia.transactiondata.entity.response.cod;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fajarnuha on 19/12/18.
 */
public class Data implements Parcelable {

    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("info_link")
    @Expose
    private String infoLink;
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("price_summary")
    @Expose
    private List<PriceSummary> priceSummary = null;
    @SerializedName("counter_info")
    @Expose
    private String counterInfo;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<PriceSummary> getPriceSummary() {
        return priceSummary;
    }

    public void setPriceSummary(List<PriceSummary> priceSummary) {
        this.priceSummary = priceSummary;
    }

    public String getCounterInfo() {
        return counterInfo;
    }

    public void setCounterInfo(String counterInfo) {
        this.counterInfo = counterInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.errorMessage);
        dest.writeString(this.infoLink);
        dest.writeParcelable(this.message, flags);
        dest.writeTypedList(this.priceSummary);
        dest.writeString(this.counterInfo);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.errorMessage = in.readString();
        this.infoLink = in.readString();
        this.message = in.readParcelable(Message.class.getClassLoader());
        this.priceSummary = in.createTypedArrayList(PriceSummary.CREATOR);
        this.counterInfo = in.readString();
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
