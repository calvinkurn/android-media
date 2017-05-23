
package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstallmentRule implements Parcelable {

    @SerializedName("min_purchase")
    @Expose
    private String minPurchase;
    @SerializedName("percentage")
    @Expose
    private String percentage;

    public String getMinPurchase() {
        return minPurchase;
    }

    public void setMinPurchase(String minPurchase) {
        this.minPurchase = minPurchase;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }


    protected InstallmentRule(Parcel in) {
        minPurchase = in.readString();
        percentage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(minPurchase);
        dest.writeString(percentage);
    }

    @SuppressWarnings("unused")
    public static final Creator<InstallmentRule> CREATOR = new Creator<InstallmentRule>() {
        @Override
        public InstallmentRule createFromParcel(Parcel in) {
            return new InstallmentRule(in);
        }

        @Override
        public InstallmentRule[] newArray(int size) {
            return new InstallmentRule[size];
        }
    };
}