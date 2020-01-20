package com.tokopedia.common.travel.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nabillasabbaha on 19/11/18.
 */
public class TravelDateUtil {

    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT+7");
    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");
    public static final String DEFAULT_VIEW_FORMAT = "dd MMM yyyy";
    public static final String DEFAULT_VIEW_TIME_FORMAT = "dd MMM yyyy, HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_DATE = "EEEE, dd LLLL yyyy";
    public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String VIEW_FORMAT_WITHOUT_YEAR = "dd MMM";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";

    public static Date stringToDate(String format, String input) {
        DateFormat fromFormat = new SimpleDateFormat(format, DEFAULT_LOCALE);
        try {
            return fromFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Date doesnt valid (" + input + ") with format" + format);
        }
    }

    public static String dateToString(String format, Date input) {
        DateFormat formatDate = new SimpleDateFormat(format, DEFAULT_LOCALE);
        return formatDate.format(input);
    }

    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    public static Date addTimeToSpesificDate(Date date, int field, int value) {
        Calendar now = getCurrentCalendar();
        now.setTime(date);
        now.add(field, value);
        return now.getTime();
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String formatDate(String currentFormat, String newFormat, String dateString) {
        return formatDate(currentFormat, newFormat, dateString, DEFAULT_LOCALE);
    }

    public static String formatDateByUsersTimezone(String currentFormat, String newFormat, String dateString) {
        TimeZone timeZone = TimeZone.getDefault();
        return formatDate(currentFormat, newFormat, dateString, DEFAULT_LOCALE, DEFAULT_TIMEZONE, timeZone);
    }

    public static String formatDate(String currentFormat, String newFormat, String dateString, Locale locale) {
        try {
            DateFormat fromFormat = new SimpleDateFormat(currentFormat, locale);
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat(newFormat, locale);
            toFormat.setLenient(false);
            Date date = fromFormat.parse(dateString);
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    public static String formatDate(String currentFormat,
                                    String newFormat,
                                    String dateString,
                                    Locale locale,
                                    TimeZone fromTimeZone,
                                    TimeZone toTimezone) {
        try {
            DateFormat fromFormat = new SimpleDateFormat(currentFormat, locale);
            fromFormat.setTimeZone(fromTimeZone);
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat(newFormat, locale);
            toFormat.setLenient(false);
            toFormat.setTimeZone(toTimezone);
            Date date = fromFormat.parse(dateString);
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    public static String formatToUi(String dateStr) {
        return formatDate(YYYY_MM_DD, DEFAULT_VIEW_FORMAT, dateStr);
    }
}
