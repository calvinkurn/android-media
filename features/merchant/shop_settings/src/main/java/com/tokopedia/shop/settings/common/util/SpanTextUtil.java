package com.tokopedia.shop.settings.common.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

/**
 * Created by hendry on 16/08/18.
 */
public class SpanTextUtil {
    public static CharSequence getSpandableColorText(String strToPut, String stringToBold, ForegroundColorSpan color) {
        int indexStartBold = -1;
        int indexEndBold = -1;
        if (TextUtils.isEmpty(stringToBold)) {
            return strToPut;
        }
        String strToPutLowerCase = strToPut.toLowerCase();
        String strToBoldLowerCase = stringToBold.toLowerCase();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(strToPut);
        indexStartBold = strToPutLowerCase.indexOf(strToBoldLowerCase);
        if (indexStartBold != -1) {
            indexEndBold = indexStartBold + stringToBold.length();
        }
        if (indexStartBold == -1) {
            return spannableStringBuilder;
        } else {
            spannableStringBuilder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    indexStartBold, indexEndBold, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(color, indexStartBold, indexEndBold,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableStringBuilder;
        }
    }
}
