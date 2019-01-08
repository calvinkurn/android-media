package com.tokopedia.tkpd.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.network.entity.home.TopAdsHome;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.home.favorite.presenter.Favorite;

import java.util.ArrayList;
import java.util.List;

public class HorizontalShopList extends RecyclerViewItem implements Parcelable {
    List<ShopItem> shopItemList = new ArrayList<>();

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
        shopItem.name = data.getHeadline().getShop().getName();// 1
        shopItem.iconUri = data.getHeadline().getShop().getImageShop().getXsUrl();// 2
        shopItem.coverUri = data.getHeadline().getShop().getImageShop().getCover();// 3
        shopItem.location = data.getHeadline().getShop().getLocation();// 4
        shopItem.isFav = Favorite.TOP_ADS_AD_SHOP_VALUE;// 5
        shopItem.id = data.getHeadline().getShop().getId();// 6
        shopItem.adKey = data.getAdRefKey();
        shopItem.shopClickUrl = data.getAdClickUrl();
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
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.shopItemList);
    }

    protected HorizontalShopList(Parcel in) {
        super(in);
        this.shopItemList = in.createTypedArrayList(ShopItem.CREATOR);
    }

    public static final Creator<HorizontalShopList> CREATOR = new Creator<HorizontalShopList>() {
        @Override
        public HorizontalShopList createFromParcel(Parcel source) {
            return new HorizontalShopList(source);
        }

        @Override
        public HorizontalShopList[] newArray(int size) {
            return new HorizontalShopList[size];
        }
    };
}
