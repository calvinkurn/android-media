package com.tokopedia.ovo.view;

import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    public static Locale locale = new Locale("in", "ID");
    public static final String RUPIAH_FORMAT = "Rp %s";

    public static String convertToCurrencyString(long value) {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value));
    }

    public static String convertToCurrencyStringWithoutRp(long value) {
        return NumberFormat.getNumberInstance(locale).format(value);
    }

    public static long convertToCurrencyLongFromString(String currency) {

        currency = currency.replace(".", "");
        return Long.valueOf(currency);

    }
}
