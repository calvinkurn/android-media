package com.tokopedia.shop.settings.etalase.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory;

/**
 * Created by hendry on 23/08/18.
 */
public abstract class BaseShopEtalaseViewModel implements Parcelable, Visitable<BaseShopEtalaseFactory> {
    protected String id;
    protected String name;
    protected int count;
    protected @ShopEtalaseTypeDef
    int type;
    protected boolean isPrimaryEtalase;

    public BaseShopEtalaseViewModel(){
    }

    public BaseShopEtalaseViewModel(ShopEtalaseModel shopEtalaseModel,
                                    boolean isPrimaryEtalase){
        this.id = shopEtalaseModel.getId();
        this.name = shopEtalaseModel.getName();
        this.count = shopEtalaseModel.getCount();
        this.type = shopEtalaseModel.getType();
        this.isPrimaryEtalase = isPrimaryEtalase;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public boolean isPrimaryEtalase() {
        return isPrimaryEtalase;
    }

    @Override
    public abstract int type(BaseShopEtalaseFactory typeFactory);


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeInt(this.type);
        dest.writeByte(this.isPrimaryEtalase ? (byte) 1 : (byte) 0);
    }

    protected BaseShopEtalaseViewModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.count = in.readInt();
        this.type = in.readInt();
        this.isPrimaryEtalase = in.readByte() != 0;
    }
}
