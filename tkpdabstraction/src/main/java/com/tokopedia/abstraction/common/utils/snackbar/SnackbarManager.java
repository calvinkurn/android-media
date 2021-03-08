package com.tokopedia.abstraction.common.utils.snackbar;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.R;

import timber.log.Timber;

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
            Timber.d("Shown " + text);
        else
            Timber.d("Queue " + text);

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
        return make(view, text, duration, R.color.Unify_N0, actionColorRes, backGroundColor, 0);
    }

    public static View getContentView (Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    private static void setViewForText(Context context, Snackbar snackbar, @ColorRes int colorRes){
        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, colorRes));
        textView.setMaxLines(5);
    }

    private static void setViewForAction(Context context, Snackbar snackbar, @ColorRes int colorRes){
        Button snackBarAction = (Button) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action);
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
                com.tokopedia.unifyprinciples.R.color.Unify_G400, com.tokopedia.unifyprinciples.R.color.Unify_N700_68);
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
        return make(view, text, duration, com.tokopedia.unifyprinciples.R.color.Unify_R600, com.tokopedia.unifyprinciples.R.color.Unify_N700, com.tokopedia.unifyprinciples.R.color.Unify_R100, R.drawable.bg_red_snackbar);
    }

    /**
     * use ToasterError instead
     */
    @Deprecated
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
        return make(view,
                text,
                duration,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_32, com.tokopedia.unifyprinciples.R.color.Unify_G500, com.tokopedia.unifyprinciples.R.color.Unify_G100,
                R.drawable.bg_green_snackbar);
    }

}
