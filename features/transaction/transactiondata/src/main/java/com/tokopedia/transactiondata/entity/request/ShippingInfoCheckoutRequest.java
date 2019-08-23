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

    public String analyticsDataShippingCourierPrice;

    public ShippingInfoCheckoutRequest() {
    }

    private ShippingInfoCheckoutRequest(Builder builder) {
        shippingId = builder.shippingId;
        spId = builder.spId;
        ratesId = builder.ratesId;
        checksum = builder.checksum;
        ut = builder.ut;
        analyticsDataShippingCourierPrice = builder.analyticsDataShippingCourierPrice;
    }

    public static final class Builder {
        private int shippingId;
        private int spId;
        private String ratesId;
        private String checksum;
        private String ut;
        private String analyticsDataShippingCourierPrice;

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

        public Builder analyticsDataShippingCourierPrice(String val) {
            analyticsDataShippingCourierPrice = val;
            return this;
        }

        public ShippingInfoCheckoutRequest build() {
            return new ShippingInfoCheckoutRequest(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shippingId);
        dest.writeInt(this.spId);
        dest.writeString(this.ratesId);
        dest.writeString(this.checksum);
        dest.writeString(this.ut);
        dest.writeString(this.analyticsDataShippingCourierPrice);
    }

    protected ShippingInfoCheckoutRequest(Parcel in) {
        this.shippingId = in.readInt();
        this.spId = in.readInt();
        this.ratesId = in.readString();
        this.checksum = in.readString();
        this.ut = in.readString();
        this.analyticsDataShippingCourierPrice = in.readString();
    }

    public static final Creator<ShippingInfoCheckoutRequest> CREATOR = new Creator<ShippingInfoCheckoutRequest>() {
        @Override
        public ShippingInfoCheckoutRequest createFromParcel(Parcel source) {
            return new ShippingInfoCheckoutRequest(source);
        }

        @Override
        public ShippingInfoCheckoutRequest[] newArray(int size) {
            return new ShippingInfoCheckoutRequest[size];
        }
    };
}
