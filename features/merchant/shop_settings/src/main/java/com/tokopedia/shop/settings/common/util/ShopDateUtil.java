package com.tokopedia.shop.settings.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hendry on 15/08/18.
 */

public class ShopDateUtil {
    public static final int DEFAULT_LAST_HOUR_IN_DAY = 23;
    public static final int DEFAULT_LAST_MIN_IN_DAY = 59;
    public static final int DEFAULT_LAST_SEC_IN_DAY = 59;

    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");

    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    public static Date getCurrentDate() {
        Calendar now = getCurrentCalendar();
        return now.getTime();
    }
    public static Date stringToDate(String format, String input) {
        DateFormat fromFormat = new SimpleDateFormat(format, DEFAULT_LOCALE);
        try {
            return fromFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Date doesnt valid (" + input + ")");
        }
    }

    public static Date unixToDate(String unixTime) {
        try {
            return new Date(Long.parseLong(unixTime));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Date doesnt valid (" + unixTime + ")");
        }
    }
}
