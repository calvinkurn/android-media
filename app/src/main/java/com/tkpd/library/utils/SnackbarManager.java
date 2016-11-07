package com.tkpd.library.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import com.tokopedia.tkpd.R;

import nope.yuuji.kirisame.Kirisame;

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
        return snack;
    }

}
