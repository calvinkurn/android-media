package com.tokopedia.core.gcm;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gcm.data.RegisterDeviceInteractor;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import timber.log.Timber;

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

    public void actionRegisterOrUpdateDevice(GCMHandlerListener listener, boolean isPlayServiceAvailable) {
        if (listener != null) {
            gcmlistener = listener;
        } else {
            return;
        }

        if (isPlayServiceAvailable) {
            registerDeviceToFCM();
        } else {
            Log.d(TAG, " Play services not available");
        }
    }

    private void registerDeviceToFCM() {
        regid = getRegistrationId(context);
        Timber.d(TAG + "start FCM get");
        if (TextUtils.isEmpty(regid)) {
            final RegisterDeviceInteractor interactor = new RegisterDeviceInteractor(context);
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
                                .eventError(context, context.getClass().toString(), response.getStatusMessage());
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


    // this code should be on main thread.
    public static boolean isPlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(activity);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                if (!activity.isFinishing()) {
                    googleAPI.getErrorDialog(activity, result,
                            PLAY_SERVICES_RESOLUTION_REQUEST, dialog -> {
                                Map<String, String> messageMap = new HashMap<>();
                                messageMap.put("type", "gcm");
                                messageMap.put("fingerprint", Build.FINGERPRINT);
                                ServerLogger.log(Priority.P1, "PLAY_SERVICE_ERROR", messageMap);
                            }).show();
                }
            }

            return false;
        } else {
            return true;
        }
    }
}
