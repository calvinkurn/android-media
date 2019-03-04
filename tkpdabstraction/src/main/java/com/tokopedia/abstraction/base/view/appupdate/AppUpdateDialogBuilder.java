package com.tokopedia.abstraction.base.view.appupdate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.inappupdate.AppUpdateManagerWrapper;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;

/**
 * Created by okasurya on 7/26/17.
 */

public class AppUpdateDialogBuilder {
    private Activity activity;
    private DetailUpdate detail;
    private AlertDialog alertDialog;
    private Listener listener;

    public AppUpdateDialogBuilder(Activity activity, DetailUpdate detail, Listener listener) {
        this.activity = activity;
        this.detail = detail;
        this.listener = listener;
    }

    public AlertDialog getAlertDialog() {
        alertDialog = new AlertDialog.Builder(activity)
                .setTitle(detail.getUpdateTitle())
                .setMessage(detail.getUpdateMessage())
                .setPositiveButton(R.string.appupdate_update, null)
                .setNegativeButton(detail.isForceUpdate() ? R.string.appupdate_close : R.string.appupdate_later, null)
                .setCancelable(false)
                .create();

        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(v -> {
                if (detail.isForceUpdate()) {
                    goToPlayStore();
                } else {
                    negativeButton.setEnabled(false);
                    positiveButton.setEnabled(false);
                    //check if inappupdate flag is true
                    if (detail.isInAppUpdateEnabled() &&
                            GlobalConfig.VERSION_CODE >= Build.VERSION_CODES.LOLLIPOP) {
                        AppUpdateManagerWrapper.checkFlexibleUpdateAllowed(activity, new Function3<Boolean, Boolean, String, Unit>() {
                            @Override
                            public Unit invoke(Boolean allowFlexUpdate, Boolean isOnProgress, String onProgressMessage) {
                                if (allowFlexUpdate) {
                                    if (isOnProgress) {
                                        ToasterNormal.show(activity, onProgressMessage);
                                    } else {
                                        try {
                                            boolean successTriggerUpdate = AppUpdateManagerWrapper.doFlexibleUpdate(activity, 12354);
                                            if (!successTriggerUpdate) {
                                                goToPlayStore();
                                            }
                                        } catch (Exception e) {
                                            goToPlayStore();
                                        }
                                    }
                                } else {
                                    goToPlayStore();
                                }
                                dialog.dismiss();
                                return null;
                            }
                        });
                    } else {
                        goToPlayStore();
                        dialog.dismiss();
                    }
                }
                listener.onPositiveButtonClicked(detail);
            });

            negativeButton.setOnClickListener(v -> {
                dialog.dismiss();
                if (detail.isForceUpdate()) activity.finish();
                listener.onNegativeButtonClicked(detail);
            });
        });

        return alertDialog;
    }

    private void doFlexibleUpdate(){

    }

    private void goToPlayStore(){
        activity.startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(detail.getUpdateLink()))
        );
    }

    public interface Listener {
        void onPositiveButtonClicked(DetailUpdate detail);

        void onNegativeButtonClicked(DetailUpdate detail);
    }
}
