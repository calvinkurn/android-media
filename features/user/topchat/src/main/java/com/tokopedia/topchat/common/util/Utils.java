package com.tokopedia.topchat.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ";
    private static final String LANGUAGE_CODE = "in";
    private static final String COUNTRY_CODE = "ID";

    private static Locale mLocale;

    public static String getDateTime(String isoTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, getLocale());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", getLocale());
            Date date = inputFormat.parse(isoTime);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

    }


    private static Locale getLocale() {
        if (mLocale == null)
            mLocale = new Locale(LANGUAGE_CODE, COUNTRY_CODE, "");
        return mLocale;
    }
}
