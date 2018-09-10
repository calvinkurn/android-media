package com.tokopedia.shop.etalase.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.view.adapter.ShopEtalaseAdapterTypeFactory;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseViewModel implements Visitable<ShopEtalaseAdapterTypeFactory>, Parcelable{

    public static final int USE_ACE = 1;
    private boolean useAce;
    private String etalaseId;
    private String etalaseName;
    private long etalaseCount;
    private String etalaseBadge;

    private boolean isSelected;

    public ShopEtalaseViewModel(String id, String name, boolean isUseAce) {
        setEtalaseId(id);
        setEtalaseName(name);
        setUseAce(isUseAce);
    }

    public ShopEtalaseViewModel(EtalaseModel etalaseModel) {
        setEtalaseBadge(etalaseModel.getEtalaseBadge());
        setEtalaseId(etalaseModel.getEtalaseId());
        setEtalaseName(etalaseModel.getEtalaseName());
        setEtalaseCount(etalaseModel.getEtalaseNumProduct());
        setUseAce(etalaseModel.getUseAce() == USE_ACE);
    }

    public ShopEtalaseViewModel(ShopEtalaseModel shopEtalaseModel) {
        setEtalaseBadge(null);
        setEtalaseId(shopEtalaseModel.getId());
        setEtalaseName(shopEtalaseModel.getName());
        setEtalaseCount(shopEtalaseModel.getCount());
        setUseAce(shopEtalaseModel.getUseAce());
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isUseAce(){
        return useAce;
    }

    public void setUseAce(boolean useAce) {
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
        this.etalaseName = etalaseName == null? "" : etalaseName;
    }

    public long getEtalaseCount() {
        return etalaseCount;
    }

    public void setEtalaseCount(long etalaseCount) {
        this.etalaseCount = etalaseCount;
    }

    public String getEtalaseBadge() {
        return etalaseBadge;
    }

    public void setEtalaseBadge(String etalaseBadge) {
        this.etalaseBadge = etalaseBadge == null ? "" : etalaseBadge;
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
        dest.writeByte(this.useAce ? (byte) 1 : (byte) 0);
        dest.writeString(this.etalaseId);
        dest.writeString(this.etalaseName);
        dest.writeLong(this.etalaseCount);
        dest.writeString(this.etalaseBadge);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected ShopEtalaseViewModel(Parcel in) {
        this.useAce = in.readByte() != 0;
        this.etalaseId = in.readString();
        this.etalaseName = in.readString();
        this.etalaseCount = in.readLong();
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
