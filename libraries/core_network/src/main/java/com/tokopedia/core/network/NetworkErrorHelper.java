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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.network.R;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;

/**
 * Created by ricoharisin on 5/30/16.
 */

@Deprecated
public class NetworkErrorHelper {

    public static void showDialog(Context context, final RetryClickedListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        String noConnection = context.getResources().getString(R.string.msg_no_connection) + ".\n"
                + context.getResources().getString(R.string.error_no_connection2) + ".";
        msg.setText(noConnection);
        dialog.setView(promptsView);
        if (listener != null) {
            dialog.setPositiveButton(context.getString(R.string.title_try_again),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onRetryClicked();
                            dialog.dismiss();
                        }
                    });
        } else {
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        Dialog finalDialog = dialog.create();
        finalDialog.setCanceledOnTouchOutside(false);
        finalDialog.show();
    }

    public static SnackbarRetry createSnackbarWithAction(CoordinatorLayout coordinatorLayout, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.make(coordinatorLayout,
                coordinatorLayout.getContext().getResources().getString(R.string.msg_network_error), Snackbar.LENGTH_INDEFINITE), listener);
    }

    public static SnackbarRetry createSnackbarWithAction(
            CoordinatorLayout coordinatorLayout, String message, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE), listener);
    }

    public static SnackbarRetry createSnackbarWithAction(
            CoordinatorLayout coordinatorLayout, String message, int duration, final RetryClickedListener listener) {
        return createSnackbarWithAction(coordinatorLayout, message, duration,
                coordinatorLayout.getContext().getString(R.string.title_try_again), listener);
    }

    public static SnackbarRetry createSnackbarWithAction(
            CoordinatorLayout coordinatorLayout, String message, int duration, String actionText, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.make(coordinatorLayout, message, duration), actionText, listener);
    }

    public static SnackbarRetry createSnackbarWithAction(Activity activity, final RetryClickedListener listener) {
        String message = "";
        if (activity != null) {
            message = activity.getResources().getString(R.string.msg_network_error);
        }
        return createSnackbarWithAction(activity, message, listener);
    }

    public static SnackbarRetry createSnackbarWithAction(
            Activity activity, String message, final RetryClickedListener listener) {
        return createSnackbarWithAction(activity, message, Snackbar.LENGTH_INDEFINITE, listener);
    }

    public static SnackbarRetry createSnackbarWithAction(
            Activity activity, String message, int duration, final RetryClickedListener listener) {
        return createSnackbarWithAction(activity, message, duration, activity.getString(R.string.title_try_again), listener);
    }

    public static SnackbarRetry createSnackbarWithAction(
            Activity activity, String message, int duration, String actionText, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.make(activity, message, duration), actionText, listener);
    }

    public static void showCloseSnackbar(Activity activity, String message) {
        SnackbarManager.make(activity, message, Snackbar.LENGTH_LONG).setAction(
                activity.getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // no operation
                    }
                }
        ).show();
    }

    public static void showSnackbar(Activity activity) {
        if (activity != null) {
            ToasterError.make(BaseToaster.getContentView(activity),
                    activity.getResources().getString(R.string.msg_network_error),
                    BaseToaster.LENGTH_SHORT).show();
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

    public static void showEmptyState(Context context, final View rootview, final RetryClickedListener listener) {
        try {
            rootview.findViewById(R.id.main_retry).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            params.weight = 1.0f;
            View retryLoad = inflater.inflate(R.layout.item_base_network_error, (ViewGroup) rootview);
            View retryButon = retryLoad.findViewById(R.id.retry_but);
            if (listener != null) {
                retryButon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        rootview.findViewById(R.id.main_retry).setVisibility(View.GONE);
                        listener.onRetryClicked();
                    }
                });
            } else {
                retryButon.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("NewApi")
    public static void showEmptyState(Context context, final View rootview,
                                      @Nullable String titleMessage,
                                      @Nullable String subTitleMessage,
                                      @Nullable String titleRetry,
                                      @DrawableRes int iconRes,
                                      @Nullable final RetryClickedListener listener) {
        try {
            rootview.findViewById(R.id.main_retry).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.gravity = Gravity.CENTER;
            params.weight = 1.0f;
            View retryLoad = inflater.inflate(R.layout.item_base_network_error, (ViewGroup) rootview);
            TextView retryButon = (TextView) retryLoad.findViewById(R.id.retry_but);
            TextView tvTitleMessage = (TextView) retryLoad.findViewById(R.id.retry_text);
            TextView tvSubTitleMessage = (TextView) retryLoad.findViewById(R.id.retry_description);
            ImageView ivIcon = (ImageView) retryLoad.findViewById(R.id.image_error);
            if (subTitleMessage != null) tvSubTitleMessage.setText(subTitleMessage);
            if (titleMessage != null) tvTitleMessage.setText(titleMessage);
            if (titleRetry != null) retryButon.setText(titleRetry);
            if (iconRes != 0) {
                //noinspection deprecation
                ivIcon.setImageDrawable(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                        context.getDrawable(iconRes) : context.getResources().getDrawable(iconRes));
            }
            if (listener != null) {
                retryButon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        rootview.findViewById(R.id.main_retry).setVisibility(View.GONE);
                        listener.onRetryClicked();
                    }
                });
            }
        }
    }

    public static void removeEmptyState(View rootview) {
        try {
            rootview.findViewById(R.id.main_retry).setVisibility(View.GONE);
        } catch (NullPointerException e) {
        }
    }

    public static void showEmptyState(Context context, final View rootview, String message, final RetryClickedListener listener) {
        try {
            rootview.findViewById(R.id.main_retry).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            params.weight = 1.0f;
            View retryLoad = inflater.inflate(R.layout.item_base_network_error, (ViewGroup) rootview);
            View retryButon = retryLoad.findViewById(R.id.retry_but);
            TextView msgRetry = (TextView) retryLoad.findViewById(R.id.retry_text);
            if (message != null) {
                msgRetry.setText(message);
            }
            if (listener != null) {
                retryButon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        rootview.findViewById(R.id.main_retry).setVisibility(View.GONE);
                        listener.onRetryClicked();
                    }
                });
            } else {
                retryButon.setVisibility(View.GONE);
            }
        }

    }

    public static void hideEmptyState(final View rootview) {
        try {
            rootview.findViewById(R.id.main_retry).setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public static void showDialogCustomMSG(Context context, final RetryClickedListener listener, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        msg.setText(message);
        dialog.setView(promptsView);
        if (listener != null) {
            dialog.setPositiveButton(context.getString(R.string.title_try_again),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onRetryClicked();
                            dialog.dismiss();
                        }
                    });
        } else {
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog.create().show();
    }

    public interface RetryClickedListener {
        void onRetryClicked();
    }
}