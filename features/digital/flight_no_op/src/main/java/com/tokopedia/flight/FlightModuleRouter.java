package com.tokopedia.flight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.contactus.model.FlightContactUsPassData;
import com.tokopedia.flight.review.domain.FlightVoucherCodeWrapper;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;

import java.io.File;

import okhttp3.Interceptor;
import rx.Observable;

public interface FlightModuleRouter {

    long getLongConfig(String flightAirport);

    boolean isPromoNativeEnable();

    Intent getLoginIntent();

    Intent getTopPayIntent(Activity activity, FlightCheckoutViewModel flightCheckoutViewModel);

    int getTopPayPaymentSuccessCode();

    int getTopPayPaymentFailedCode();

    int getTopPayPaymentCancelCode();

    Intent getBannerWebViewIntent(Activity activity, String url);

    Intent getWebviewActivity(Activity activity, String url);

    Intent getHomeIntent(Context context);

    Intent getContactUsIntent(Activity activity, FlightContactUsPassData passData);

    Intent getDefaultContactUsIntent(Activity activity);

    Intent getDefaultContactUsIntent(Activity activity, String url);

    Intent getPhoneVerifIntent(Activity activity);

    Observable<ProfileInfo> getProfile();

    Interceptor getChuckInterceptor();

    Intent getLoyaltyWithCoupon(Activity activity, String platform, String categoryId, String cartId);

    Intent getLoyaltyWithCouponTabSelected(Activity activity, String platform, String categoryId, String cartId);

    FlightVoucherCodeWrapper getFlightVoucherCodeWrapper();

    Intent getPromoListIntent(Activity activity);

    Intent getPromoDetailIntent(Context context, String slug);

    File writeImage(String filePath, int qualityProcentage);

    Intent getOrderListIntent(Context context);
}