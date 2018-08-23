package com.tokopedia.shop.settings.etalase.data;

import android.os.Parcel;

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.settings.etalase.view.adapter.factory.BaseShopEtalaseFactory;

/**
 * Created by hendry on 20/08/18.
 */
public class ShopEtalaseTitleViewModel extends BaseShopEtalaseViewModel{
    public ShopEtalaseTitleViewModel(String name){
        super();
        setName(name);
    }

    public ShopEtalaseTitleViewModel(Parcel in) {
        super(in);
    }

    @Override
    public int type(BaseShopEtalaseFactory typeFactory) {
        return typeFactory.type(this);
    }

    public static final Creator<BaseShopEtalaseViewModel> CREATOR = new Creator<BaseShopEtalaseViewModel>() {
        @Override
        public BaseShopEtalaseViewModel createFromParcel(Parcel source) {
            return new ShopEtalaseTitleViewModel(source);
        }

        @Override
        public BaseShopEtalaseViewModel[] newArray(int size) {
            return new BaseShopEtalaseViewModel[size];
        }
    };

}
