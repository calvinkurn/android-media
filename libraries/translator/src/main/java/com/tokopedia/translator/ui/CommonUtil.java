package com.tokopedia.translator.ui;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

public class CommonUtil {
    public static void showToastWithUIUpdate(Context context, String message, TextView viewRequireUpdate, String updatedText) {
        if (context == null || message == null) {
            return;
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        if (viewRequireUpdate != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                viewRequireUpdate.setText(Html.fromHtml(updatedText, Html.FROM_HTML_MODE_COMPACT));
            } else {
                viewRequireUpdate.setText(Html.fromHtml(updatedText));
            }
        }
    }


    public static void showToast(Context context, String message) {
        if (context == null || message == null) {
            return;
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setText(TextView viewRequireUpdate, String updatedText) {
        if (viewRequireUpdate != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                viewRequireUpdate.setText(Html.fromHtml(updatedText, Html.FROM_HTML_MODE_COMPACT));
            } else {
                viewRequireUpdate.setText(Html.fromHtml(updatedText));
            }
        }
    }
}
