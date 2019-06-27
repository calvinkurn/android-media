package com.tokopedia.core.util;

import com.tokopedia.abstraction.common.utils.GlobalConfig;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


@Deprecated
public class GeneralUtils {

    public static Double parsePriceToDouble(String price, String currency) {
        price = price.replace(",", "");
        if (!currency.equals("US$")) {
            // remove cent
            price = price.replace(".", "");
        }
        return Double.parseDouble(price);
    }

    public static List<String> checkNullMessageError(List<String> messageError) {
        boolean isNull = false;
        if (messageError == null && messageError.size() <= 0)
            return new ArrayList<String>();

        for (String msg : messageError) {
            if (!checkNullForZeroJson(msg))
                return new ArrayList<String>();
        }
        return messageError;
    }

    public static boolean checkNullForZeroJson(String input) {
        if (input == null || input.equals("0") || input.equals(""))
            return false;

        return true;
    }

    public static <T> boolean checkStringNotNull(T reference) {
        if (checkNotNull(reference) && (!reference.equals("")) && (!reference.equals("0"))) {
            return true;
        } else {
            return false;
        }
    }

    public static <T> boolean checkNotNull(T reference) {
        if (reference == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void dumper(String str) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Log.i("Dumper", str);
        }
    }

    public static Spanned fromHtml(String text) {
        if (text == null) {
            text = "";
        }
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(text);
        }
        return result;
    }

}