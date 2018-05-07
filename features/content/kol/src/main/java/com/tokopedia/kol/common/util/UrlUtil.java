package com.tokopedia.kol.common.util;

import android.os.Build;
import android.text.util.Linkify;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * @author by milhamj on 07/05/18.
 */

public class UrlUtil {
    private static final Pattern pattern = Pattern.compile("((https?|ftp|file)://)?(www.)?(tokopedia.com|tkp.me)([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])?");
    private static final String[] SCHEMES = {"http://", "http://"};

    public static void convertToClickableUrl(TextView textView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Linkify.addLinks(textView, pattern, SCHEMES[0], SCHEMES, null, null);
        } else {
            Linkify.addLinks(textView, pattern, SCHEMES[0]);
        }
    }
}
