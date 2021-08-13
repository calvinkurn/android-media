package com.tokopedia.tkpd.deeplink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;

import timber.log.Timber;

/**
 * Created by alvarisi on 1/31/17.
 */

public class DeepLinkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String deepLinkUri = intent.getStringExtra(DeepLinkHandler.EXTRA_URI);

        if (intent.getBooleanExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, false)) {
            Timber.d("Success deep linking: " + deepLinkUri);
        } else {
            Timber.d("Failed deep linking: " + deepLinkUri);
        }
    }
}