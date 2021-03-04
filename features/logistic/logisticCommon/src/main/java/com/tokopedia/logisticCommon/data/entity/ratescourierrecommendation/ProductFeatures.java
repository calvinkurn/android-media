package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductFeatures implements Parcelable {

    @SerializedName("ontime_delivery_guarantee")
    @Expose
    private OntimeDeliveryGuarantee ontimeDeliveryGuarantee;
    @SerializedName("mvc")
    @Expose
    private MerchantVoucherProductData merchantVoucherProductData;

    public OntimeDeliveryGuarantee getOntimeDeliveryGuarantee() {
        return ontimeDeliveryGuarantee;
    }

    public void setOntimeDeliveryGuarantee(OntimeDeliveryGuarantee ontimeDeliveryGuarantee) {
        this.ontimeDeliveryGuarantee = ontimeDeliveryGuarantee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ontimeDeliveryGuarantee, flags);
    }

    public ProductFeatures() {
    }

    protected ProductFeatures(Parcel in) {
        this.ontimeDeliveryGuarantee = in.readParcelable(OntimeDeliveryGuarantee.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductFeatures> CREATOR = new Parcelable.Creator<ProductFeatures>() {
        @Override
        public ProductFeatures createFromParcel(Parcel source) {
            return new ProductFeatures(source);
        }

        @Override
        public ProductFeatures[] newArray(int size) {
            return new ProductFeatures[size];
        }
    };

    public MerchantVoucherProductData getMerchantVoucherProductData() {
        return merchantVoucherProductData;
    }

    public void setMerchantVoucherProductData(MerchantVoucherProductData merchantVoucherProductData) {
        this.merchantVoucherProductData = merchantVoucherProductData;
    }
}
