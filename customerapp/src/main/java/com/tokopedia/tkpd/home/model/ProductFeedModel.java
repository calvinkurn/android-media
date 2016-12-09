package com.tokopedia.tkpd.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.var.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class ProductFeedModel implements Parcelable {
    HorizontalProductList dataHistory;
    List<ProductItem> dataProduct;
    HorizontalProductList dataTopAds;

    public ProductFeedModel(){}

    public HorizontalProductList getDataHistory() {
        return dataHistory;
    }

    public void setDataHistory(List<ProductItem> dataHistory) {
        this.dataHistory = new HorizontalProductList(dataHistory);
    }

    public List<ProductItem> getDataProduct() {
        return dataProduct;
    }

    public void setDataProduct(List<ProductItem> dataProduct) {
        this.dataProduct = dataProduct;
    }

    public HorizontalProductList getDataTopAds() {
        return dataTopAds;
    }

    public void setDataTopAds(List<ProductItem> dataTopAds) {
        this.dataTopAds = new HorizontalProductList(dataTopAds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.dataHistory);
        dest.writeList(this.dataProduct);
        dest.writeSerializable(this.dataTopAds);
    }

    protected ProductFeedModel(Parcel in) {
        this.dataHistory = (HorizontalProductList) in.readSerializable();
        this.dataProduct = new ArrayList<ProductItem>();
        in.readList(this.dataProduct, ProductItem.class.getClassLoader());
        this.dataTopAds = (HorizontalProductList) in.readSerializable();
    }

    public static final Parcelable.Creator<ProductFeedModel> CREATOR = new Parcelable.Creator<ProductFeedModel>() {
        @Override
        public ProductFeedModel createFromParcel(Parcel source) {
            return new ProductFeedModel(source);
        }

        @Override
        public ProductFeedModel[] newArray(int size) {
            return new ProductFeedModel[size];
        }
    };
}
