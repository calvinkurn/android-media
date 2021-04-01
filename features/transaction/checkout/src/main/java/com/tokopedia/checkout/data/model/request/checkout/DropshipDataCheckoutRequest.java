package com.tokopedia.checkout.data.model.request.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class DropshipDataCheckoutRequest implements Parcelable {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("telp_no")
    @Expose
    public String telpNo;

    public DropshipDataCheckoutRequest() {
    }

    private DropshipDataCheckoutRequest(Builder builder) {
        name = builder.name;
        telpNo = builder.telpNo;
    }

    protected DropshipDataCheckoutRequest(Parcel in) {
        name = in.readString();
        telpNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(telpNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DropshipDataCheckoutRequest> CREATOR = new Creator<DropshipDataCheckoutRequest>() {
        @Override
        public DropshipDataCheckoutRequest createFromParcel(Parcel in) {
            return new DropshipDataCheckoutRequest(in);
        }

        @Override
        public DropshipDataCheckoutRequest[] newArray(int size) {
            return new DropshipDataCheckoutRequest[size];
        }
    };

    public static final class Builder {
        private String name;
        private String telpNo;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder telpNo(String val) {
            telpNo = val;
            return this;
        }

        public DropshipDataCheckoutRequest build() {
            return new DropshipDataCheckoutRequest(this);
        }
    }
}
