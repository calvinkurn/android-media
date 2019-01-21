package com.tokopedia.core.util;

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

    public static <T> boolean checkStringNotNull(T reference) {
        if (checkNotNull(reference) && (!reference.equals("")) && (!reference.equals("0"))) {
            return true;
        } else {
            return false;
        }
    }

    public static void dumper(String str) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Log.i("Dumper", str);
        }
    }

}