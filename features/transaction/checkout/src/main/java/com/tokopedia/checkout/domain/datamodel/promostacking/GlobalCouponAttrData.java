package com.tokopedia.checkout.domain.datamodel.promostacking;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fwidjaja on 15/04/19.
 */
public class GlobalCouponAttrData implements Parcelable {

    private String description;
    private String quantityLabel;

    public GlobalCouponAttrData(Parcel in) {
        description = in.readString();
        quantityLabel = in.readString();
    }

    public GlobalCouponAttrData() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantityLabel() {
        return quantityLabel;
    }

    public void setQuantityLabel(String quantityLabel) {
        this.quantityLabel = quantityLabel;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(quantityLabel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GlobalCouponAttrData> CREATOR = new Creator<GlobalCouponAttrData>() {
        @Override
        public GlobalCouponAttrData createFromParcel(Parcel in) {
            return new GlobalCouponAttrData(in);
        }

        @Override
        public GlobalCouponAttrData[] newArray(int size) {
            return new GlobalCouponAttrData[size];
        }
    };
}
