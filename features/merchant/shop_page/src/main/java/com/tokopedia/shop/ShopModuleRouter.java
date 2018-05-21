package com.tokopedia.shop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Map;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {

    Fragment getShopReputationFragmentShop(String shopId, String shopDomain);

    Fragment getShopTalkFragment();

    void goToManageShop(Context context);

    void goToEditShopNote(Context context);

    void goToAddProduct(Context context);

    void goToChatSeller(Context context, String shopId, String shopName, String avatar);

    void goToShareShop(FragmentManager fragmentManager, String shopId, String shopUrl, String shareLabel);

    void goToProductDetail(Context context, String productId, String name, String displayedPrice, String imageUrl, String attribution, String listNameOfProduct);

    void goToWebview(Context context, String url);

    void goToProductDetailById(Context activity, String productId);

    void goToProfileShop(Context context, String userId);

    Intent getLoginIntent(Context context);

    void sendEventTrackingShopPage(Map<String, Object> eventTracking);

    void sendScreenName(String screenName);

    Intent getTopProfileIntent(Context context, String userId);
}
