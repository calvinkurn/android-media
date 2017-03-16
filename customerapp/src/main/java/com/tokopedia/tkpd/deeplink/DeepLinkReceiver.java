package com.tokopedia.tkpd.deeplink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;

/**
 * Created by alvarisi on 1/31/17.
 */

public class DeepLinkReceiver extends BroadcastReceiver {
    private static final String TAG = DeepLinkReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String deepLinkUri = intent.getStringExtra(DeepLinkHandler.EXTRA_URI);

        if (intent.getBooleanExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, false)) {
            Log.i(TAG, "Success deep linking: " + deepLinkUri);
        } else {
            Intent deeplinkIntent = new Intent(context, DeepLinkActivity.class);
            deeplinkIntent.setData(Uri.parse(deepLinkUri));
            context.startActivity(deeplinkIntent);
        }
    }
}