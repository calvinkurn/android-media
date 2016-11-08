package com.tokopedia.core.home.model;

import com.tokopedia.core.home.presenter.Favorite;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 31/10/2015.
 */
@Parcel
public class HorizontalShopList extends RecyclerViewItem{
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
        for(int i= 0;i<data.length;i++){
            shopItems.add(fromData(data[i]));
        }
        return shopItems;
    }
}
