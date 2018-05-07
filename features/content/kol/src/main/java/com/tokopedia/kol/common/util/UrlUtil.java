package com.tokopedia.kol.common.util;

import android.os.Build;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;

import java.util.regex.Pattern;

/**
 * @author by milhamj on 07/05/18.
 */

public class UrlUtil {
    private static final Pattern pattern = Pattern.compile("((https?)://)?(www.)?(tokopedia.com|tkp.me)([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])?");
    private static final String[] SCHEMES = {"http://", "https://"};

    public static void setTextWithClickableTokopediaUrl(TextView textView, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(MethodChecker.fromHtml(text));
            Linkify.addLinks(textView, pattern, SCHEMES[0], SCHEMES, null, null);
        } else {
            if (!TextUtils.isEmpty(text)) {
                text = text.replace(SCHEMES[1], SCHEMES[0]);
                textView.setText(MethodChecker.fromHtml(text));
                Linkify.addLinks(textView, pattern, SCHEMES[0]);
            }
        }
    }
}
