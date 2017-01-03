package com.tokopedia.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.telephony.SmsMessage;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import com.tkpd.library.utils.CommonUtils;

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

    public static void setBackgroundTintList(View view, ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setBackgroundTintList(tint);
        } else {
            ViewCompat.setBackgroundTintList(view, tint);
        }
    }

    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }


    public static void removeAllCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    CommonUtils.dumper("Success Clear Cookie");
                }
            });
        } else
            CookieManager.getInstance().removeAllCookie();
    }

    public static Uri getUri(Context context, File outputMediaFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", outputMediaFile);
        } else {
            return Uri.fromFile(outputMediaFile);
        }
    }

    public static Spanned fromHtml(String text) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(text);
        }
        return result;
    }

    public static SmsMessage createSmsFromPdu(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            return msgs[0];
        } else {
            final Object[] pdusObj = (Object[]) intent.getExtras().get("pdus");

            return SmsMessage.createFromPdu((byte[]) (pdusObj != null ? pdusObj[0] : ""));
        }
    }
}