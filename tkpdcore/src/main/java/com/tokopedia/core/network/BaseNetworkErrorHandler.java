package com.tokopedia.core.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;

/**
 * Created by normansyahputa on 1/19/17.
 */

public abstract class BaseNetworkErrorHandler {

    protected NetworkErrorHelper.RetryClickedListener listener;

    public BaseNetworkErrorHandler(NetworkErrorHelper.RetryClickedListener listener){
        this.listener = listener;
    }

    protected abstract void showDialog(Context context);

    protected abstract SnackbarRetry createSnackbarWithAction(Activity activity);

    protected abstract SnackbarRetry createSnackbarWithAction(Activity activity, String message);

    protected abstract void showSnackbar(Activity activity);

    protected abstract void showSnackbar(Activity activity, String error);

    protected abstract void showEmptyState(Context context, final View rootview);

    protected abstract void showDialogCustomMSG(Context context, String message);
}
