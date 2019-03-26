package com.tokopedia.unifycomponents;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Created by meta on 27/02/19.
 */
public class Toaster {

    private final static String RED = "RED";
    private final static String GREEN = "GREEN";

    public static void showGreen(View view, CharSequence charSequence, int duration) {
        Snackbar snackbar = buildView(view, charSequence, duration, GREEN);
        snackbar.show();
    }

    public static void showGreenWithAction(View view, CharSequence charSequence, int duration, CharSequence actionText, View.OnClickListener actionClicklistener) {
        Snackbar snackbar = buildView(view, charSequence, duration, GREEN);
        snackbar.setAction(actionText, actionClicklistener);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.Neutral_N0));
        snackbar.show();
    }

    public static void showRed(View view, CharSequence charSequence, int duration) {
        Snackbar snackbar = buildView(view, charSequence, duration, RED);
        snackbar.show();
    }

    public static void showRedWithAction(View view, CharSequence charSequence, int duration, CharSequence actionText, View.OnClickListener actionClicklistener) {
        Snackbar snackbar = buildView(view, charSequence, duration, RED);
        snackbar.setAction(actionText, actionClicklistener);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.Neutral_N0));
        snackbar.show();
    }

    private static Snackbar buildView(View view, CharSequence charSequence, int duration, String type) {
        Snackbar snackbar = Snackbar.make(view, charSequence, duration);
        View snackbarView = snackbar.getView();
        snackbarView.setPadding(0,0,0,0);
        if (type.equals(RED)) {
            snackbarView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.bg_toaster_red));
        } else if (type.equals(GREEN)) {
            snackbarView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.bg_toaster_green));
        }
        return snackbar;
    }
}
