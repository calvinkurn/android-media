package com.tokopedia.flight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;

import java.io.File;

import okhttp3.Interceptor;
import rx.Observable;

public interface FlightModuleRouter {

    Intent getTopPayIntent(Activity activity, FlightCheckoutViewModel flightCheckoutViewModel);

    Intent getWebviewActivity(Activity activity, String url);

    Intent getHomeIntent(Context context);

    Intent getDefaultContactUsIntent(Activity activity);

    Intent getDefaultContactUsIntent(Activity activity, String url);

    Intent getPhoneVerifIntent(Activity activity);

    Observable<ProfileInfo> getProfile();

    Interceptor getChuckInterceptor();

    File writeImage(String filePath, int qualityProcentage);

}
