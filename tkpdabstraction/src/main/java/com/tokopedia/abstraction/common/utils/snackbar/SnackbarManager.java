package com.tokopedia.abstraction.common.utils.snackbar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.common.utils.CommonUtils;

/**
 * Created by Tkpd_Eka on 12/23/2015.
 */
public class SnackbarManager {

    public static Snackbar make(
            View view,
            String text,
            @Snackbar.Duration int duration,
            @ColorRes int textColorRes,
            @ColorRes int actionColorRes,
            @ColorRes int backGroundColor,
            @DrawableRes int backGroundDrawableRes
    ) {
        Context context = view.getContext();
        Snackbar snack = Snackbar.make(view, text, duration);

        if (snack.isShownOrQueued())
            CommonUtils.dumper("Shown " + text);
        else
            CommonUtils.dumper("Queue " + text);

        setViewForText(context, snack, textColorRes);
        setViewForAction(context, snack, actionColorRes);
        setViewForBackground(context, snack, backGroundColor, backGroundDrawableRes);

        return snack;
    }

    public static Snackbar make(
            View view,
            String text,
            @Snackbar.Duration int duration,
            @ColorRes int actionColorRes,
            @ColorRes int backGroundColor
    ) {
        return make(view, text, duration, R.color.white, actionColorRes, backGroundColor, 0);
    }

    public static View getContentView (Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    private static void setViewForText(Context context, Snackbar snackbar, @ColorRes int colorRes){
        TextView textView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, colorRes));
        textView.setMaxLines(5);
    }

    private static void setViewForAction(Context context, Snackbar snackbar, @ColorRes int colorRes){
        Button snackBarAction = (Button) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(context, colorRes));
        snackBarAction.setAllCaps(false);
    }

    private static void setViewForBackground(Context context, Snackbar snackbar, @ColorRes int colorRes, @DrawableRes int backGroundDrawableRes){
        if (backGroundDrawableRes!= 0) {
            snackbar.getView().setBackground(ContextCompat.getDrawable(context, backGroundDrawableRes));
        } else {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, colorRes));
        }
    }

    public static Snackbar make(
            View view,
            String text,
            @Snackbar.Duration int duration) {

        return make(view, text, duration,
                R.color.tkpd_main_green, R.color.font_black_primary_70);
    }

    public static Snackbar make(
            Activity activity,
            String text,
            @Snackbar.Duration int duration) {
        return make(getContentView(activity), text, duration);
    }

    public static Snackbar makeRed(
            View view,
            String text,
            @Snackbar.Duration int duration) {
        return make(view, text, duration, R.color.red_500, R.color.font_black_primary_70, R.color.red_50, R.drawable.bg_red_snackbar);
    }

    public static Snackbar makeRed(
            Activity activity,
            String text,
            @Snackbar.Duration int duration) {
        return makeRed(getContentView(activity), text, duration);
    }

    public static Snackbar makeGreen(
            View view,
            String text,
            @Snackbar.Duration int duration) {
        return make(view, text, duration, R.color.font_black_disabled_38, R.color.green_500, R.color.light_green, R.drawable.bg_green_snackbar);
    }

}
