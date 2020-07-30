package com.tokopedia.core.util;

import android.text.TextUtils;

import com.tokopedia.design.utils.CurrencyFormatHelper;

public class PriceUtil {
    public static String from(String price){
        price = CurrencyFormatHelper.removeCurrencyPrefix(price);
        if (!TextUtils.isEmpty(price)) {
            long result = Double.valueOf(price).longValue();
            return Long.valueOf(result).toString();
        } else {
            return "0";
        }
    }
}
