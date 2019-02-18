package com.tokopedia.transactiondata.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class CheckoutRequest {

    @SerializedName("promo_code")
    @Expose
    public String promoCode;
    @SerializedName("is_donation")
    @Expose
    public int isDonation;
    @SerializedName("data")
    @Expose
    public List<DataCheckoutRequest> data = new ArrayList<>();
    @SerializedName("tokopedia_corner_data")
    @Expose
    public TokopediaCornerData cornerData;

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
        cornerData = builder.cornerData;
    }


    public static final class Builder {
        private String promoCode;
        private int isDonation;
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

        public CheckoutRequest build() {
            return new CheckoutRequest(this);
        }
    }
}
