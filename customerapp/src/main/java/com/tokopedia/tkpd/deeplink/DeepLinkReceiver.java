package com.tokopedia.tkpd.deeplink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.appupdate.AppUpdateDialogBuilder;
import com.tokopedia.core.appupdate.ApplicationUpdate;
import com.tokopedia.core.appupdate.model.DetailUpdate;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.fcm.appupdate.FirebaseRemoteAppUpdate;
import com.tokopedia.tkpd.home.ParentIndexHome;

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