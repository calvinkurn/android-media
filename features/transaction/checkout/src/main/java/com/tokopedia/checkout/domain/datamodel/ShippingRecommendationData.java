package com.tokopedia.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view.ShippingDurationViewModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 11/10/18.
 */

public class ShippingRecommendationData implements Parcelable {

    private List<ShippingDurationViewModel> shippingDurationViewModels;
    private String errorMessage;

    public ShippingRecommendationData() {
    }

    protected ShippingRecommendationData(Parcel in) {
        shippingDurationViewModels = in.createTypedArrayList(ShippingDurationViewModel.CREATOR);
        errorMessage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(shippingDurationViewModels);
        dest.writeString(errorMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShippingRecommendationData> CREATOR = new Creator<ShippingRecommendationData>() {
        @Override
        public ShippingRecommendationData createFromParcel(Parcel in) {
            return new ShippingRecommendationData(in);
        }

        @Override
        public ShippingRecommendationData[] newArray(int size) {
            return new ShippingRecommendationData[size];
        }
    };

    public List<ShippingDurationViewModel> getShippingDurationViewModels() {
        return shippingDurationViewModels;
    }

    public void setShippingDurationViewModels(List<ShippingDurationViewModel> shippingDurationViewModels) {
        this.shippingDurationViewModels = shippingDurationViewModels;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
