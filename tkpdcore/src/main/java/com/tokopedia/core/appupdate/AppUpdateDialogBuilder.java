package com.tokopedia.core.appupdate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.R;
import com.tokopedia.core.appupdate.model.DetailUpdate;

/**
 * Created by okasurya on 7/26/17.
 */

public class AppUpdateDialogBuilder {
    public static AlertDialog getAlertDialog(final Activity activity, final DetailUpdate detail) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
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

                        if(!detail.isForceUpdate()) dialog.dismiss();
                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(detail.isForceUpdate()) activity.finish();
                    }
                });
            }
        });

        return alertDialog;
    }
}
