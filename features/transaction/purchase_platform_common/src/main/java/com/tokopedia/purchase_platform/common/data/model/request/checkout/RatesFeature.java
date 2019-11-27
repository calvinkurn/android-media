package com.tokopedia.purchase_platform.common.data.model.request.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatesFeature implements Parcelable {

    @SerializedName("ontime_delivery_guarantee")
    @Expose
    private OntimeDeliveryGuarantee ontimeDeliveryGuarantee;

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

    public RatesFeature() {
    }

    protected RatesFeature(Parcel in) {
        this.ontimeDeliveryGuarantee = in.readParcelable(OntimeDeliveryGuarantee.class.getClassLoader());
    }

    public static final Parcelable.Creator<RatesFeature> CREATOR = new Parcelable.Creator<RatesFeature>() {
        @Override
        public RatesFeature createFromParcel(Parcel source) {
            return new RatesFeature(source);
        }

        @Override
        public RatesFeature[] newArray(int size) {
            return new RatesFeature[size];
        }
    };
}
