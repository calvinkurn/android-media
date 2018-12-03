package com.tokopedia.core.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.router.InboxRouter;

/**
 * Created by Tkpd_Eka on 4/15/2015.
 */
public class VersionInfo {

    public static void createVersionDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        setView(builder, inflater, context);
        setListener(builder, context);
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private static void setView(AlertDialog.Builder builder, LayoutInflater inflater, Context context) {
        View view = inflater.inflate(R.layout.dialog_about_apps, null);
        TextView version = (TextView) view.findViewById(R.id.title_version);
        setViewTermPrivacy(view, context);
        version.setText(version.getText() + " " + getVersionInfo(context));
        builder.setView(view);
    }

    private static void setViewTermPrivacy(View view, Context context) {
        String joinString = context.getString(R.string.link_term_condition) +
                "<br>" + context.getString(R.string.link_privacy_policy);
        TextView textTermAndPolicy = (TextView) view.findViewById(R.id.term_policy);
        textTermAndPolicy.setText(MethodChecker.fromHtml(joinString));
        textTermAndPolicy.setClickable(true);
        textTermAndPolicy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static String getVersionInfo(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Collector Edition";
        }
    }

    private static void setListener(AlertDialog.Builder builder, Context context){
        builder.setPositiveButton(R.string.custom_string_rate_us, OnRateUsListener(context));
        builder.setNegativeButton(R.string.title_share_apps, OnShareAppsListener(context));
        builder.setNeutralButton(R.string.title_activity_contact_us,onContactUsListener(context));
    }

    private static DialogInterface.OnClickListener onContactUsListener(final Context context) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = InboxRouter.getContactUsActivityIntent(context);
                context.startActivity(intent);
                dialog.dismiss();
            }
        };
    }

    private static DialogInterface.OnClickListener OnShareAppsListener(final Context context){
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String urlPlayStore = "http://play.google.com/store/apps/details?shopId=" + context.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.msg_share_apps)+"\n"+urlPlayStore);
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.title_share)));
            }
        };
    }

    private static DialogInterface.OnClickListener OnRateUsListener(final Context context){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("market://details?shopId=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    context.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?shopId=" + context.getPackageName())));
                }
                dialog.dismiss();
            }
        };
    }

    public static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        if (manufacturer.equalsIgnoreCase("HTC")) {
            return "HTC " + model;
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        final char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (final char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }


}
