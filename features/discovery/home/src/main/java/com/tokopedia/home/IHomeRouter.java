package com.tokopedia.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;

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

    void sendIndexScreen(Activity activity, String screeName);

    Observable<HomeHeaderWalletAction> getWalletBalanceHomeHeader();

    String getExtraBroadcastReceiverWallet();

    AnalyticTracker getAnalyticTracker();
}
