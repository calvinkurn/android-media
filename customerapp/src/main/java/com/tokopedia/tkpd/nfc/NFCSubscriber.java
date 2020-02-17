package com.tokopedia.tkpd.nfc;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConsInternalDigital;

public class NFCSubscriber implements Application.ActivityLifecycleCallbacks {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    public static void onNewIntent(Context context, Intent intent) {
        if (intent != null &&
                (intent.getAction() == NfcAdapter.ACTION_TAG_DISCOVERED ||
                        intent.getAction() == NfcAdapter.ACTION_TECH_DISCOVERED)) {
            Intent newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.SMARTCARD, "calling_from_nfc");
            newIntent.putExtras(intent.getExtras());
            newIntent.setAction(intent.getAction());
            context.startActivity(newIntent);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (activity != null && activity instanceof BaseActivity && ((BaseActivity) activity).isAllowNFC()) {
            pendingIntent = PendingIntent.getActivity(activity, 0,
                    activity.getIntent().setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (nfcAdapter != null && activity instanceof BaseActivity && ((BaseActivity) activity).isAllowNFC()) {
            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, new IntentFilter[]{}, null);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (nfcAdapter != null && activity instanceof BaseActivity && ((BaseActivity) activity).isAllowNFC())
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
