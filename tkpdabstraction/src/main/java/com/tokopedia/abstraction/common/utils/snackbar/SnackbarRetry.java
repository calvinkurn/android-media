package com.tokopedia.abstraction.common.utils.snackbar;

import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.tokopedia.abstraction.R;

/**
 * Created by ricoharisin on 6/2/16.
 */
public class SnackbarRetry {

    protected Boolean isRetryClicked;
    protected Snackbar snackBar;

    public SnackbarRetry(Snackbar snackbar, final NetworkErrorHelper.RetryClickedListener listener) {
        isRetryClicked = true;
        this.snackBar = snackbar;
        snackBar.setAction(R.string.title_try_again, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRetryClicked = true;
                listener.onRetryClicked();
            }
        });
    }

    public void resumeRetrySnackbar() {
        if (!isRetryClicked) {
            showRetrySnackbar();
        }
    }

    public void pauseRetrySnackbar() {
        hideRetrySnackbar();
    }

    public void showRetrySnackbar() {
        isRetryClicked = false;
        snackBar.show();
    }

    public void hideRetrySnackbar() {
        snackBar.dismiss();
    }

    public boolean isShown(){
        return snackBar.isShown();
    }

    public void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
        snackBar.getView().addOnAttachStateChangeListener(onAttachStateChangeListener);
    }

    public void setColorActionRetry(@ColorInt int colorActionRetry){
        snackBar.setActionTextColor(colorActionRetry);
    }
}
