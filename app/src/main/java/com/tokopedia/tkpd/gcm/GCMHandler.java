package com.tokopedia.tkpd.gcm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.network.retrofit.utils.DialogNoConnection;
import com.tokopedia.tkpd.util.PasswordGenerator;
import com.tokopedia.tkpd.analytics.TrackingUtils;

import java.io.IOException;

public class GCMHandler {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    // private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static int STATUS_OK = 1;
    private static int STATUS_ERROR = 2;
    private static int STATUS_WAITING = 0;
    private String SENDER_ID = "673352445777";
    private GoogleCloudMessaging gcm;
    private Context context;
    private String regid;
    private GCMHandlerListener gcmlistener;
    private int retryAttempt = 0;

    public interface GCMHandlerListener {
        void onGCMSuccess(String gcmId);
    }

    public GCMHandler (Context context) {
        this.context = context;
    }

    public String getSenderID(){
        return SENDER_ID;
    }

    public void commitGCMProcess (GCMHandlerListener listener) {
        if(listener!=null)
            gcmlistener = listener;

        Integer resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode == ConnectionResult.SUCCESS) {
            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId(context);
            CommonUtils.dumper("start gcm get");
            if (regid.isEmpty()) {
                new registerInBackground().execute();
            } else {

                Localytics.setPushRegistrationId(regid);

                if(listener!=null)
                    gcmlistener.onGCMSuccess(regid);
            }
        } else {
            Log.d("MNORMANSYAH", GCMHandler.class.getSimpleName()+" failed to get gcm id !!!");
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)context, 0);
            if (dialog != null) {
                //This dialog will help the user update to the latest GooglePlayServices
                dialog.show();
            }
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    private class registerInBackground extends AsyncTask<Void, Void, GCMParam> {

        @Override
        protected GCMParam doInBackground(Void... params) {
            GCMParam param = new GCMParam();
            int status = STATUS_WAITING;
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                storeRegistrationId(regid);
                param.statusCode = STATUS_OK;
                param.statusMessage = "Device registered, registration ID=" + regid;
                CommonUtils.dumper("Device registered, registration ID=" + regid);

                Localytics.setPushRegistrationId(regid);

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.


                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                //storeRegistrationId(context, regid);
            } catch (IOException ex) {
                param.statusCode = STATUS_ERROR;
                param.statusMessage = ex.getMessage();
                CommonUtils.dumper("Error :" + ex.getMessage());
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return param;
        }

        @Override
        protected void onPostExecute(GCMParam param) {
            if (param.statusCode == STATUS_OK) {
                gcmlistener.onGCMSuccess(regid);
            } else {
                if (retryAttempt < 5) {
                    retryAttempt++;
                    new registerInBackground().execute();
                } else {
                    TrackingUtils.eventError(context.getClass().toString(), param.statusMessage);
                    storeDummyGCMID();
                    gcmlistener.onGCMSuccess(regid);
                }
            }
        }

    }

    private void storeRegistrationId(String id) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "GCM_STORAGE");
        cache.putString("gcm_id", id);
        cache.applyEditor();
    }

    public static String getRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "GCM_STORAGE");
        return cache.getString("gcm_id", "");
    }

    public static void clearRegistrationId(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "GCM_STORAGE");
        cache.putString("gcm_id", null);
        cache.applyEditor();
    }

    public class GCMParam {
        int statusCode;
        String statusMessage;
    }

    private void showGCMRegisterError(String msg) {
        DialogNoConnection.create(context,
                context.getResources().getString(R.string.error_connection_problem) + ".\n"
                        + context.getResources().getString(R.string.error_no_connection2) + ".\n \n"
                        + msg,
                new DialogNoConnection.ActionListener() {
                    @Override
                    public void onRetryClicked() {
                        retryAttempt = 0;
                        new registerInBackground().execute();
                    }
                }).show();
    }

    //for specific case when gcm failed to get the id
    private void storeDummyGCMID() {
        regid = PasswordGenerator.getAppId(context);
        storeRegistrationId(regid);
    }


}
