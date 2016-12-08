package com.tokopedia.core.notification.interactor.source;

import com.tokopedia.core.network.apiservices.notification.NotificationService;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 12/8/16.
 */

public class CloudNotificationDataSource {
    public Observable<String> updateFcmClientId(Map<String, String> map) {
        return Observable.just(map)
                .flatMap(new Func1<Map<String, String>, Observable<Response<String>>>() {
                    @Override
                    public Observable<Response<String>> call(Map<String, String> map) {
                        NotificationService service = new NotificationService();
                        return service.getApi().update(map);
                    }
                })
                .map(new Func1<Response<String>, String>() {
                    @Override
                    public String call(Response<String> response) {
                        if (response.isSuccessful()) {
                            return response.body();
                        } else {
                            throw new RuntimeException("error");
                        }
                    }
                });
    }
}