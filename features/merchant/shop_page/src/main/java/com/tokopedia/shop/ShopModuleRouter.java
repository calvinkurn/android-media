package com.tokopedia.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {

    Fragment getKolPostShopFragment(String shopId, String createPostUrl);

    void goToEditShop(Context context);

    void goToManageShop(Context context);

    void goToEditShopNote(Context context);

    void goToManageShipping(Context context);

    void goToAddProduct(Context context);

    void goToChatSeller(Context context, String shopId, String shopName, String avatar);

    void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel);

    void goToWebview(Context context, String url);

    void goToProfileShop(Context context, String userId);

    void goToShopReview(Context context, String shopId, String shopDomain);

    void goToShopDiscussion(Context context, String shopId);

    Intent getLoginIntent(Context context);

    Intent getTopProfileIntent(Context context, String userId);

    boolean isFeedShopPageEnabled();

    void sendMoEngageFavoriteEvent(String shopName, String shopID, String shopDomain, String shopLocation, boolean isShopOfficaial, boolean isFollowed);
}
