package com.tokopedia.design.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by meyta on 2/14/18.
 */

public abstract class Dialog {

    private Activity context;

    private AlertDialog alertDialog;

    public abstract int layoutResId();

    public abstract void initView(View dialogView);

    public Dialog(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        View dialogView = context.getLayoutInflater().inflate(layoutResId(), null);
        initView(dialogView);

        alertDialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void show() {
        if (alertDialog != null)
            alertDialog.show();
    }
}