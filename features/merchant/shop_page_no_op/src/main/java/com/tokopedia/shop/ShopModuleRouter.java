package com.tokopedia.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Map;

public interface ShopModuleRouter {

    void goToManageShop(Context context);

    void goToManageShipping(Context context);

    void goToChatSeller(Context context, String shopId, String shopName, String avatar);

    void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel);

    void goToWebview(Context context, String url);

    void goToProfileShop(Context context, String userId);

    void goToShopReview(Context context, String shopId, String shopDomain);

    void goToShopDiscussion(Context context, String shopId);

    Intent getLoginIntent(Context context);

    Intent getTopProfileIntent(Context context, String userId);

    boolean isFeedShopPageEnabled();

}
