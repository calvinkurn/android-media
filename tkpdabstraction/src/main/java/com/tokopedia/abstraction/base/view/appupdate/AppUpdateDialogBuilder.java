package com.tokopedia.abstraction.base.view.appupdate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.tokopedia.abstraction.BuildConfig;
import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.abstraction.common.utils.GlobalConfig;

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
                    //check if inappupdate flag is true
                    if (detail.isInAppUpdateEnabled() &&
                            GlobalConfig.VERSION_CODE >= Build.VERSION_CODES.LOLLIPOP) {
                        
                    } else {
                        goToPlayStore();
                    }
                    dialog.dismiss();
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
