package com.tokopedia.core.network;

import android.view.View;

import androidx.annotation.ColorInt;

import com.google.android.material.snackbar.Snackbar;

/**
 * Created by ricoharisin on 6/2/16.
 */
@Deprecated
public class SnackbarRetry {

    private Snackbar snackBar;

    public SnackbarRetry(Snackbar snackbar, String actionText, final NetworkErrorHelper.RetryClickedListener listener) {
        this.snackBar = snackbar;
        snackBar.setAction(actionText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRetryClicked();
            }
        });
    }

    public void showRetrySnackbar() {
        snackBar.show();
    }

    public void hideRetrySnackbar() {
        snackBar.dismiss();
    }

    public boolean isShown(){
        return snackBar.isShown();
    }

    public void setColorActionRetry(@ColorInt int colorActionRetry){
        snackBar.setActionTextColor(colorActionRetry);
    }
}
