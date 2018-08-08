package com.tokopedia.core.appupdate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.appupdate.model.DetailUpdate;

/**
 * Created by okasurya on 7/26/17.
 */

public class AppUpdateDialogBuilder {
    private Activity activity;
    private DetailUpdate detail;
    private AlertDialog alertDialog;

    public AppUpdateDialogBuilder(Activity activity, DetailUpdate detail) {
        this.activity = activity;
        this.detail = detail;
    }

    public AlertDialog getAlertDialog() {
        alertDialog = new AlertDialog.Builder(activity)
                .setTitle(detail.getUpdateTitle())
                .setMessage(detail.getUpdateMessage())
                .setPositiveButton(R.string.appupdate_update, null)
                .setNegativeButton(detail.isForceUpdate() ?
                        R.string.appupdate_close : R.string.appupdate_later, null)
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
                        activity.startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(detail.getUpdateLink()))
                        );
                        UnifyTracking.eventClickAppUpdate(detail.isForceUpdate());

                        if(!detail.isForceUpdate()) dialog.dismiss();
                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UnifyTracking.eventClickCancelAppUpdate(detail.isForceUpdate());

                        dialog.dismiss();
                        if(detail.isForceUpdate()) activity.finish();
                    }
                });
            }
        });

        return alertDialog;
    }
}
