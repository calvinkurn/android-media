package com.tokopedia.core.gcm.interactor.source;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tokopedia.core.gcm.interactor.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.network.apiservices.notification.PushNotificationService;
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

    public Observable<FCMTokenUpdateEntity> updateTokenServer(final FCMTokenUpdate data) {
        return Observable.just(data)
                .flatMap(new Func1<FCMTokenUpdate, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(FCMTokenUpdate requestData) {
                        TKPDMapParam<String, String> param = new TKPDMapParam<>();
                        param.put("device_id_old", requestData.getOldToken());
                        param.put("device_id_new", requestData.getNewToken());
                        param.put("os_type", requestData.getOsType());
                        param.put("user_id", requestData.getUserId());
                        PushNotificationService service = new PushNotificationService(requestData.getAccessToken());
                        return service.getApi().updateToken(param);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, FCMTokenUpdateEntity>() {
                    @Override
                    public FCMTokenUpdateEntity call(Response<TkpdResponse> response) {
                        FCMTokenUpdateEntity entity = new FCMTokenUpdateEntity();
                        entity.setToken(data.getNewToken());
                        if (response.isSuccessful()) {
                            TkpdResponse tkpdResponse = response.body();
                            if (tkpdResponse.isError()) {
                                entity.setSuccess(false);
                                return entity;
                            }
                            if (tkpdResponse.isNullData()) {
                                entity.setSuccess(false);
                                return entity;
                            }
                            if (!response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                                try {
                                    int status = response.body().getJsonData()
                                            .getInt(KEY_FLAG_IS_SUCCESS);
                                    switch (status) {
                                        case 1:
                                            entity.setSuccess(true);
                                            return entity;
                                        default:
                                            entity.setSuccess(false);
                                            return entity;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    entity.setSuccess(false);
                                    return entity;
                                }
                            }
                            entity.setSuccess(false);
                            return entity;
                        } else {
                            throw new RuntimeException("error");
                        }
                    }
                });
    }

    public Observable<String> deviceRegistration(){
        return Observable.just(true)
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String call(Boolean aBoolean) {
                        return FirebaseInstanceId.getInstance().getToken();
                    }
                });
    }
}