package com.tokopedia.abstraction.common.utils.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.telephony.SmsMessage;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;

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


    public static void removeAllCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    CommonUtils.dumper("Success Clear Cookie");
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider", outputMediaFile);
        } else {
            return Uri.fromFile(outputMediaFile);
        }
    }

    public static Spanned fromHtml(String text) {
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

    public static SmsMessage createSmsFromPdu(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            return msgs[0];
        } else {
            final Object[] pdusObj = (Object[]) intent.getExtras().get("pdus");

            return SmsMessage.createFromPdu((byte[]) (pdusObj != null ? pdusObj[0] : ""));
        }
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

    public static Intent getSmsIntent(Activity activity, String shareText) {
        Intent smsIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(activity);
            smsIntent = new Intent(Intent.ACTION_SEND);
            smsIntent.setType("text/plain");
            smsIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            if (defaultSmsPackageName != null) {
                smsIntent.setPackage(defaultSmsPackageName);
            }

        } else {
            smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("sms_body", shareText);
        }
        return smsIntent;
    }

    public static void loadImageFitCenter(ImageView imageView, String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageFitCenter(imageView.getContext(), imageView, url);
        } else {
            Glide.with(imageView.getContext())
                    .load(url)
                    .fitCenter()
                    .into(imageView);
        }
    }
}