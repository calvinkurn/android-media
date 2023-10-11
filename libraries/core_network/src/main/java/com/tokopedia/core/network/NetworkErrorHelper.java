package com.tokopedia.core.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.unifycomponents.Toaster;

/**
 * Created by ricoharisin on 5/30/16.
 */

@Deprecated
public class NetworkErrorHelper {

    public static SnackbarRetry createSnackbarWithAction(
            Activity activity, String message, final RetryClickedListener listener) {
        return createSnackbarWithAction(activity, message, Snackbar.LENGTH_INDEFINITE, listener);
    }

    public static SnackbarRetry createSnackbarWithAction(
            Activity activity, String message, int duration, final RetryClickedListener listener) {
        return createSnackbarWithAction(activity, message, duration, activity.getString(com.tokopedia.abstraction.R.string.title_try_again), listener);
    }

    public static SnackbarRetry createSnackbarWithAction(
            Activity activity, String message, int duration, String actionText, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.make(activity, message, duration), actionText, listener);
    }

    public static void showSnackbar(Activity activity) {
        if (activity != null) {
            Toaster.INSTANCE.make(activity.findViewById(android.R.id.content),
                    activity.getResources().getString(com.tokopedia.abstraction.R.string.msg_network_error),
                    Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, "", v->{});
        }
    }

    public static void showSnackbar(Activity activity, String error) {
        try {
            if (activity != null) {
                if (error != null && !error.isEmpty()) {
                    SnackbarManager.make(activity,
                            error,
                            Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    showSnackbar(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface RetryClickedListener {
        void onRetryClicked();
    }
}