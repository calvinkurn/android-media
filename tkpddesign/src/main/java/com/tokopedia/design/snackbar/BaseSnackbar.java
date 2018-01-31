package com.tokopedia.design.snackbar;

import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author okasurya on 1/30/18.
 */

class BaseSnackbar {
    static Snackbar make(View view,
                         String snackbarText,
                         String actionText,
                         @Snackbar.Duration int duration,
                         @ColorRes int backgroundColor,
                         @ColorRes int textColor,
                         @ColorRes int actionColor,
                         View.OnClickListener actionListener) {
        Snackbar snackbar = Snackbar.make(view, snackbarText, duration);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), backgroundColor));

        TextView textView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), textColor));
        textView.setMaxLines(5);

        Button snackbarAction = snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackbarAction.setTextColor(ContextCompat.getColor(view.getContext(), actionColor));
        snackbarAction.setAllCaps(false);
        snackbarAction.setText(actionText);

        snackbar.setAction(actionText, actionListener);

        return snackbar;
    }
}
