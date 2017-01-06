package com.tokopedia.core.gcm;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.iid.FirebaseInstanceId;
import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.interactor.RegisterDeviceInteractor;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.analytics.TrackingUtils;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GCMHandler {

    private static final String TAG = GCMHandler.class.getSimpleName();
    private static int STATUS_OK = 1;
    private static int STATUS_ERROR = 2;
    private static int STATUS_WAITING = 0;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Context context;
    private String regid;
    private String gcmRegid;
    private GCMHandlerListener gcmlistener;
    private GoogleCloudMessaging mGoogleCloudMessaging;
    private int retryAttempt = 0;

    public GCMHandler(Context context) {
        this.context = context;
    }

    public String getSenderID() {
        return Constants.FIREBASE_PROJECT_ID;
    }

    public void actionRegisterOrUpdateDevice(GCMHandlerListener listener) {
        if (listener != null)
            gcmlistener = listener;

        if (isPlayServicesAvailable()) {
            regid = getRegistrationId(context);
            CommonUtils.dumper(TAG + "start GCM get");
            if (regid.isEmpty()) {
                registerDeviceToFCM();
            } else {
                if (listener != null) {
                    gcmlistener.onGCMSuccess(regid);
                }
            }
        } else {
            Log.d(TAG, " failed to get GCM id !!!");
        }
    }

    public void commitGCMProcess() {
        if (isPlayServicesAvailable()) {
            mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(context);
            gcmRegid = getRegistrationId(context);
            CommonUtils.dumper("start mGoogleCloudMessaging get");
            if (gcmRegid.isEmpty()) {
                registerGCM();
            } else {
                Localytics.setPushRegistrationId(gcmRegid);
            }
        } else {
            Log.d(TAG, " failed to get mGoogleCloudMessaging id !!!");
        }
    }

    /**
     * :)
     */
    private void registerGCM() {
        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<Boolean, DeviceRegistrationDataResponse>() {
                    @Override
                    public DeviceRegistrationDataResponse call(Boolean aBoolean) {
                        DeviceRegistrationDataResponse param = new DeviceRegistrationDataResponse();
                        try {
                            if (mGoogleCloudMessaging == null) {
                                mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(context);
                            }
                            gcmRegid = mGoogleCloudMessaging.register(getSenderID());
                            FCMCacheManager.storeGCMRegId(gcmRegid, context);
                            param.setStatusCode(STATUS_OK);
                            param.setStatusMessage("GCM :: Device registered, registration ID=" + gcmRegid);
                            CommonUtils.dumper("GCM :: Device registered, registration ID=" + gcmRegid);

                            Localytics.setPushRegistrationId(gcmRegid);

                        } catch (IOException ex) {
                            param.setStatusCode(STATUS_ERROR);
                            param.setStatusMessage(ex.getMessage());
                        }
                        return param;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeviceRegistrationDataResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DeviceRegistrationDataResponse response) {
                        if (response.getStatusCode() == STATUS_OK) {

                        } else {
                            if (retryAttempt < 5) {
                                retryAttempt++;
                                registerGCM();
                            } else {
                                TrackingUtils.eventError(
                                        context.getClass().toString(),
                                        response.getStatusMessage()
                                );
                            }
                        }
                    }
                });
    }

    private void registerDeviceToFCM() {
        final RegisterDeviceInteractor interactor = new RegisterDeviceInteractor();
        interactor.registerDevice(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String registrationDeviceId) {
                interactor.storeRegistrationDevice(registrationDeviceId);
            }
        });

        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<Boolean, DeviceRegistrationDataResponse>() {
                    @Override
                    public DeviceRegistrationDataResponse call(Boolean aBoolean) {
                        DeviceRegistrationDataResponse param = new DeviceRegistrationDataResponse();
                        regid = FirebaseInstanceId.getInstance().getToken();
                        if (!TextUtils.isEmpty(regid)) {
                            FCMCacheManager.storeRegId(regid, context);
                            param.setStatusCode(STATUS_OK);
                            param.setStatusMessage(String.format("FCM :: Device registered, registration ID=%s", regid));
                            CommonUtils.dumper("FCM :: Device registered, registration ID=" + regid);
                        } else {
                            param.setStatusCode(STATUS_ERROR);
                            param.setStatusMessage("FCM :: GCM registration error");
                            CommonUtils.dumper("FCM :: GCM registration error");
                        }
                        return param;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeviceRegistrationDataResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DeviceRegistrationDataResponse response) {
                        if (response.getStatusCode() != STATUS_OK) {
                            TrackingUtils
                                    .eventError(context.getClass().toString(), response.getStatusMessage());
                            createAndStoreDummyRegistrationID();
                        }
                        Log.d(TAG, "FCM :: RegistrationId: " + regid);
                        gcmlistener.onGCMSuccess(regid);
                    }
                });
    }

    public static String getRegistrationId(Context context) {
        return FCMCacheManager.getRegistrationId(context);
    }

    public static void clearRegistrationId(Context context) {
        FCMCacheManager.clearRegistrationId(context);
    }

    //for specific case when mGoogleCloudMessaging failed to get the id
    private void createAndStoreDummyRegistrationID() {
        regid = PasswordGenerator.getAppId(context);
        gcmRegid = PasswordGenerator.getAppId(context);
        FCMCacheManager.storeRegId(regid, context);
    }


    private boolean isPlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog((Activity) context, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }
}
