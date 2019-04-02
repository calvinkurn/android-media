
package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class InstallmentRule implements Parcelable {

    @SerializedName("min_purchase")
    @Expose
    private String minPurchase;
    @SerializedName("installment_price")
    @Expose
    private String price;

    public String getMinPurchase() {
        return minPurchase;
    }

    public void setMinPurchase(String minPurchase) {
        this.minPurchase = minPurchase;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    protected InstallmentRule(Parcel in) {
        minPurchase = in.readString();
        price = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(minPurchase);
        dest.writeString(price);
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