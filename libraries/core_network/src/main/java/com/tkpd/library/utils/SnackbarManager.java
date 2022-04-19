package com.tkpd.library.utils;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.core.network.R;

import timber.log.Timber;

/**
 * Created by Tkpd_Eka on 12/23/2015.
 */
@Deprecated
public class SnackbarManager {

    public static Snackbar make(Activity activity, String text, @Snackbar.Duration int duration) {
        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), text, duration);
        TextView tv = (TextView) snack.getView().findViewById(R.id.snackbar_text);
        tv.setMaxLines(5);

        if (snack.isShownOrQueued())
            Timber.d("Shown %s", text);
        else
            Timber.d("Queue %s",  text);

        Button snackBarAction = (Button) snack.getView().findViewById(com.google.android.material.R.id.snackbar_action);
        if (activity != null) {
            snackBarAction.setTextColor(ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_G400));
            snack.getView().setBackgroundColor(ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
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
            Timber.d("Shown " + text);
        else
            Timber.d("Queue " + text);

        Button snackBarAction = (Button) snack.getView().findViewById(com.google.android.material.R.id.snackbar_action);
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

        return make(coordinatorLayout, text, duration, com.tokopedia.unifyprinciples.R.color.Unify_G400, com.tokopedia.unifyprinciples.R.color.Unify_N700_68);
    }

}
