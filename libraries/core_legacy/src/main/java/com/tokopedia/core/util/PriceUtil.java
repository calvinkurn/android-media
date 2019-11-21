package com.tokopedia.core.util;

import android.text.TextUtils;

import com.tokopedia.design.utils.CurrencyFormatHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceUtil {
    public static String from(String price){
        price = CurrencyFormatHelper.removeCurrencyPrefix(price);
        if (!TextUtils.isEmpty(price)) {
            try {
                long result = new DecimalFormat("#,#", new DecimalFormatSymbols(new Locale("in", "ID"))).parse(price).longValue();
                return Long.valueOf(result).toString();
            }catch (ParseException pe){
                return "0";
            }
        } else {
            return "0";
        }
    }
}
