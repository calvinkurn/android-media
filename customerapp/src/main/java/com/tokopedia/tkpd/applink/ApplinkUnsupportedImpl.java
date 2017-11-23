package com.tokopedia.tkpd.applink;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.core.appupdate.model.DetailUpdate;
import com.tokopedia.core.gcm.ApplinkUnsupported;
import com.tokopedia.design.BuildConfig;
import com.tokopedia.tkpd.R;

/**
 * @author  by alvarisi on 11/13/17.
 */

public class ApplinkUnsupportedImpl implements ApplinkUnsupported {

    private static final String MAINAPP_IS_NEED_UPDATE = "mainapp_applink_is_need_update";
    private static final String MAINAPP_LATEST_VERSION_CODE = "mainapp_latest_version_code";
    private static final String MAINAPP_UPDATE_TITLE = "mainapp_applink_update_title";
    private static final String MAINAPP_UPDATE_MESSAGE = "mainapp_applink_update_message";
    private static final String MAINAPP_UPDATE_LINK = "mainapp_applink_update_link";

    private Activity activity;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    public ApplinkUnsupportedImpl(Activity activity) {
        this.activity = activity;
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    @Override
    public void showAndCheckApplinkUnsupported() {
        if (firebaseRemoteConfig != null) {
            final DetailUpdate detail = new DetailUpdate();
            detail.setNeedUpdate(firebaseRemoteConfig.getBoolean(MAINAPP_IS_NEED_UPDATE));
            detail.setLatestVersionCode(firebaseRemoteConfig.getLong(MAINAPP_LATEST_VERSION_CODE));
            detail.setForceUpdate(false);
            detail.setUpdateTitle(firebaseRemoteConfig.getString(MAINAPP_UPDATE_TITLE));
            detail.setUpdateMessage(firebaseRemoteConfig.getString(MAINAPP_UPDATE_MESSAGE));
            detail.setUpdateLink(firebaseRemoteConfig.getString(MAINAPP_UPDATE_LINK));
            if (detail.isNeedUpdate() && BuildConfig.VERSION_CODE < detail.getLatestVersionCode()) {
                final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                        .setTitle(detail.getUpdateTitle())
                        .setMessage(detail.getUpdateMessage())
                        .setPositiveButton(activity.getString(R.string.deeplink_receiver_applink_update_button_title), null)
                        .setNegativeButton(activity.getString(R.string.deeplink_receiver_applink_update_button_cancel_title), null)
                        .setCancelable(true)
                        .create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.startActivity(
                                        new Intent(Intent.ACTION_VIEW, Uri.parse(detail.getUpdateLink()))
                                );

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
                alertDialog.show();
            }
        }
    }
}
