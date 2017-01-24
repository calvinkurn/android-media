package com.tokopedia.core.drawer2.interactor;

import android.content.Context;

import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 1/23/17.
 */

public class NotificationNetworkInteractorImpl implements NotificationNetworkInteractor {
    private NotificationService notificationService;

    public NotificationNetworkInteractorImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public Observable<Response<TkpdResponse>> getNotification(Context context, TKPDMapParam<String, String> params) {
        return notificationService.getApi().
                getNotification(AuthUtil.generateParamsNetwork(context, params));
    }
}
