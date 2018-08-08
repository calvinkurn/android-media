package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierViewModel implements Parcelable {

    private ProductData productData;
    private boolean selected;

    public ShippingCourierViewModel() {
    }

    protected ShippingCourierViewModel(Parcel in) {
        productData = in.readParcelable(ProductData.class.getClassLoader());
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(productData, flags);
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
}
