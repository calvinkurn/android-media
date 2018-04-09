package com.tokopedia.posapp.product.management.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementAdapterTypeFactory;

/**
 * Created by okasurya on 8/10/17.
 */

public class ProductViewModel implements Visitable<ProductManagementAdapterTypeFactory>,Parcelable {
    private String id;
    private String imageUrl;
    private String name;
    private double onlinePrice;
    private double outletPrice;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOnlinePrice() {
        return onlinePrice;
    }

    public void setOnlinePrice(double onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    public double getOutletPrice() {
        return outletPrice;
    }

    public void setOutletPrice(double outletPrice) {
        this.outletPrice = outletPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int type(ProductManagementAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imageUrl);
        dest.writeString(this.name);
        dest.writeDouble(this.onlinePrice);
        dest.writeDouble(this.outletPrice);
        dest.writeInt(this.status);
    }

    public ProductViewModel() {
    }

    protected ProductViewModel(Parcel in) {
        this.id = in.readString();
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.onlinePrice = in.readDouble();
        this.outletPrice = in.readDouble();
        this.status = in.readInt();
    }

    public static final Creator<ProductViewModel> CREATOR = new Creator<ProductViewModel>() {
        @Override
        public ProductViewModel createFromParcel(Parcel source) {
            return new ProductViewModel(source);
        }

        @Override
        public ProductViewModel[] newArray(int size) {
            return new ProductViewModel[size];
        }
    };
}
