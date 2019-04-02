package com.tokopedia.tkpd.deeplink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tkpd.library.utils.CommonUtils;

/**
 * Created by alvarisi on 1/31/17.
 */

public class DeepLinkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String deepLinkUri = intent.getStringExtra(DeepLinkHandler.EXTRA_URI);

        if (intent.getBooleanExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, false)) {
            CommonUtils.dumper("Success deep linking: " + deepLinkUri);
        } else {
            CommonUtils.dumper("Failed deep linking: " + deepLinkUri);
        }
    }
}