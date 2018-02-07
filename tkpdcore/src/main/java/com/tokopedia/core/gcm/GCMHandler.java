package com.tokopedia.core.gcm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gcm.data.RegisterDeviceInteractor;
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
    private GCMHandlerListener gcmlistener;

    public GCMHandler(Context context) {
        this.context = context;
    }

    public static String getRegistrationId(Context context) {
        return FCMCacheManager.getRegistrationId(context);
    }

    public void actionRegisterOrUpdateDevice(GCMHandlerListener listener) {
        if (listener != null) {
            gcmlistener = listener;
        } else {
            return;
        }

        if (isPlayServicesAvailable()) {
            registerDeviceToFCM();
        } else {
            Log.d(TAG, " Play services not available");
        }
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

    public String getRegistrationId() {
        return FCMCacheManager.getRegistrationId(context);
    }

    private boolean isPlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing() && googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog((Activity) context, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }
}
