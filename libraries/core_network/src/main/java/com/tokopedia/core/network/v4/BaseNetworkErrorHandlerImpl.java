package com.tokopedia.core.network.v4;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.tokopedia.core.network.BaseNetworkErrorHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;

/**
 * Created by normansyahputa on 1/19/17.
 */

@Deprecated
public class BaseNetworkErrorHandlerImpl extends BaseNetworkErrorHandler {
    public BaseNetworkErrorHandlerImpl(NetworkErrorHelper.RetryClickedListener listener) {
        super(listener);
    }

    @Override
    public void showDialog(Context context) {
        NetworkErrorHelper.showDialog(context, listener);
    }

    @Override
    public SnackbarRetry createSnackbarWithAction(Activity activity) {
        return NetworkErrorHelper.createSnackbarWithAction(activity, listener);
    }

    @Override
    public SnackbarRetry createSnackbarWithAction(Activity activity, String message) {
        return NetworkErrorHelper.createSnackbarWithAction(activity,message,listener);
    }

    @Override
    public void showSnackbar(Activity activity) {
        NetworkErrorHelper.showSnackbar(activity);
    }

    @Override
    public void showSnackbar(Activity activity, String error) {
        NetworkErrorHelper.showSnackbar(activity,error);
    }

    @Override
    public void showEmptyState(Context context, View rootview) {
        NetworkErrorHelper.showEmptyState(context, rootview, listener);
    }

    @Override
    public void showDialogCustomMSG(Context context, String message) {
        NetworkErrorHelper.showDialogCustomMSG(context, listener, message);
    }
}
