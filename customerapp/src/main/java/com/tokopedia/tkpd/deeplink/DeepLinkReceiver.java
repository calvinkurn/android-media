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
import com.tokopedia.design.BuildConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.fcm.appupdate.FirebaseRemoteAppUpdate;
import com.tokopedia.tkpd.home.ParentIndexHome;

/**
 * Created by alvarisi on 1/31/17.
 */

public class DeepLinkReceiver extends BroadcastReceiver {
    private static final String MAINAPP_IS_NEED_UPDATE = "mainapp_applink_is_need_update";
    private static final String MAINAPP_LATEST_VERSION_CODE = "mainapp_applink_latest_version_code";
    private static final String MAINAPP_UPDATE_TITLE = "mainapp_applink_update_title";
    private static final String MAINAPP_UPDATE_MESSAGE = "mainapp_applink_update_message";
    private static final String MAINAPP_UPDATE_LINK = "mainapp_applink_update_link";

    private static final String TAG = DeepLinkReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        String deepLinkUri = intent.getStringExtra(DeepLinkHandler.EXTRA_URI);

        if (intent.getBooleanExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, false)) {
            CommonUtils.dumper("Success deep linking: " + deepLinkUri);
        } else {
            CommonUtils.dumper("Failed deep linking: " + deepLinkUri);

            FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            if (firebaseRemoteConfig != null) {
                final DetailUpdate detail = new DetailUpdate();
                detail.setNeedUpdate(firebaseRemoteConfig.getBoolean(MAINAPP_IS_NEED_UPDATE));
                detail.setLatestVersionCode(firebaseRemoteConfig.getLong(MAINAPP_LATEST_VERSION_CODE));
                detail.setForceUpdate(false);
                detail.setUpdateTitle(firebaseRemoteConfig.getString(MAINAPP_UPDATE_TITLE));
                detail.setUpdateMessage(firebaseRemoteConfig.getString(MAINAPP_UPDATE_MESSAGE));
                detail.setUpdateLink(firebaseRemoteConfig.getString(MAINAPP_UPDATE_LINK));
                if (detail.isNeedUpdate() && BuildConfig.VERSION_CODE < detail.getLatestVersionCode()) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setTitle(detail.getUpdateTitle())
                            .setMessage(detail.getUpdateMessage())
                            .setPositiveButton(context.getString(R.string.deeplink_receiver_applink_update_button_title), null)
                            .setNegativeButton(context.getString(R.string.deeplink_receiver_applink_update_button_cancel_title))
                            .setCancelable(false)
                            .create();

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(final DialogInterface dialog) {
                            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    context.startActivity(
                                            new Intent(Intent.ACTION_VIEW, Uri.parse(detail.getUpdateLink()))
                                    );
                                    UnifyTracking.eventClickAppUpdate(detail.isForceUpdate());

                                    if (!detail.isForceUpdate()) dialog.dismiss();
                                }
                            });

                            negativeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }
            }

        }
    }
}