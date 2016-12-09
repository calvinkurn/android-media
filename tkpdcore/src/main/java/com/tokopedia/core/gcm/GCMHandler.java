package com.tokopedia.core.gcm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.analytics.TrackingUtils;

public class GCMHandler {

    private static final String TAG = GCMHandler.class.getSimpleName();
    private static int STATUS_OK = 1;
    private static int STATUS_ERROR = 2;
    private String SENDER_ID = "673352445777";
    private Context context;
    private String regid;
    private GCMHandlerListener gcmlistener;

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
            Log.d(TAG, GCMHandler.class.getSimpleName()+" failed to get gcm id !!!");
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)context, 0);
            if (dialog != null) {
                dialog.show();
            }
        }

    }

    private class registerInBackground extends AsyncTask<Void, Void, GCMParam> {

        @Override
        protected GCMParam doInBackground(Void... params) {
            GCMParam param = new GCMParam();
            regid = FirebaseInstanceId.getInstance().getToken();
            if (!TextUtils.isEmpty(regid)) {
                storeRegistrationId(regid);
                param.statusCode = STATUS_OK;
                param.statusMessage = "Device registered, registration ID=" + regid;
                CommonUtils.dumper("Device registered, registration ID=" + regid);
            } else {
                param.statusCode = STATUS_ERROR;
                param.statusMessage = "GCM registration error";
                CommonUtils.dumper("GCM registration error");
            }
            return param;
        }

        @Override
        protected void onPostExecute(GCMParam param) {
            if (param.statusCode != STATUS_OK) {
                TrackingUtils
                        .eventError(context.getClass().toString(), param.statusMessage);
                storeDummyGCMID();
            }
            Log.d(TAG, "GCM RegistrationId: " + regid);
            gcmlistener.onGCMSuccess(regid);
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

    //for specific case when gcm failed to get the id
    private void storeDummyGCMID() {
        regid = PasswordGenerator.getAppId(context);
        storeRegistrationId(regid);
    }


}
