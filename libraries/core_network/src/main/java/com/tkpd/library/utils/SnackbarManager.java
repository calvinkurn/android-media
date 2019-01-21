package com.tkpd.library.utils;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.tkpd.library.kirisame.Kirisame;
import com.tokopedia.core.util.GeneralUtils;
import com.tokopedia.core.network.R;

/**
 * Created by Tkpd_Eka on 12/23/2015.
 */
public class SnackbarManager {

    public static Snackbar make(Activity activity, String text, @Snackbar.Duration int duration) {
        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), text, duration);
        TextView tv = (TextView) snack.getView().findViewById(R.id.snackbar_text);
        tv.setMaxLines(5);

        if (snack.isShownOrQueued())
            Kirisame.print("Shown " + text);
        else
            Kirisame.print("Queue " + text);

        Button snackBarAction = (Button) snack.getView().findViewById(android.support.design.R.id.snackbar_action);
        if (activity != null) {
            snackBarAction.setTextColor(ContextCompat.getColor(activity, R.color.tkpd_main_green));
            snack.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.black_seventy_percent_));
        }
        return snack;
    }

    public static Snackbar make(
            CoordinatorLayout coordinatorLayout,
            String text,
            @Snackbar.Duration int duration,
            @ColorRes int actionColorRes,
            @ColorRes int backGroundColor
    ) {
        Snackbar snack = Snackbar.make(coordinatorLayout, text, duration);
        TextView tv = (TextView) snack.getView().findViewById(R.id.snackbar_text);
        tv.setMaxLines(5);

        if (snack.isShownOrQueued())
            GeneralUtils.dumper("Shown " + text);
        else
            GeneralUtils.dumper("Queue " + text);

        Button snackBarAction = (Button) snack.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(coordinatorLayout.getContext(), actionColorRes));
        snackBarAction.setAllCaps(false);

        snack.getView().setBackgroundColor(ContextCompat.getColor(coordinatorLayout.getContext(), backGroundColor));

        return snack;
    }

    public static Snackbar make(
            CoordinatorLayout coordinatorLayout,
            String text,
            @Snackbar.Duration int duration
    ) {

        return make(coordinatorLayout, text, duration, R.color.tkpd_main_green, R.color.black_seventy_percent_);
    }

}
