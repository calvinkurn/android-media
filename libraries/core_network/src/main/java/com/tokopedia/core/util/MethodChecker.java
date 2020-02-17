package com.tokopedia.core.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.tokopedia.core.network.CoreNetworkApplication;

import java.io.File;

/**
 * Created by nisie on 10/28/16.
 */

@Deprecated
/**
 * Use from Abstraction instead.
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }


    public static void removeAllCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    GeneralUtils.dumper("Success Clear Cookie");
                }
            });
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
        }
    }

    public static Uri getUri(Context context, File outputMediaFile) {
        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider", outputMediaFile);
        } else {
            return Uri.fromFile(outputMediaFile);
        }
    }

    public static Spanned fromHtml(String text) {
        if (text == null) {
            text = "";
        }
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
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

    public static Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            return context.getResources().getDrawable(resId, context.getApplicationContext().getTheme());
        else
            return AppCompatResources.getDrawable(context, resId);
    }

    public static boolean isTimezoneNotAutomatic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return android.provider.Settings.Global.getInt(
                    CoreNetworkApplication.getAppContext().getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME, 0) == 0;
        } else {
            return android.provider.Settings.System.getInt(
                    CoreNetworkApplication.getAppContext().getContentResolver(),
                    android.provider.Settings.System.AUTO_TIME, 0) == 0;
        }
    }
}