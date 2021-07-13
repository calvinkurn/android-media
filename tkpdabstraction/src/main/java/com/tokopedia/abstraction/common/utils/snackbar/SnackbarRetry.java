package com.tokopedia.abstraction.common.utils.snackbar;

import androidx.annotation.ColorInt;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
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

    public void showRetrySnackbar() {
        Log.d("ERROR_SNACK", "SHOW ERROR SNACK GET SHOW");
        isRetryClicked = false;
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
