package com.tokopedia.sellerhomedrawer.domain.service;

import com.tokopedia.sellerhomedrawer.data.constant.SellerDrawerUrl;
import com.tokopedia.sellerhomedrawer.domain.retrofit.api.NotificationApi;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class NotificationService {
    private static final String TAG = NotificationService.class.getSimpleName();

    private Retrofit retrofit;
    private NotificationApi notificationApi;
    private String baseUrl = SellerDrawerUrl.User.URL_NOTIFICATION;

    @Inject
    public NotificationService(Retrofit retrofit) {
        this.retrofit = retrofit;
        notificationApi = retrofit.create(NotificationApi.class);
    }

    public NotificationApi getNotificationApi() {
        return notificationApi;
    }

}
