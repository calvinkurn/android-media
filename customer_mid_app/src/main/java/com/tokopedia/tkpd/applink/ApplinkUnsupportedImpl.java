package com.tokopedia.tkpd.applink;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.appupdate.model.DataUpdateApp;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

/**
 * @author by alvarisi on 11/13/17.
 */

public class ApplinkUnsupportedImpl implements ApplinkUnsupported {

    private static final String ANDROID_CUSTOMER_APP_UPDATE = "android_customer_app_update";

    private Activity activity;
    private RemoteConfig remoteConfig;

    public ApplinkUnsupportedImpl(Activity activity) {
        this.activity = activity;
        remoteConfig = new FirebaseRemoteConfigImpl(activity);
    }

    @Override
    public void showAndCheckApplinkUnsupported() {
        if (remoteConfig == null) {
            return;
        }
        String dataAppUpdate = remoteConfig.getString(ANDROID_CUSTOMER_APP_UPDATE);
        if (!TextUtils.isEmpty(dataAppUpdate)) {
            Gson gson = new Gson();
            DataUpdateApp dataUpdateApp = gson.fromJson(dataAppUpdate, DataUpdateApp.class);
            if (dataUpdateApp != null) {
                DetailUpdate detail = generateDetailUpdate(dataUpdateApp);
                if (detail.isNeedUpdate()) {
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

    private DetailUpdate generateDetailUpdate(DataUpdateApp dataUpdateApp) {
        DetailUpdate detailUpdate = new DetailUpdate();
        detailUpdate.setInAppUpdateEnabled(false);
        if (dataUpdateApp.isIsForceEnabled() && GlobalConfig.VERSION_CODE < dataUpdateApp.getLatestVersionForceUpdate()) {
            detailUpdate.setLatestVersionCode(dataUpdateApp.getLatestVersionForceUpdate());
            detailUpdate.setNeedUpdate(true);
            detailUpdate.setForceUpdate(true);
        } else if (dataUpdateApp.isIsOptionalEnabled() && GlobalConfig.VERSION_CODE < dataUpdateApp.getLatestVersionOptionalUpdate()) {
            detailUpdate.setLatestVersionCode(dataUpdateApp.getLatestVersionOptionalUpdate());
            detailUpdate.setNeedUpdate(true);
            detailUpdate.setForceUpdate(false);
        } else {
            detailUpdate.setNeedUpdate(false);
        }
        detailUpdate.setUpdateTitle(dataUpdateApp.getTitle());
        detailUpdate.setUpdateMessage(dataUpdateApp.getMessage());
        detailUpdate.setUpdateLink(dataUpdateApp.getLink());
        return detailUpdate;
    }
}
