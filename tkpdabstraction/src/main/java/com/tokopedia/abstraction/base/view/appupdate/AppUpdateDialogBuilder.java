package com.tokopedia.abstraction.base.view.appupdate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

    public AlertDialog getAlertDialogAndShowPopUpUpdate() {
        alertDialog = new AlertDialog.Builder(activity)
                .setTitle(detail.getUpdateTitle())
                .setMessage(detail.getUpdateMessage())
                .setPositiveButton(R.string.appupdate_update, null)
                .setNegativeButton(detail.isForceUpdate() ? R.string.appupdate_close : R.string.appupdate_later, null)
                .setCancelable(false)
                .create();

        if (detail.isInAppUpdateEnabled()) {
            if (detail.isForceUpdate()) {
                AppUpdateManagerWrapper.checkAndDoImmediateUpdate(activity, () -> {
                    /* on Error */
                    alertDialog.show();
                    return null;
                }, /* onFinished */ () -> null);
            } else {
                AppUpdateManagerWrapper.checkAndDoFlexibleUpdate(activity, onProgressMessage -> {
                    // if in progress
                    Toast.makeText(activity, onProgressMessage, Toast.LENGTH_LONG).show();
                    return null;
                }, () -> {
                    // if flexible update fail or cannot be operated
                    alertDialog.show();
                    return null;
                }, () -> {
                    // action after do the checking, close the dialog
                    return null;
                });
            }
        }

        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(v -> {
                negativeButton.setEnabled(false);
                positiveButton.setEnabled(false);
                if (detail.isInAppUpdateEnabled()) {
                    goToPlayStore();
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

    private Boolean isForceUpdate() {
        return detail.isInAppUpdateEnabled() && detail.isForceUpdate();
    }

    private Boolean isFlexibleUpdate() {
        return detail.isInAppUpdateEnabled() && !detail.isForceUpdate();
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
