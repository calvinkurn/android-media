package com.tokopedia.kol.common.util;

import android.text.util.Linkify;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * @author by milhamj on 07/05/18.
 */

public class UrlUtil {
    private static final Pattern pattern = Pattern.compile("((https?|ftp|file)://)?(www.)?(tokopedia.com|tkp.me)([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])?");

    public static void convertToClickableUrl(TextView textView) {
        Linkify.addLinks(textView, pattern, "");
    }
}
