package com.tokopedia.train.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;

import okhttp3.Interceptor;
import rx.Observable;

/**
 * Created by nabillasabbaha on 28/06/18.
 */
public interface TrainRouter {

    Observable<ProfileBuyerInfo> getProfileInfo();

    Interceptor getChuckInterceptor();

    void goToTrainOrderList(Context context);

    Intent getIntentOfLoyaltyActivityWithoutCoupon(Activity activity, String platform);

    Intent getLoginIntent();

}
