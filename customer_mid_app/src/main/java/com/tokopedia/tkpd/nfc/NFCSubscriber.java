package com.tokopedia.tkpd.nfc;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConsInternalDigital;
import com.tokopedia.common_digital.common.constant.DigitalExtraParam;
import com.tokopedia.common_electronic_money.util.CardUtils;
import com.tokopedia.utils.permission.PermissionCheckerHelper

public class NFCSubscriber implements Application.ActivityLifecycleCallbacks {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private PermissionCheckerHelper permissionCheckerHelper;

    public static void onNewIntent(Context context, Intent intent) {
        if (intent != null &&
                (intent.getAction() == NfcAdapter.ACTION_TAG_DISCOVERED ||
                        intent.getAction() == NfcAdapter.ACTION_TECH_DISCOVERED)) {
            Intent newIntent;
            if (CardUtils.cardIsEmoney(intent)) {
                newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY, DigitalExtraParam.EXTRA_NFC);
            } else {
                newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI, DigitalExtraParam.EXTRA_NFC);
            }
            newIntent.replaceExtras(intent);
            newIntent.setAction(intent.getAction());
            context.startActivity(newIntent);
        }
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
            permissionCheckerHelper = new PermissionChecker();
            pendingIntent = PendingIntent.getActivity(activity, 0,
                    activity.getIntent().setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            permissionCheckerHelper.checkPermission(activity,
                    PermissionCheckerHelper.PERMISSION_NFC,
                    new PermissionCheckerHelper.PermissionCheckListener {
                @Override
                public void onPermissionDenied(permissionText: String) {
                    permissionCheckerHelper.onPermissionDenied(it, permissionText)
                }

                @Override
                public void onNeverAskAgain(permissionText: String) {
                    permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                }

                @Override
                public void onPermissionGranted() {
                    try {
                        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, new IntentFilter[]{}, null);
                    } catch(SecurityException e){

                    }
                }
            },
            "Aplikasi ini membutuhkan izin untuk mengakses NFC"
            );

        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(activity);
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
