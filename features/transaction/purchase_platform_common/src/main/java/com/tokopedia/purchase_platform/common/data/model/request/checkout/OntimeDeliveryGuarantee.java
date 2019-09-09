package com.tokopedia.purchase_platform.common.data.model.request.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OntimeDeliveryGuarantee implements Parcelable {

    @SerializedName("available")
    @Expose
    private boolean available;
    @SerializedName("duration")
    @Expose
    private int duration;

    public Boolean getAvailable() {
        return available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public OntimeDeliveryGuarantee() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeInt(this.duration);
    }

    protected OntimeDeliveryGuarantee(Parcel in) {
        this.available = in.readByte() != 0;
        this.duration = in.readInt();
    }

    public static final Creator<OntimeDeliveryGuarantee> CREATOR = new Creator<OntimeDeliveryGuarantee>() {
        @Override
        public OntimeDeliveryGuarantee createFromParcel(Parcel source) {
            return new OntimeDeliveryGuarantee(source);
        }

        @Override
        public OntimeDeliveryGuarantee[] newArray(int size) {
            return new OntimeDeliveryGuarantee[size];
        }
    };
}