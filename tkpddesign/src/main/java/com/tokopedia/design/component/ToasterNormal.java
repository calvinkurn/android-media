package com.tokopedia.design.component;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseToaster;

/**
 * @author okasurya on 1/30/18.
 */

public class ToasterNormal extends BaseToaster {

    public static Snackbar make(View view,
                                String snackbarText,
                                @BaseToaster.Duration int duration) {
        return new Builder(view, snackbarText, duration)
                .setBackgroundDrawable(R.drawable.bg_snackbar_normal)
                .setTextColor(R.color.font_black_secondary_54)
                .setActionTextColor(R.color.font_black_primary_70)
                .build();
    }

    public static void showClose(@NonNull Activity activity,
                                 String snackbarText) {
        if (activity == null) {
            return;
        }
        ToasterNormal.make(activity.findViewById(android.R.id.content),
                snackbarText, BaseToaster.LENGTH_LONG)
                .setAction(activity.getString(R.string.close), v -> {
                    // no-op
                }).show();
    }

    public static void show(@NonNull Activity activity,
                            String snackbarText,
                            String actionLabel) {
        if (activity == null) {
            return;
        }
        ToasterNormal.make(activity.findViewById(android.R.id.content),
                snackbarText, BaseToaster.LENGTH_LONG)
                .setAction(actionLabel, v -> {
                    // no-op
                }).show();
    }

    public static void show(@NonNull Activity activity,
                                 String snackbarText) {
        if (activity == null) {
            return;
        }
        ToasterNormal.make(activity.findViewById(android.R.id.content),
                snackbarText, BaseToaster.LENGTH_LONG).show();
    }
}
