package com.tokopedia.design.base;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author okasurya on 1/30/18.
 * Base Snackbar builder
 */

public class BaseToaster {
    protected static class Builder {
        private View view;
        private Snackbar snackbar;
        private TextView snackbarTextView;
        private Button snackbarActionButton;

        public Builder(View view, String snackbarText, @Snackbar.Duration int duration) {
            this.view = view;
            snackbar = Snackbar.make(view, snackbarText, duration);
            snackbarTextView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            snackbarActionButton = snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);

            setDefaultSetting();
        }

        private void setDefaultSetting() {
            snackbarTextView.setMaxLines(2);
            snackbarActionButton.setAllCaps(false);
        }

        public Builder setBackgroundDrawable(@DrawableRes int backgroundDrawable) {
            snackbar.getView().setBackground(ContextCompat.getDrawable(view.getContext(), backgroundDrawable));
            return this;
        }

        public Builder setTextColor(@ColorRes int textColor) {
            snackbarTextView.setTextColor(ContextCompat.getColor(view.getContext(), textColor));
            return this;
        }

        public Builder setActionTextColor(@ColorRes int actionColor) {
            snackbarActionButton.setTextColor(ContextCompat.getColor(view.getContext(), actionColor));
            return this;
        }

        public Builder setAction(String actionText, View.OnClickListener actionListener) {
            snackbar.setAction(actionText, actionListener);
            return this;
        }

        public Snackbar build() {
            return snackbar;
        }
    }
}
