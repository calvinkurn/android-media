package com.tokopedia.common.travel.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nabillasabbaha on 19/11/18.
 */
public class TravelDateUtil {

    public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");
    public static final String DEFAULT_VIEW_FORMAT = "dd MMM yyyy";

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
        return  formatDate.format(input);
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
}
