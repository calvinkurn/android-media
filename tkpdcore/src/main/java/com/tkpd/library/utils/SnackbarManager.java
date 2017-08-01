package com.tkpd.library.utils;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.tkpd.library.kirisame.Kirisame;
import com.tokopedia.core.R;

/**
 * Created by Tkpd_Eka on 12/23/2015.
 */
public class SnackbarManager {

    @ColorRes
    private static final int DEFAULT_BG_COLOR = R.color.black_seventy_percent_;

    public static Snackbar make(Activity activity, String text, @Snackbar.Duration int duration) {
        return makeSnackbar(activity, text, duration, DEFAULT_BG_COLOR);
    }

    public static Snackbar make(Activity activity, String text, @Snackbar.Duration int duration, int backgroundColor) {
        return makeSnackbar(activity, text, duration, backgroundColor);
    }

    private static Snackbar makeSnackbar(Activity activity, String text, @Snackbar.Duration int duration, int backgroundColorRes) {
        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), text, duration);
        TextView tv = (TextView) snack.getView().findViewById(R.id.snackbar_text);
        tv.setMaxLines(5);

        if (snack.isShownOrQueued())
            Kirisame.print("Shown " + text);
        else
            Kirisame.print("Queue " + text);

        Button snackBarAction = (Button) snack.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(activity, R.color.tkpd_main_green));

        if (backgroundColorRes != 0) {
            snack.getView().setBackgroundColor(ContextCompat.getColor(activity, backgroundColorRes));
        }
        return snack;
    }

}
