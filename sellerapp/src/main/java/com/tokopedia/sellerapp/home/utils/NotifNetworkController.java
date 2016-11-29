package com.tokopedia.sellerapp.home.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.sellerapp.home.model.notification.Notification;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.sellerapp.home.utils.ShopNetworkController.onResponseError;

/**
 * Created by normansyahputa on 8/31/16.
 */

public class NotifNetworkController extends BaseNetworkController {

    private NotificationService notificationService;

    public NotifNetworkController(Context context, NotificationService notificationService, Gson gson){
        super(context, gson);
        this.notificationService = notificationService;
    }

    public void getNotif(String userId, String deviceId, final GetNotif getNotif){
        getNotif(userId, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getNotif.onError(e);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> response) {
                                if (response.isSuccessful()) {
                                    if(!response.body().isError()) {
                                        String stringData = response.body().getStringData();
                                        Log.d("STUART", "getNotif : onNext : "+stringData);
                                        Notification.Data notification = gson.fromJson(stringData, Notification.Data.class);
                                        getNotif.onSuccess(notification);
                                    }else {
                                        throw new ShopNetworkController.MessageErrorException(response.body().getErrorMessages().get(0));
                                    }
                                } else {
                                    onResponseError(response.code(), getNotif);
                                }
                            }
                        }
                );
    }

    public Observable<Response<TkpdResponse>> getNotif(String userId, String deviceId){
        return notificationService.getApi().getNotification(AuthUtil.generateParams(userId, deviceId, new HashMap<String, String>()));
    }

    public interface GetNotif extends ShopNetworkController.CommonListener {
        void onSuccess(Notification.Data notification);
    }
}
