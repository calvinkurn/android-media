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
    private double onlinePriceUnformatted;
    private double outletPriceUnformatted;
    private String onlinePrice;
    private String outletPrice;
    private int status;
    private long etalaseId;

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

    public double getOnlinePriceUnformatted() {
        return onlinePriceUnformatted;
    }

    public void setOnlinePriceUnformatted(double onlinePriceUnformatted) {
        this.onlinePriceUnformatted = onlinePriceUnformatted;
    }

    public double getOutletPriceUnformatted() {
        return outletPriceUnformatted;
    }

    public void setOutletPriceUnformatted(double outletPriceUnformatted) {
        this.outletPriceUnformatted = outletPriceUnformatted;
    }

    public String getOnlinePrice() {
        return onlinePrice;
    }

    public void setOnlinePrice(String onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    public String getOutletPrice() {
        return outletPrice;
    }

    public void setOutletPrice(String outletPrice) {
        this.outletPrice = outletPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    @Override
    public int type(ProductManagementAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public ProductViewModel() {
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
        dest.writeDouble(this.onlinePriceUnformatted);
        dest.writeDouble(this.outletPriceUnformatted);
        dest.writeString(this.onlinePrice);
        dest.writeString(this.outletPrice);
        dest.writeInt(this.status);
        dest.writeLong(this.etalaseId);
    }

    protected ProductViewModel(Parcel in) {
        this.id = in.readString();
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.onlinePriceUnformatted = in.readDouble();
        this.outletPriceUnformatted = in.readDouble();
        this.onlinePrice = in.readString();
        this.outletPrice = in.readString();
        this.status = in.readInt();
        this.etalaseId = in.readLong();
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProductViewModel) {
            return this.id.equals(((ProductViewModel) obj).getId());
        }
        return super.equals(obj);
    }
}
