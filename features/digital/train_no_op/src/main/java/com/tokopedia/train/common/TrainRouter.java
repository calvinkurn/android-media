package com.tokopedia.train.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.tokopedia.train.checkout.presentation.model.TrainCheckoutViewModel;
import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;

import okhttp3.Interceptor;
import rx.Observable;

public interface TrainRouter {

    String TRAIN_ENABLE_REMOTE_CONFIG = "mainapp_kai_native";

    Observable<ProfileBuyerInfo> getProfileInfo();

    Interceptor getChuckInterceptor();

    Intent getIntentOfLoyaltyActivityWithCoupon(Activity activity, String platform, String reservationId, String reservationCode);

    Intent getLoginIntent();

    SnapHelper getSnapHelper();

    RecyclerView.ItemDecoration getSpacingItemDecorationHome(int spacing, int displayMode);

    Intent getPromoListIntent(Activity activity, String menuId, String subMenuId);

    boolean isPromoNativeEnable();

    Intent getPromoDetailIntent(Context context, String slug);

    Intent getBannerWebViewIntent(Activity activity, String url);

    Intent getTopPayIntent(Activity activity, TrainCheckoutViewModel trainCheckoutViewModel);

    int getTopPayPaymentSuccessCode();

    int getTopPayPaymentFailedCode();

    int getTopPayPaymentCancelCode();

    Intent getHomeIntent(Context context);

    Intent getTrainOrderListIntent(Context activity);

    Intent getWebviewActivity(Activity activity, String url);

    boolean isTrainNativeEnable();

}
