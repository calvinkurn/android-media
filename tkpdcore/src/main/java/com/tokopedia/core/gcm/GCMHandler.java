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
    private String SENDER_ID = "673352445777";
    private Context context;
    private String regid;
    private String gcmRegid;
    private GCMHandlerListener gcmlistener;
    private GoogleCloudMessaging gcm;
    private int retryAttempt = 0;

    public GCMHandler (Context context) {
        this.context = context;
    }

    public String getSenderID(){
        return SENDER_ID;
    }

    public void commitFCMProcess(GCMHandlerListener listener) {
        if(listener!=null)
            gcmlistener = listener;

        if (checkPlayServices()) {
            regid = getRegistrationId(context);
            CommonUtils.dumper(TAG+ "start gcm get");
            if (regid.isEmpty()) {
                registerFCM();
            } else {
                if(listener!=null)
                    gcmlistener.onGCMSuccess(regid);
            }
        } else {
            Log.d(TAG, " failed to get gcm id !!!");
        }
    }

    public void commitGCMProcess () {
        if(checkPlayServices()){
            gcm = GoogleCloudMessaging.getInstance(context);
            gcmRegid = getRegistrationId(context);
            CommonUtils.dumper("start gcm get");
            if (gcmRegid.isEmpty()) {
                registerGCM();
            } else {

                Localytics.setPushRegistrationId(gcmRegid);

            }
        } else {
            Log.d(TAG, " failed to get gcm id !!!");
        }
    }

    /**
     *  :)
     */
    private void registerGCM(){
        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<Boolean, GCMParam>(){
                    @Override
                    public GCMParam call(Boolean aBoolean) {
                        GCMParam param = new GCMParam();
                        try {
                            if (gcm == null) {
                                gcm = GoogleCloudMessaging.getInstance(context);
                            }
                            gcmRegid = gcm.register(SENDER_ID);
                            GCMCacheManager.storeGCMRegId(gcmRegid, context);
                            param.statusCode = STATUS_OK;
                            param.statusMessage = "GCM :: Device registered, registration ID=" + gcmRegid;
                            CommonUtils.dumper("GCM :: Device registered, registration ID=" + gcmRegid);

                            Localytics.setPushRegistrationId(gcmRegid);

                        } catch (IOException ex) {
                            param.statusCode = STATUS_ERROR;
                            param.statusMessage = ex.getMessage();
                            CommonUtils.dumper("Error :" + ex.getMessage());
                        }
                        return param;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GCMParam>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GCMParam gcmParam) {
                        if (gcmParam.statusCode == STATUS_OK) {

                        } else {
                            if (retryAttempt < 5) {
                                retryAttempt++;
                                registerGCM();
                            } else {
                                TrackingUtils.eventError(context.getClass().toString(), gcmParam.statusMessage);
                                storeDummyGCMID();
                            }
                        }
                    }
                });
    }

    private void registerFCM(){
        Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<Boolean, GCMParam>(){
                    @Override
                    public GCMParam call(Boolean aBoolean) {
                        GCMParam param = new GCMParam();
                        regid = FirebaseInstanceId.getInstance().getToken();
                        if (!TextUtils.isEmpty(regid)) {
                            GCMCacheManager.storeRegId(regid, context);
                            param.statusCode = STATUS_OK;
                            param.statusMessage = "FCM :: Device registered, registration ID=" + regid;
                            CommonUtils.dumper("FCM :: Device registered, registration ID=" + regid);
                        } else {
                            param.statusCode = STATUS_ERROR;
                            param.statusMessage = "FCM :: GCM registration error";
                            CommonUtils.dumper("FCM :: GCM registration error");
                        }
                        return param;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GCMParam>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GCMParam gcmParam) {
                        if (gcmParam.statusCode != STATUS_OK) {
                            TrackingUtils
                                    .eventError(context.getClass().toString(), gcmParam.statusMessage);
                            storeDummyGCMID();
                        }
                        Log.d(TAG, "FCM :: RegistrationId: " + regid);
                        gcmlistener.onGCMSuccess(regid);
                    }
                });
    }

    public static String getRegistrationId(Context context) {
        return GCMCacheManager.getRegistrationId(context);
    }

    public static void clearRegistrationId(Context context) {
        GCMCacheManager.clearRegistrationId(context);
    }

    public class GCMParam {
        int statusCode;
        String statusMessage;
    }

    //for specific case when gcm failed to get the id
    private void storeDummyGCMID() {
        regid = PasswordGenerator.getAppId(context);
        gcmRegid = PasswordGenerator.getAppId(context);
        GCMCacheManager.storeRegId(regid, context);
        GCMCacheManager.storeGCMRegId(gcmRegid, context);
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog((Activity) context, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }
}
