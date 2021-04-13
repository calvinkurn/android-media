package com.tokopedia.checkout.data.model.request.checkout;

import android.annotation.SuppressLint;
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

    @SerializedName("promos")
    @Expose
    public List<PromoRequest> promos;
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
    @SerializedName("has_promo_stacking")
    @Expose
    public boolean hasPromoStacking;
    @SerializedName("promo_codes")
    @Expose
    public ArrayList<String> promoCodes;
    @SerializedName("leasing_id")
    @Expose
    @SuppressLint("Invalid Data Type") // Need to add this since we're not ready to change the existing data type to String
    public int leasingId;

    public CheckoutRequest() {
    }

    protected CheckoutRequest(Parcel in) {
        promos = in.createTypedArrayList(PromoRequest.CREATOR);
        promoCode = in.readString();
        isDonation = in.readInt();
        egoldData = in.readParcelable(EgoldData.class.getClassLoader());
        data = in.createTypedArrayList(DataCheckoutRequest.CREATOR);
        cornerData = in.readParcelable(TokopediaCornerData.class.getClassLoader());
        hasPromoStacking = in.readByte() != 0;
        promoCodes = in.createStringArrayList();
        leasingId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(promos);
        dest.writeString(promoCode);
        dest.writeInt(isDonation);
        dest.writeParcelable(egoldData, flags);
        dest.writeTypedList(data);
        dest.writeParcelable(cornerData, flags);
        dest.writeByte((byte) (hasPromoStacking ? 1 : 0));
        dest.writeStringList(promoCodes);
        dest.writeInt(leasingId);
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
        promos = builder.promoRequests;
        promoCode = builder.promoCode;
        isDonation = builder.isDonation;
        data = builder.data;
        egoldData = builder.egoldData;
        cornerData = builder.cornerData;
        hasPromoStacking = builder.hasPromoStacking;
        promoCodes = builder.promoCodes;
        leasingId = builder.leasingId;
    }


    public static final class Builder {
        private List<PromoRequest> promoRequests;
        private String promoCode;
        private int isDonation;
        private EgoldData egoldData;
        private List<DataCheckoutRequest> data;
        private TokopediaCornerData cornerData;
        private boolean hasPromoStacking;
        private ArrayList<String> promoCodes;
        private int leasingId;

        public Builder() {
        }

        public Builder promos(List<PromoRequest> val) {
            promoRequests = val;
            return this;
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

        public Builder hasPromoStacking(boolean val) {
            hasPromoStacking = val;
            return this;
        }

        public Builder promoCodes(ArrayList<String> val) {
            promoCodes = val;
            return this;
        }

        public Builder setLeasingId(int val) {
            leasingId = val;
            return this;
        }

        public CheckoutRequest build() {
            return new CheckoutRequest(this);
        }
    }
}
