package com.tokopedia.purchase_platform.features.checkout.view;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public class TypeFaceUtil {

    private static final String FONT_FAMILY_SANS_SERIF = "sans-serif";
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";

    public static void setTypefaceMedium(TextView textView) {
        textView.setTypeface(Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
    }

    public static void setTypefaceNormal(TextView textView) {
        textView.setTypeface(Typeface.create(FONT_FAMILY_SANS_SERIF, Typeface.NORMAL));
    }
}
