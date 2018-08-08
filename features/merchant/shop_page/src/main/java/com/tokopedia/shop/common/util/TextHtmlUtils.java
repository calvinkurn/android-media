package com.tokopedia.shop.common.util;

import android.text.Html;

/**
 * Created by nathan on 3/4/18.
 */

public class TextHtmlUtils {

    public static CharSequence getTextFromHtml(String htmlText) {
        CharSequence charSequence;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml(htmlText);
        }
        return charSequence;
    }
}
