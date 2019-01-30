package com.tokopedia.shipping_recommendation.domain.shipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierViewModel implements Parcelable {

    private ProductData productData;
    private ServiceData serviceData;
    private String ratesId;
    private int additionalFee;
    private boolean allowDropshipper;
    private boolean selected;

    public ShippingCourierViewModel() {
    }

    protected ShippingCourierViewModel(Parcel in) {
        productData = in.readParcelable(ProductData.class.getClassLoader());
        serviceData = in.readParcelable(ServiceData.class.getClassLoader());
        ratesId = in.readString();
        additionalFee = in.readInt();
        allowDropshipper = in.readByte() != 0;
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(productData, flags);
        dest.writeParcelable(serviceData, flags);
        dest.writeString(ratesId);
        dest.writeInt(additionalFee);
        dest.writeByte((byte) (allowDropshipper ? 1 : 0));
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShippingCourierViewModel> CREATOR = new Creator<ShippingCourierViewModel>() {
        @Override
        public ShippingCourierViewModel createFromParcel(Parcel in) {
            return new ShippingCourierViewModel(in);
        }

        @Override
        public ShippingCourierViewModel[] newArray(int size) {
            return new ShippingCourierViewModel[size];
        }
    };

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRatesId() {
        return ratesId;
    }

    public void setRatesId(String ratesId) {
        this.ratesId = ratesId;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    public boolean isAllowDropshipper() {
        return allowDropshipper;
    }

    public void setAllowDropshipper(boolean allowDropshipper) {
        this.allowDropshipper = allowDropshipper;
    }

    public ServiceData getServiceData() {
        return serviceData;
    }

    public void setServiceData(ServiceData serviceData) {
        this.serviceData = serviceData;
    }
}
