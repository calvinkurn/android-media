package com.tokopedia.tkpd.home.model;

import android.os.Parcelable;

import com.tokopedia.core.network.entity.home.TopAdsHome;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.home.favorite.presenter.Favorite;

import java.util.ArrayList;
import java.util.List;

public class HorizontalShopList extends RecyclerViewItem implements Parcelable {
    List<ShopItem> shopItemList;

    public HorizontalShopList(){
        setType(TkpdState.RecyclerViewItem.TYPE_LIST);
    }

    public HorizontalShopList(List<ShopItem> shopItemList){
        this.shopItemList = shopItemList;
        setType(TkpdState.RecyclerViewItem.TYPE_LIST);
    }

    public void addAll(List<ShopItem> items){
        shopItemList.addAll(items);
    }

    public void clear(){
        shopItemList.clear();
    }

    public List<ShopItem> getShopItemList() {
        return shopItemList;
    }

    public void setShopItemList(List<ShopItem> shopItemList) {
        this.shopItemList = shopItemList;
    }

    public static ShopItem fromData(TopAdsHome.Data data){
        ShopItem shopItem = new ShopItem();
        shopItem.name = data.shop.name;// 1
        shopItem.iconUri = data.shop.imageShop.xsUrl;// 2
        shopItem.coverUri = data.shop.imageShop.cover;// 3
        shopItem.location = data.shop.location;// 4
        shopItem.isFav = Favorite.TOP_ADS_AD_SHOP_VALUE;// 5
        shopItem.id = data.shop.id;// 6
        shopItem.adKey = data.adRefKey;
        shopItem.shopClickUrl = data.shopClickUrl;
        return shopItem;
    }

    public static List<ShopItem> fromDatas(TopAdsHome.Data... data){
        List<ShopItem> shopItems = new ArrayList<>();
        for (TopAdsHome.Data aData : data) {
            shopItems.add(fromData(aData));
        }
        return shopItems;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeList(this.shopItemList);
    }

    protected HorizontalShopList(android.os.Parcel in) {
        this.shopItemList = new ArrayList<>();
        in.readList(this.shopItemList, ShopItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<HorizontalShopList> CREATOR
            = new Parcelable.Creator<HorizontalShopList>() {

        @Override
        public HorizontalShopList createFromParcel(android.os.Parcel source) {
            return new HorizontalShopList(source);
        }

        @Override
        public HorizontalShopList[] newArray(int size) {
            return new HorizontalShopList[size];
        }
    };
}
