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

    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");
    public static final String FORMAT_DATE = "EEE, dd MMM yyyy";

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

    public static String toString(Date date) {
        DateFormat fromFormat = new SimpleDateFormat(FORMAT_DATE, DEFAULT_LOCALE);
        try {
            return fromFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Date doesnt valid");
        }
    }

    public static final Date toDate(int year, int month, int dayOfMonth) {
        try {
            Calendar now =  getCurrentCalendar();
            now.set(Calendar.YEAR, year);
            now.set(Calendar.MONTH, month);
            now.set(Calendar.DATE, dayOfMonth);
            return now.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Date doesnt valid");
        }
    }

    public static Date unixToDate(long unixTime) {
        try {
            return new Date(unixTime);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Date doesnt valid (" + unixTime + ")");
        }
    }
}
