package com.tokopedia.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;

import rx.Observable;
import com.tokopedia.topads.sdk.domain.model.Product;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface IHomeRouter {

    void openIntermediaryActivity(Activity activity, String depID, String title);

    void onDigitalItemClickFromExploreHome(
            Activity activity,
            String appLink,
            String categoryId,
            String name,
            String url);

    void openReactNativeOfficialStore(FragmentActivity activity);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    Intent getInstantLoanIntent(Context context);

    Intent getTopAdsProductDetailIntentForHome(Context context, Product product);

    void goToManageShop(Context context);

    void goToApplinkActivity(Context context, String applink);

    boolean isSupportApplink(String appLink);

    Intent getLoginIntent(Context context);

    Intent getHomeIntent(Context context);

    void goToWalletFromHome(Context context, String url);

    void goToProductDetail(Context context,
                           String productId,
                           String imageSourceSingle,
                           String name,
                           String price);

    void goToProductDetail(Context context, String productUrl);

    Intent getIntentCreateShop(Context context);

    void goToTokoCash(String applinkUrl, String redirectUrl, Activity activity);

    Observable<TokopointHomeDrawerData> getTokopointUseCaseForHome();

    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

    Intent getBannerWebViewOnAllPromoClickFromHomeIntent(Activity activity,
                                                         String url,
                                                         String title);

    Intent getBannerWebViewIntent(Activity activity, String url);

    Intent openWebViewGimicURLIntentFromExploreHome(Context context, String url, String title);

    Intent getActivityShopCreateEdit(Context context);
}
