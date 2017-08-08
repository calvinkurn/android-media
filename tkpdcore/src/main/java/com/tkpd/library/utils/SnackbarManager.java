package com.tkpd.library.utils;

import android.app.Activity;
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

    public static Snackbar make(Activity activity, String text, @Snackbar.Duration int duration) {
        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), text, duration);
        TextView tv = (TextView) snack.getView().findViewById(R.id.snackbar_text);
        tv.setMaxLines(5);

        if (snack.isShownOrQueued())
            Kirisame.print("Shown " + text);
        else
            Kirisame.print("Queue " + text);

        Button snackBarAction = (Button) snack.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(activity, R.color.tkpd_main_green));

        snack.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.black_seventy_percent_));

        return snack;
    }

}
