package com.tokopedia.core.drawer.model;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer.var.UserType;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

/**
 * Created by nisie on 11/23/16.
 */

public class DrawerHeader extends RecyclerViewItem {

    public String userName = "";
    public String shopName = "";
    public String userIcon = "";
    public String shopIcon = "";
    public String shopCover = "";
    public String deposit = "";
    public String timestamp = "";
    public String ShopId = "";
    public String Loyalty = "";
    public String LoyaltyUrl = "";
    public UserType userType = UserType.TYPE_GUEST;

    public DrawerHeader() {
        setType(TkpdState.DrawerItem.TYPE_HEADER);
    }

    public DrawerHeader(Context context) {
        setType(TkpdState.DrawerItem.TYPE_HEADER);
        LocalCacheHandler Cache = new LocalCacheHandler(context,
                TkpdState.CacheName.CACHE_USER);
        this.timestamp = Cache.getString("timestamp", timestamp);
        this.userName = Cache.getString("user_name", userName);
        this.userIcon = Cache.getString("user_pic_uri", userIcon);
        this.shopName = Cache.getString("user_shop", shopName);
        this.shopIcon = Cache.getString("shop_pic_uri", shopIcon);
        this.shopCover = Cache.getString("shop_cover_uri", shopCover);
        this.ShopId = Cache.getString("shop_id", ShopId);
    }


    public void setDataToCache(Context context) {
        LocalCacheHandler Cache = new LocalCacheHandler(context, TkpdState.CacheName.CACHE_USER);
        Cache.putString("timestamp", timestamp);
        Cache.putString("user_name", userName);
        Cache.putString("user_pic_uri", userIcon);
        Cache.putString("user_shop", shopName);
        Cache.putString("shop_pic_uri", shopIcon);
        Cache.putString("shop_cover_uri", shopCover);
        Cache.putString("shop_id", ShopId);
        Cache.applyEditor();
    }

    public String getDeposit(Context context) {
        LocalCacheHandler Cache = new LocalCacheHandler(context, TkpdState.CacheName.CACHE_USER);
        return Cache.getString("deposit", "");
    }
}
