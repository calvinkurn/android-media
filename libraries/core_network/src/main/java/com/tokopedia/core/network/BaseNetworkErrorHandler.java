package com.tokopedia.core.network;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by normansyahputa on 1/19/17.
 */

@Deprecated
public abstract class BaseNetworkErrorHandler {

    protected NetworkErrorHelper.RetryClickedListener listener;

    public BaseNetworkErrorHandler(NetworkErrorHelper.RetryClickedListener listener){
        this.listener = listener;
    }

    public abstract void showDialog(Context context);

    public abstract SnackbarRetry createSnackbarWithAction(Activity activity);

    public abstract SnackbarRetry createSnackbarWithAction(Activity activity, String message);

    public abstract void showSnackbar(Activity activity);

    public abstract void showSnackbar(Activity activity, String error);

    public abstract void showEmptyState(Context context, final View rootview);

    public abstract void showDialogCustomMSG(Context context, String message);

    public void setListener(NetworkErrorHelper.RetryClickedListener listener) {
        this.listener = listener;
    }
}
