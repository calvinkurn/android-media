package com.tokopedia.train.common;

import android.content.Context;

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

}
