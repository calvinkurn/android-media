package com.tokopedia.transactiondata.entity.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class ShippingInfoCheckoutRequest implements Parcelable {

    @SerializedName("shipping_id")
    @Expose
    public int shippingId;
    @SerializedName("sp_id")
    @Expose
    public int spId;
    @SerializedName("rates_id")
    @Expose
    public String ratesId;
    @SerializedName("checksum")
    @Expose
    public String checksum;
    @SerializedName("ut")
    @Expose
    public String ut;

    public ShippingInfoCheckoutRequest() {
    }

    private ShippingInfoCheckoutRequest(Builder builder) {
        shippingId = builder.shippingId;
        spId = builder.spId;
        ratesId = builder.ratesId;
        checksum = builder.checksum;
        ut = builder.ut;
    }

    protected ShippingInfoCheckoutRequest(Parcel in) {
        shippingId = in.readInt();
        spId = in.readInt();
        ratesId = in.readString();
        checksum = in.readString();
        ut = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shippingId);
        dest.writeInt(spId);
        dest.writeString(ratesId);
        dest.writeString(checksum);
        dest.writeString(ut);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShippingInfoCheckoutRequest> CREATOR = new Creator<ShippingInfoCheckoutRequest>() {
        @Override
        public ShippingInfoCheckoutRequest createFromParcel(Parcel in) {
            return new ShippingInfoCheckoutRequest(in);
        }

        @Override
        public ShippingInfoCheckoutRequest[] newArray(int size) {
            return new ShippingInfoCheckoutRequest[size];
        }
    };

    public static final class Builder {
        private int shippingId;
        private int spId;
        private String ratesId;
        private String checksum;
        private String ut;

        public Builder() {
        }

        public Builder shippingId(int val) {
            shippingId = val;
            return this;
        }

        public Builder spId(int val) {
            spId = val;
            return this;
        }

        public Builder ratesId(String val) {
            ratesId = val;
            return this;
        }

        public Builder checksum(String val) {
            checksum = val;
            return this;
        }

        public Builder ut(String val) {
            ut = val;
            return this;
        }

        public ShippingInfoCheckoutRequest build() {
            return new ShippingInfoCheckoutRequest(this);
        }
    }
}
