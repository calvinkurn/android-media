package com.tokopedia.tokocash.autosweepmf.view.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Utility class for restricting the EditText input to certain limit/boundaries
 */
public class InputFilterMinMax implements InputFilter {
    private int min;
    private int max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length()));
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) {
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
