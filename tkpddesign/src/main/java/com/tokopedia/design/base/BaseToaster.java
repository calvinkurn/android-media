package com.tokopedia.design.base;

import android.app.Activity;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author okasurya on 1/30/18.
 * Base Snackbar builder
 */

public class BaseToaster {

    public static final int LENGTH_SHORT = Snackbar.LENGTH_SHORT;
    public static final int LENGTH_LONG = Snackbar.LENGTH_LONG;
    public static final int LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE;

    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    public @interface Duration {}

    protected static class Builder {
        private View view;
        private Snackbar snackbar;
        private TextView snackbarTextView;
        private Button snackbarActionButton;

        public Builder(View view, String snackbarText, @Duration int duration) {
            this.view = view;
            snackbar = Snackbar.make(view, snackbarText, duration);
            snackbarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            snackbarActionButton = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action);

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

    public static View getContentView(Activity activity) {
        return activity.findViewById(android.R.id.content);
    }
}
