package com.tokopedia.design.snackbar;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * @author okasurya on 1/30/18.
 */

public class SnackbarError extends BaseSnackbar {
    public static Snackbar make(View view,
                                String snackbarText,
                                String actionText,
                                @Snackbar.Duration int duration,
                                View.OnClickListener actionListener) {
        return makeBase(
                view, snackbarText, actionText, duration,
                R.color.light_red, R.color.font_black_secondary_54, R.color.font_black_primary_70,
                actionListener
        );
    }
}
