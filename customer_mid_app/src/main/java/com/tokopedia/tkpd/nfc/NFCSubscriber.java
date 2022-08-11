package com.tokopedia.tkpd.nfc;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConsInternalDigital;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.common_electronic_money.util.CardUtils;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class NFCSubscriber implements Application.ActivityLifecycleCallbacks {

    private static final String TAG_EMONEY_TIME_CHECK_LOGIC = "EMONEY_TIME_CHECK_LOGIC";
    private static final String TAG_EMONEY_DEBUG = "EMONEY_DEBUG";
    private static final int DIVIDER = 1000000;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private PermissionCheckerHelper permissionCheckerHelper;

    public static void onNewIntent(Context context, Intent intent) {
        long startTime = System.nanoTime();
        if (intent != null &&
                (intent.getAction() == NfcAdapter.ACTION_TAG_DISCOVERED ||
                        intent.getAction() == NfcAdapter.ACTION_TECH_DISCOVERED)) {
            Intent newIntent;
            if (CardUtils.isEmoneyCard(intent) || CardUtils.isTapcashCard(intent)) {
                newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY, DigitalExtraParam.EXTRA_NFC);
            } else if (CardUtils.isBrizziCard(intent)){
                newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI, DigitalExtraParam.EXTRA_NFC);
            } else {
                newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY, DigitalExtraParam.EXTRA_NFC);
            }
            newIntent.replaceExtras(intent);
            newIntent.setAction(intent.getAction());

            String durationString = getDuration(startTime);
            newIntent.putExtra(TAG_EMONEY_TIME_CHECK_LOGIC, durationString);
            sendLog(durationString);

            context.startActivity(newIntent);
        }
    }

    private static String getDuration(long startTime) {
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/DIVIDER;
        return ""+duration+" ms";
    }

    private static void sendLog(String durationString) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put(TAG_EMONEY_TIME_CHECK_LOGIC, durationString);
        ServerLogger.log(Priority.P2, TAG_EMONEY_DEBUG, messageMap);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (activity != null) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
            if (nfcAdapter == null) return;
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (nfcAdapter != null) {
            permissionCheckerHelper = new PermissionCheckerHelper();
            permissionCheckerHelper.checkPermission(activity,
                    PermissionCheckerHelper.Companion.PERMISSION_NFC,
                    (new PermissionCheckerHelper.PermissionCheckListener() {
                        @Override
                        public void onPermissionDenied(@NotNull String permissionText) {
                            permissionCheckerHelper.onPermissionDenied(activity.getApplicationContext(), permissionText);
                        }

                        @Override
                        public void onNeverAskAgain(@NotNull String permissionText) {
                            permissionCheckerHelper.onNeverAskAgain(activity.getApplicationContext(), permissionText);
                        }

                        @Override
                        public void onPermissionGranted() {
                            try {
                                if (nfcAdapter.isEnabled()) {
                                    pendingIntent = PendingIntent.getActivity(activity, 0,
                                            activity.getIntent().setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                                    nfcAdapter.enableForegroundDispatch(activity, pendingIntent, new IntentFilter[]{}, null);
                                }
                            } catch (SecurityException e) {
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        }
                    })
                    , activity.getBaseContext().getResources().getString(R.string.permission_emoney_not_granted)
            );

        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (nfcAdapter != null)
            try {
                nfcAdapter.disableForegroundDispatch(activity);
            } catch (SecurityException e) {
                FirebaseCrashlytics.getInstance().recordException(e);
            }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
