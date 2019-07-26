package com.tokopedia.logisticcart.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 11/10/18.
 */

public class ShippingRecommendationData implements Parcelable {

    private List<ShippingDurationViewModel> shippingDurationViewModels;
    private LogisticPromoViewModel logisticPromo;
    private String errorMessage;
    private String errorId;
    private String blackboxInfo;

    public ShippingRecommendationData() {
    }

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

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public LogisticPromoViewModel getLogisticPromo() {
        return logisticPromo;
    }

    public void setLogisticPromo(LogisticPromoViewModel logisticPromo) {
        this.logisticPromo = logisticPromo;
    }

    public String getBlackboxInfo() { return blackboxInfo; }

    public void setBlackboxInfo(String blackboxInfo) { this.blackboxInfo = blackboxInfo; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.shippingDurationViewModels);
        dest.writeParcelable(this.logisticPromo, flags);
        dest.writeString(this.errorMessage);
        dest.writeString(this.errorId);
        dest.writeString(this.blackboxInfo);
    }

    protected ShippingRecommendationData(Parcel in) {
        this.shippingDurationViewModels = in.createTypedArrayList(ShippingDurationViewModel.CREATOR);
        this.logisticPromo = in.readParcelable(LogisticPromoViewModel.class.getClassLoader());
        this.errorMessage = in.readString();
        this.errorId = in.readString();
        this.blackboxInfo = in.readString();
    }

    public static final Creator<ShippingRecommendationData> CREATOR = new Creator<ShippingRecommendationData>() {
        @Override
        public ShippingRecommendationData createFromParcel(Parcel source) {
            return new ShippingRecommendationData(source);
        }

        @Override
        public ShippingRecommendationData[] newArray(int size) {
            return new ShippingRecommendationData[size];
        }
    };

}
