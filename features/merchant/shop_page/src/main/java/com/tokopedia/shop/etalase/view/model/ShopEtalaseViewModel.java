package com.tokopedia.shop.etalase.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.view.adapter.ShopEtalaseAdapterTypeFactory;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseViewModel implements Visitable<ShopEtalaseAdapterTypeFactory>, Parcelable{

    public static final int USE_ACE = 1;
    private long useAce;
    private String etalaseId;
    private String etalaseName;
    private long etalaseNumProduct;
    private long etalaseTotalProduct;
    private String etalaseBadge;

    private boolean isSelected;

    public ShopEtalaseViewModel(String id, String name) {
        setEtalaseId(id);
        setEtalaseName(name);
    }

    public ShopEtalaseViewModel(EtalaseModel etalaseModel) {
        setEtalaseBadge(etalaseModel.getEtalaseBadge());
        setEtalaseId(etalaseModel.getEtalaseId());
        setEtalaseName(etalaseModel.getEtalaseName());
        setEtalaseNumProduct(etalaseModel.getEtalaseNumProduct());
        setEtalaseTotalProduct(etalaseModel.getEtalaseTotalProduct());
        setUseAce(etalaseModel.getUseAce());
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getUseAce() {
        return useAce;
    }

    public boolean isUseAce(){
        return useAce == USE_ACE;
    }

    public void setUseAce(long useAce) {
        this.useAce = useAce;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public long getEtalaseNumProduct() {
        return etalaseNumProduct;
    }

    public void setEtalaseNumProduct(long etalaseNumProduct) {
        this.etalaseNumProduct = etalaseNumProduct;
    }

    public long getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    public void setEtalaseTotalProduct(long etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

    public String getEtalaseBadge() {
        return etalaseBadge;
    }

    public void setEtalaseBadge(String etalaseBadge) {
        this.etalaseBadge = etalaseBadge;
    }

    @Override
    public int type(ShopEtalaseAdapterTypeFactory shopEtalaseAdapterTypeFactory) {
        return shopEtalaseAdapterTypeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.useAce);
        dest.writeString(this.etalaseId);
        dest.writeString(this.etalaseName);
        dest.writeLong(this.etalaseNumProduct);
        dest.writeLong(this.etalaseTotalProduct);
        dest.writeString(this.etalaseBadge);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected ShopEtalaseViewModel(Parcel in) {
        this.useAce = in.readLong();
        this.etalaseId = in.readString();
        this.etalaseName = in.readString();
        this.etalaseNumProduct = in.readLong();
        this.etalaseTotalProduct = in.readLong();
        this.etalaseBadge = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<ShopEtalaseViewModel> CREATOR = new Creator<ShopEtalaseViewModel>() {
        @Override
        public ShopEtalaseViewModel createFromParcel(Parcel source) {
            return new ShopEtalaseViewModel(source);
        }

        @Override
        public ShopEtalaseViewModel[] newArray(int size) {
            return new ShopEtalaseViewModel[size];
        }
    };
}
