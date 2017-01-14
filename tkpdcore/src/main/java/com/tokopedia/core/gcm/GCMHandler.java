package com.tokopedia.core.gcm;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gcm.interactor.RegisterDeviceInteractor;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_ERROR;
import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_OK;

public class GCMHandler {

    private static final String TAG = GCMHandler.class.getSimpleName();
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
        if (listener != null) {
            gcmlistener = listener;
        } else {
            return;
        }

        if (isPlayServicesAvailable()) {
            registerDeviceToFCM();
            commitGCMProcess();
        } else {
            Log.d(TAG, " Play services not available");
        }
    }

    private void commitGCMProcess() {
        mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(context);
        gcmRegid = getRegistrationId(context);
        CommonUtils.dumper("start mGoogleCloudMessaging get");
        if (gcmRegid.isEmpty()) {
            registerGCM();
        } else {
            Localytics.setPushRegistrationId(gcmRegid);
        }
    }

    /**
     * This class for Localytics because our console localytics we set GCM supported.
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
                            param.setStatusCode(REGISTRATION_STATUS_OK);
                            param.setStatusMessage("GCM :: Device registered, registration ID=" + gcmRegid);
                            CommonUtils.dumper("GCM :: Device registered, registration ID=" + gcmRegid);
                            Localytics.setPushRegistrationId(gcmRegid);

                        } catch (IOException ex) {
                            param.setStatusCode(REGISTRATION_STATUS_ERROR);
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
                        if (response.getStatusCode() == REGISTRATION_STATUS_OK) {

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
        regid = getRegistrationId(context);
        CommonUtils.dumper(TAG + "start FCM get");
        if (TextUtils.isEmpty(regid)) {
            final RegisterDeviceInteractor interactor = new RegisterDeviceInteractor();
            interactor.registerDevice(new Subscriber<DeviceRegistrationDataResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(DeviceRegistrationDataResponse response) {
                    if (response.getStatusCode() != REGISTRATION_STATUS_OK) {
                        TrackingUtils
                                .eventError(context.getClass().toString(), response.getStatusMessage());
                    }
                    interactor.storeRegistrationDevice(response.getDeviceRegistration());
                    gcmlistener.onGCMSuccess(regid);
                }
            });
        } else {
            gcmlistener.onGCMSuccess(regid);
        }
    }

    public static String getRegistrationId(Context context) {
        return FCMCacheManager.getRegistrationId(context);
    }

    public static void clearRegistrationId(Context context) {
        FCMCacheManager.clearRegistrationId(context);
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
