package com.tokopedia.transactiondata.entity.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class CheckoutRequest implements Parcelable {

    @SerializedName("promo_code")
    @Expose
    public String promoCode;
    @SerializedName("is_donation")
    @Expose
    public int isDonation;
    @SerializedName("egold_data")
    @Expose
    public EgoldData egoldData;
    @SerializedName("data")
    @Expose
    public List<DataCheckoutRequest> data = new ArrayList<>();
    @SerializedName("tokopedia_corner_data")
    @Expose
    public TokopediaCornerData cornerData;

    public CheckoutRequest() {
    }

    protected CheckoutRequest(Parcel in) {
        promoCode = in.readString();
        isDonation = in.readInt();
        egoldData = in.readParcelable(EgoldData.class.getClassLoader());
        data = in.createTypedArrayList(DataCheckoutRequest.CREATOR);
        cornerData = in.readParcelable(TokopediaCornerData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(promoCode);
        dest.writeInt(isDonation);
        dest.writeParcelable(egoldData, flags);
        dest.writeTypedList(data);
        dest.writeParcelable(cornerData, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckoutRequest> CREATOR = new Creator<CheckoutRequest>() {
        @Override
        public CheckoutRequest createFromParcel(Parcel in) {
            return new CheckoutRequest(in);
        }

        @Override
        public CheckoutRequest[] newArray(int size) {
            return new CheckoutRequest[size];
        }
    };

    public boolean isHavingPurchaseProtectionEnabled() {
        for (DataCheckoutRequest datum : data) {
            for (ShopProductCheckoutRequest shopProduct : datum.shopProducts) {
                for (ProductDataCheckoutRequest productDatum : shopProduct.productData) {
                    if (productDatum.isPurchaseProtection()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private CheckoutRequest(Builder builder) {
        promoCode = builder.promoCode;
        isDonation = builder.isDonation;
        data = builder.data;
        egoldData = builder.egoldData;
        cornerData = builder.cornerData;
    }


    public static final class Builder {
        private String promoCode;
        private int isDonation;
        private EgoldData egoldData;
        private List<DataCheckoutRequest> data;
        private TokopediaCornerData cornerData;

        public Builder() {
        }

        public Builder promoCode(String val) {
            promoCode = val;
            return this;
        }

        public Builder isDonation(int val) {
            isDonation = val;
            return this;
        }

        public Builder cornerData(TokopediaCornerData val) {
            cornerData = val;
            return this;
        }

        public Builder data(List<DataCheckoutRequest> val) {
            data = val;
            return this;
        }

        public Builder egoldData(EgoldData val) {
            egoldData = val;
            return this;
        }

        public CheckoutRequest build() {
            return new CheckoutRequest(this);
        }
    }
}
