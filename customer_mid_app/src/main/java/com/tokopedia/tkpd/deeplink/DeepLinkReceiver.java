package com.tokopedia.tkpd.deeplink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

/**
 * Created by alvarisi on 1/31/17.
 */

public class DeepLinkReceiver extends BroadcastReceiver {

    private static final String EXTRA_SUCCESSFUL = "com.airbnb.deeplinkdispatch.EXTRA_SUCCESSFUL";
    private static final String EXTRA_URI = "com.airbnb.deeplinkdispatch.EXTRA_URI";

    @Override
    public void onReceive(final Context context, Intent intent) {
        String deepLinkUri = intent.getStringExtra(EXTRA_URI);

        if (intent.getBooleanExtra(EXTRA_SUCCESSFUL, false)) {
            Timber.d("Success deep linking: " + deepLinkUri);
        } else {
            Timber.d("Failed deep linking: " + deepLinkUri);
        }
    }
}