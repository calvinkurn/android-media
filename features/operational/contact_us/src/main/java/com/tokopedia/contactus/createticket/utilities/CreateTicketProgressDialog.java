package com.tokopedia.contactus.createticket.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;

import com.tokopedia.contactus.R;


public class CreateTicketProgressDialog {

    public static int NORMAL_PROGRESS = 1;
    public static int MAIN_PROGRESS = 2;
    private Context context;
    private int state;
    private int substate;
    private String msgLoading;
    private ProgressDialog progress;
    private View view;
    private Boolean isShow = false;
    private int loadingViewId;
    private Boolean isCancelable = false;

    public CreateTicketProgressDialog(Context context, int state) {
        this.context = context;
        this.state = state;
        msgLoading = "Loading...";
        substate = 0;
    }

    public void showDialog(String title, String message) {
        try {
            if (state == NORMAL_PROGRESS) {
                if (!isShow) {
                    progress = new ProgressDialog(context);
                    progress.setMessage(message);
                    progress.setTitle(title);
                    progress.setCancelable(isCancelable);
                    isShow = true;
                    progress.show();
                    isShow = true;
                }
            } else if (state == MAIN_PROGRESS && substate == 0) {
                progress = new ProgressDialog(context, R.style.CoolDialog);
                progress.show();
                progress.setContentView(R.layout.loader);
                progress.setCancelable(isCancelable);
                progress.setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ((Activity) context).finish();
                    }
                });
                isShow = true;
            } else {
                view.findViewById(loadingViewId).setVisibility(View.VISIBLE);
                isShow = true;
            }
        } catch (Exception e) {
        }
    }


    public void showDialog() {
        showDialog("", msgLoading);
    }


    public void dismiss() {
        if (state == MAIN_PROGRESS && substate == 1) {
            view.findViewById(loadingViewId).setVisibility(View.GONE);
        } else {
            if (progress != null) {
                isShow = false;
                if (progress.isShowing()) progress.dismiss();
            }
        }
    }

}