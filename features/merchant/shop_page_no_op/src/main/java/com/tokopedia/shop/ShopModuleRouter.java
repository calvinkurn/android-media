package com.tokopedia.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Map;

public interface ShopModuleRouter {

    Fragment getShopReputationFragmentShop(String shopId, String shopDomain);

    Fragment getKolPostShopFragment(String shopId, String createPostUrl);

    void goToEditShop(Context context);

    void goToManageShop(Context context);

    void goToEditShopNote(Context context);

    void goToManageShipping(Context context);

    void goToAddProduct(Context context);

    void goToChatSeller(Context context, String shopId, String shopName, String avatar);

    void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel);

    void goToProductDetail(Context context, String productId, String name, String displayedPrice, String imageUrl, String attribution, String listNameOfProduct);

    void goToWebview(Context context, String url);

    void goToProductDetailById(Context activity, String productId);

    void goToProfileShop(Context context, String userId);

    void goToShopReview(Context context, String shopId, String shopDomain);

    void goToShopDiscussion(Context context, String shopId);

    Intent getLoginIntent(Context context);

    Intent getTopProfileIntent(Context context, String userId);

    boolean isFeedShopPageEnabled();

}
