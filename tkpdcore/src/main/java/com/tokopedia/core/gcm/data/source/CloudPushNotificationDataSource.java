package com.tokopedia.core.gcm.data.source;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tokopedia.core.gcm.domain.PushNotification;
import com.tokopedia.core.gcm.data.PushNotificationDataStore;
import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.network.apiservices.notification.PushNotificationService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import org.json.JSONException;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.core.gcm.Constants.REGISTRATION_MESSAGE_OK;
import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_OK;

/**
 * @author by alvarisi on 12/8/16.
 */

public class CloudPushNotificationDataSource implements PushNotificationDataStore {
    private static final String KEY_FLAG_IS_SUCCESS = "is_success";

    public Observable<FCMTokenUpdateEntity> updateTokenServer(final FCMTokenUpdate data) {
        return Observable.just(data)
                .flatMap(new Func1<FCMTokenUpdate, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(FCMTokenUpdate requestData) {
                        TKPDMapParam<String, String> param = new TKPDMapParam<>();
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

    public Observable<DeviceRegistrationDataResponse> deviceRegistration(){
        return Observable.just(true)
                .map(new Func1<Boolean, DeviceRegistrationDataResponse>() {
                    @Override
                    public DeviceRegistrationDataResponse call(Boolean aBoolean) {
                        String cloudRegitrationID = FirebaseInstanceId.getInstance().getToken();
                        DeviceRegistrationDataResponse response = new DeviceRegistrationDataResponse();
                        response.setStatusCode(REGISTRATION_STATUS_OK);
                        response.setDeviceRegistration(cloudRegitrationID);
                        response.setStatusMessage(REGISTRATION_MESSAGE_OK);
                        return response;
                    }
                });
    }

    @Override
    public Observable<List<PushNotification>> getPushSavedPushNotificationWithOrderBy(String category, boolean ascendant) {
        return null;
    }

    @Override
    public Observable<Boolean> saveRegistrationDevice(String registrationDevice) {
        return null;
    }

    @Override
    public Observable<List<PushNotification>> getSavedPushNotification() {
        return null;
    }

    @Override
    public Observable<List<PushNotification>> getPushSavedPushNotification(String category) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteSavedPushNotificationByCategory(String category) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteSavedPushNotificationByCategoryAndServerId(String category, String serverId) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteSavedPushNotification() {
        return null;
    }

    @Override
    public Observable<Boolean> savePushNotification(String category, String response, String customIndex) {
        return null;
    }

    @Override
    public Observable<Boolean> savePushNotification(String category, String response) {
        return null;
    }

    @Override
    public Observable<String> getRegistrationDevice() {
        String cloudRegitrationID = FirebaseInstanceId.getInstance().getToken();
        return Observable.just(cloudRegitrationID);
    }

    @Override
    public Observable<String> savePushNotification(String category, String response, String customIndex, String serverId) {
        return null;
    }
}