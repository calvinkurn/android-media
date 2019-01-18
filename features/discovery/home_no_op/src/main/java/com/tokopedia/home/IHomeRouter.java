package com.tokopedia.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;

import rx.Observable;

public interface IHomeRouter {

    Intent openWebViewGimicURLIntentFromExploreHome(Context context, String url, String title);

    Observable<HomeHeaderWalletAction> getWalletBalanceHomeHeader();

    void openIntermediaryActivity(Activity activity, String depID, String title);

    Intent getActivityShopCreateEdit(Context context);

    Intent getInstantLoanIntent(Context context);

    Intent getBannerWebViewOnAllPromoClickFromHomeIntent(Activity activity,
                                                         String url,
                                                         String title);

    void onDigitalItemClickFromExploreHome(
            Activity activity,
            String appLink,
            String categoryId,
            String name,
            String url);

    void sendIndexScreen(Activity activity, String screeName);

    Intent getTopAdsProductDetailIntentForHome(Context context,
                                               String id,
                                               String name,
                                               String priceFormat,
                                               String imageMUrl);

    Observable<TokopointHomeDrawerData> getTokopointUseCaseForHome();
}
