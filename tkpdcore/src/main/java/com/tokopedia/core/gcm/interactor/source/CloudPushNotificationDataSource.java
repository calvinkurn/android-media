package com.tokopedia.core.gcm.interactor.source;

import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.network.apiservices.notification.NotificationService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import org.json.JSONException;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 12/8/16.
 */

public class CloudPushNotificationDataSource {
    private static final String KEY_FLAG_IS_SUCCESS = "is_success";

    public Observable<Boolean> updateTokenServer(FCMTokenUpdate data) {
        return Observable.just(data)
                .flatMap(new Func1<FCMTokenUpdate, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(FCMTokenUpdate requestData) {
                        TKPDMapParam<String, String> param = new TKPDMapParam<>();
                        param.put("device_id_old", requestData.getOldToken());
                        param.put("device_id_new", requestData.getNewToken());
                        param.put("os_type", requestData.getOsType());
                        param.put("user_id", requestData.getUserId());
                        NotificationService service = new NotificationService(requestData.getAccessToken());
                        return service.getApi().updateToken(param);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, Boolean>() {
                    @Override
                    public Boolean call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            TkpdResponse tkpdResponse = response.body();
                            if (tkpdResponse.isError()) {
                                if (!tkpdResponse.getErrorMessages().isEmpty()) {
                                    return false;
                                } else {
                                    return false;
                                }
                            }
                            if (tkpdResponse.isNullData()) {
                                return false;
                            }
                            if (!response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                                try {
                                    int status = response.body().getJsonData()
                                            .getInt(KEY_FLAG_IS_SUCCESS);
                                    switch (status) {
                                        case 1:
                                            return true;
                                        default:
                                            return false;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                            }
                            return false;
                        } else {
                            throw new RuntimeException("error");
                        }
                    }
                });
    }
}