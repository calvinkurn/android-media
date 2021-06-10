package com.tokopedia.abstraction.common.utils.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by nisie on 10/28/16.
 */

public class MethodChecker {


    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static int getColor(Context context, int id) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ContextCompat.getColor(context, id);
            } else {
                return context.getResources().getColor(id);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static Uri getUri(Context context, File outputMediaFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider", outputMediaFile);
        } else {
            return Uri.fromFile(outputMediaFile);
        }
    }

    public static Spanned fromHtml(String text) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableStringBuilder("");
        }
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(text);
        }
        return result;
    }

    public static Spanned fromHtmlPreserveLineBreak(String text) {
        String lineBreakHtmlResult = text.replace("\n", "<br />");
        return fromHtml(lineBreakHtmlResult);
    }

    public static CharSequence fromHtmlWithoutExtraSpace(String text) {
        return trimTrailingWhitespace(fromHtml(text));
    }

    public static CharSequence trimTrailingWhitespace(CharSequence source) {
        if (source == null) return "";
        int i = source.length();
        do {
            --i;
        } while (i >= 0 && Character.isWhitespace(source.charAt(i)));
        return source.subSequence(0, i + 1);
    }

    public static void setAllowMixedContent(WebSettings webSettings) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    public static Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            return context.getResources().getDrawable(resId, context.getApplicationContext().getTheme());
        else
            return AppCompatResources.getDrawable(context, resId);
    }

    public static boolean isTimezoneNotAutomatic(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return android.provider.Settings.Global.getInt(
                    context.getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME, 0) == 0;
        } else {
            return android.provider.Settings.System.getInt(
                    context.getContentResolver(),
                    android.provider.Settings.System.AUTO_TIME, 0) == 0;
        }
    }
}