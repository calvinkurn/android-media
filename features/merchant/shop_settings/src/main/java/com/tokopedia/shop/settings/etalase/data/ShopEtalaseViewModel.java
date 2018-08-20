package com.tokopedia.shop.settings.etalase.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory;

/**
 * Created by hendry on 20/08/18.
 */
public class ShopEtalaseViewModel implements Parcelable, Visitable<BaseShopEtalaseFactory>{
    private String id;
    private String name;
    private int count;
    public ShopEtalaseViewModel(){
    }

    public ShopEtalaseViewModel(ShopEtalaseModel shopEtalaseModel){
        this.id = shopEtalaseModel.getId();
        this.name = shopEtalaseModel.getName();
        this.count = shopEtalaseModel.getCount();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.count);
    }

    protected ShopEtalaseViewModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.count = in.readInt();
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

    @Override
    public int type(BaseShopEtalaseFactory typeFactory) {
        return typeFactory.type(this);
    }
}
