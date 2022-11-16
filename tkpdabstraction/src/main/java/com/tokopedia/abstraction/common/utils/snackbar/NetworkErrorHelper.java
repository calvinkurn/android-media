package com.tokopedia.abstraction.common.utils.snackbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * Created by ricoharisin on 5/30/16.
 */
public class NetworkErrorHelper {

    @SuppressWarnings("Range")
    public static SnackbarRetry createSnackbarWithAction(CoordinatorLayout coordinatorLayout, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.make(coordinatorLayout,
                coordinatorLayout.getContext().getResources().getString(R.string.msg_network_error),
                Snackbar.LENGTH_INDEFINITE), listener);
    }

    @SuppressWarnings("Range")
    public static SnackbarRetry createSnackbarWithAction(Activity activity, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.make(activity,
                activity.getResources().getString(R.string.msg_network_error),
                Snackbar.LENGTH_INDEFINITE), listener);
    }

    @SuppressWarnings("Range")
    public static SnackbarRetry createSnackbarWithAction(Activity activity, String message, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.make(activity,
                message,
                Snackbar.LENGTH_INDEFINITE), listener);
    }

    @SuppressWarnings("Range")
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

    @SuppressWarnings("Range")
    public static void showGreenCloseSnackbar(Activity activity, String message) {
        if (activity != null) {
            showGreenCloseSnackbar(SnackbarManager.getContentView(activity), message);
        }
    }

    @SuppressWarnings("Range")
    public static void showGreenCloseSnackbar(View view, String message) {
        SnackbarManager.makeGreen(view,
                message,
                Snackbar.LENGTH_LONG)
                .setAction(view.getContext().getString(R.string.close), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }
                ).show();
    }

    @SuppressWarnings("Range")
    public static void showRedCloseSnackbar(Activity activity, String message) {
        if (activity != null) {
            showRedCloseSnackbar(SnackbarManager.getContentView(activity), message);
        }
    }

    /**
     * use ToasterError instead
     */
    @Deprecated
    @SuppressWarnings("Range")
    public static void showRedCloseSnackbar(View view, String message) {
        SnackbarManager.makeRed(view, message, Snackbar.LENGTH_LONG).setAction(
                view.getContext().getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // no operation
                    }
                }
        ).show();
    }


    public static void showRedSnackbar(Activity activity, String message) {
        SnackbarManager.makeRed(SnackbarManager.getContentView(activity), message, Snackbar.LENGTH_LONG).show();
    }

    public static void showRedSnackbar(View view, String message) {
        SnackbarManager.makeRed(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showGreenSnackbar(Activity activity, String message) {
        SnackbarManager.makeGreen(SnackbarManager.getContentView(activity), message, Snackbar.LENGTH_LONG).show();
    }

    public static void showGreenSnackbarShort(Activity activity, String message) {
        SnackbarManager.makeGreen(SnackbarManager.getContentView(activity), message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showGreenSnackbar(View view, String message) {
        SnackbarManager.makeGreen(view, message, Snackbar.LENGTH_LONG).show();
    }

    @SuppressWarnings("Range")
    public static void showSnackbar(Activity activity) {
        if (activity != null) {
            SnackbarManager.make(activity,
                    activity.getResources().getString(R.string.msg_network_error),
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @SuppressWarnings("Range")
    public static void showSnackbar(Activity activity, String error) {
        if (activity != null) {
            SnackbarManager.make(activity,
                    error,
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    public static void showEmptyState(Context context, final View rootview, final RetryClickedListener listener) {
        if (context == null) {
            return;
        }

        try {
            rootview.findViewById(R.id.main_retry).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            params.weight = 1.0f;
            View retryLoad = inflater.inflate(R.layout.partial_empty_page_error, (ViewGroup) rootview);
            View retryButon = retryLoad.findViewById(R.id.button_retry);
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
            View retryLoad = inflater.inflate(R.layout.partial_empty_page_error, (ViewGroup) rootview);
            TextView retryButon = (TextView) retryLoad.findViewById(R.id.button_retry);
            TextView tvTitleMessage = (TextView) retryLoad.findViewById(R.id.message_retry);
            TextView tvSubTitleMessage = (TextView) retryLoad.findViewById(R.id.sub_message_retry);
            ImageView ivIcon = (ImageView) retryLoad.findViewById(R.id.iv_icon);
            if (subTitleMessage != null) tvSubTitleMessage.setText(subTitleMessage);
            if (titleMessage != null) tvTitleMessage.setText(titleMessage);
            if (titleRetry != null) retryButon.setText(titleRetry);
            if (iconRes != 0) {
                //noinspection deprecation
                ivIcon.setImageDrawable(MethodChecker.getDrawable(ivIcon.getContext(),iconRes));
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
            if (context != null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                params.weight = 1.0f;
                if (inflater != null) {
                    View retryLoad = inflater.inflate(R.layout.partial_empty_page_error, (ViewGroup) rootview);
                    View retryButon = retryLoad.findViewById(R.id.button_retry);
                    TextView msgRetry = (TextView) retryLoad.findViewById(R.id.message_retry);
                    msgRetry.setText(message);
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
        }

    }

    public static void hideEmptyState(final View rootview) {
        try {
            rootview.findViewById(R.id.main_retry).setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public static SnackbarRetry createSnackbarRedWithAction(Activity activity, String message, final RetryClickedListener listener) {
        return new SnackbarRetry(SnackbarManager.makeRed(activity,
                message,
                Snackbar.LENGTH_INDEFINITE), listener);
    }

    public interface RetryClickedListener {
        void onRetryClicked();
    }

}
