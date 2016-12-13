package com.tokopedia.core.gcm.interactor.source;

import com.tokopedia.core.gcm.model.FcmTokenUpdate;
import com.tokopedia.core.network.apiservices.notification.NotificationService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 12/8/16.
 */

public class CloudNotificationDataSource {
    public Observable<String> updateTokenServer(FcmTokenUpdate data) {
        return Observable.just(data)
                .flatMap(new Func1<FcmTokenUpdate, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(FcmTokenUpdate requestData) {
                        TKPDMapParam<String, String> param = new TKPDMapParam<>();
                        param.put("device_id_old", requestData.getOldToken());
                        param.put("device_id_new", requestData.getNewToken());
                        param.put("os_type", requestData.getOsType());
                        NotificationService service = new NotificationService(requestData.getAccessToken());
                        return service.getApi().update(param);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, String>() {
                    @Override
                    public String call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            return response.body().getStatus();
                        } else {
                            throw new RuntimeException("error");
                        }
                    }
                });
    }
}