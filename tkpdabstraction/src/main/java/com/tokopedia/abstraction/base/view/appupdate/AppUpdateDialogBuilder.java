package com.tokopedia.abstraction.base.view.appupdate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.inappupdate.AppUpdateManagerWrapper;

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
//                .setCancelable(false)
                .create();

        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(v -> {
                negativeButton.setEnabled(false);
                positiveButton.setEnabled(false);
                if (detail.isInAppUpdateEnabled()) {
                    if (detail.isForceUpdate()) {
                        AppUpdateManagerWrapper.checkAndDoImmediateUpdate(activity, () -> {
                            /* on Error */
                            goToPlayStore();
                            return null;
                        }, /* onFinished */ () -> {
                            negativeButton.setEnabled(true);
                            positiveButton.setEnabled(true);
                            return null;
                        });
                    } else { // flexible update
                        AppUpdateManagerWrapper.checkAndDoFlexibleUpdate(activity, onProgressMessage -> {
                            // if in progress
                            Toast.makeText(activity, onProgressMessage, Toast.LENGTH_LONG).show();
                            return null;
                        }, () -> {
                            // if flexible update fail or cannot be operated
                            goToPlayStore();
                            return null;
                        }, () -> {
                            // action after do the checking, close the dialog
                            dialog.dismiss();
                            return null;
                        });
                    }
                } else {
                    goToPlayStore();
                    if (detail.isForceUpdate()) {
                        negativeButton.setEnabled(true);
                        positiveButton.setEnabled(true);
                    } else {
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

    private void goToPlayStore() {
        activity.startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(detail.getUpdateLink()))
        );
    }

    public interface Listener {
        void onPositiveButtonClicked(DetailUpdate detail);

        void onNegativeButtonClicked(DetailUpdate detail);
    }
}
