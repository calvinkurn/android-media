package com.tokopedia.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;

import rx.Observable;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface IHomeRouter {

    void openIntermediaryActivity(Activity activity, String depID, String title);

    void onDigitalItemClick(Activity activity, DigitalCategoryDetailPassData passData, String appLink);

    void openReactNativeOfficialStore(FragmentActivity activity);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    Intent getInstantLoanIntent(Context context);

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
}
