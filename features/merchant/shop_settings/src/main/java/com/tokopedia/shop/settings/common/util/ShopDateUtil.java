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
    public static final String FORMAT_DAY_DATE = "EEE, dd MMM yyyy";
    public static final String FORMAT_DATE = "dd MMM yyyy";

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

    public static String toReadableString(String format, Date date) {
        DateFormat fromFormat = new SimpleDateFormat(format, DEFAULT_LOCALE);
        try {
            return fromFormat.format(date);
        } catch (Exception e) {
            return date.toString();
        }
    }

    public static String toReadableString(String format, String unixTime) {
        try {
            return toReadableString(format, new Date(Long.parseLong(unixTime)));
        }catch (Exception e) {
            return unixTime;
        }
    }

    public static Date toDate(int year, int month, int dayOfMonth) {
        try {
            Calendar now =  getCurrentCalendar();
            now.set(Calendar.YEAR, year);
            now.set(Calendar.MONTH, month);
            now.set(Calendar.DATE, dayOfMonth);
            return now.getTime();
        } catch (Exception e) {
            return getCurrentDate();
        }
    }

    public static Date unixToDate(long unixTime) {
        try {
            return new Date(unixTime);
        } catch (Exception e) {
            return getCurrentDate();
        }
    }
}
