package com.tokopedia.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;

import rx.Observable;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface IHomeRouter {

//    boolean isSupportApplink(String appLink);

    Observable<HomeHeaderWalletAction> getWalletBalanceHomeHeader();

    Observable<TokopointHomeDrawerData> getTokopointUseCaseForHome();

//    void openIntermediaryActivity(Activity activity, String depID, String title);

//    void onDigitalItemClickFromExploreHome(
//            Activity activity,
//            String appLink,
//            String categoryId,
//            String name,
//            String url);

    /* Can't remove because StoreSettingActivity doesn't have Deeplink */
    void goToManageShop(Context context);

//    void sendIndexScreen(Activity activity, String screeName);

//    void goToApplinkActivity(Context context, String applink);

    void goToWallet(Context context, String url);

    void goToTokoCash(String appLinkBalance, Activity activity);

//    void actionOpenGeneralWebView(Activity activity, String mobileUrl);

//    Intent getBannerWebViewOnAllPromoClickFromHomeIntent(Activity activity,
//                                                         String url,
//                                                         String title);

//    Intent getBannerWebViewIntent(Activity activity, String url);

    /* Can't remove because SimpleWebViewWithFilePickerActivity doesn't have Deeplink */
    Intent openWebViewGimicURLIntentFromExploreHome(Context context, String url, String title);

//    Intent getActivityShopCreateEdit(Context context);

//    Intent getShopPageIntent(Context context, String shopId);

//    Intent getInstantLoanIntent(Context context);

//    Intent getLoginIntent(Context context);

//    Intent getHomeIntent(Context context);

//    String getExtraBroadcastReceiverWallet();

//    Intent getIntentCreateShop(Context context);

    /* Can't remove because QrScannerActivity doesn't have Deeplink */
    Intent gotoQrScannerPage(boolean needResult);
}
