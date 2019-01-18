package com.tokopedia.logisticcommon.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.tokopedia.logisticcommon.R;

public class TkpdProgressDialog {

    public static int NORMAL_PROGRESS = 1;
    public static int MAIN_PROGRESS = 2;
    private Context context;
    private int state;
    private String msgLoading;
    private ProgressDialog progress;
    private Boolean isShow = false;

    public TkpdProgressDialog(Context context, int state, String msg) {
        this.context = context;
        this.state = state;
        msgLoading = msg;
    }

    public TkpdProgressDialog(Context context, int state) {
        this.context = context;
        this.state = state;
        msgLoading = "Loading...";
    }

    public void showDialog(String title, String message) {
        try {
            if (state == NORMAL_PROGRESS) {
                if (!isShow) {
                    progress = new ProgressDialog(context);
                    progress.setMessage(message);
                    progress.setTitle(title);
                    progress.setCancelable(false);
                    isShow = true;
                    progress.show();
                    isShow = true;
                }
            } else if (state == MAIN_PROGRESS) {
                progress = new ProgressDialog(context, R.style.CoolDialog);
                progress.show();
                progress.setContentView(R.layout.loader_logistic_module);
                progress.setCancelable(false);
                progress.setOnCancelListener(dialog -> ((Activity) context).finish());
                isShow = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean isProgress() {
        return isShow;
    }

    public void showDialog() {
        showDialog("", msgLoading);
    }

    public void dismiss() {
        if (progress != null) {
            isShow = false;
            if (progress.isShowing()) progress.dismiss();
        }
    }

    public ProgressDialog getProgress() {
        return progress;
    }
}