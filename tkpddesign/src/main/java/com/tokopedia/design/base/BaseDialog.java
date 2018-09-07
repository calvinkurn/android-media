package com.tokopedia.design.base;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

/**
 * Created by meyta on 2/14/18.
 */

public abstract class BaseDialog {

    protected Activity context;

    private AlertDialog alertDialog;

    public abstract int layoutResId();

    public abstract void initView(View dialogView);

    public abstract void initListener(AlertDialog dialog);

    public BaseDialog(Activity context) {
        if (context == null) {
            Log.e(this.getClass().getSimpleName(), "Context NULL");
            return;
        }
        this.context = context;
        init();
    }

    protected void init() {
        View dialogView = context.getLayoutInflater().inflate(layoutResId(), null);
        initView(dialogView);

        alertDialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        initListener(alertDialog);
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void show() {
        if (alertDialog != null)
            alertDialog.show();
    }

    public void dismiss() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }
}