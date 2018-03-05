package com.tokopedia.tkpdcontent.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author by nisie on 5/17/17.
 */

public class TimeConverter {
    private static final long SECONDS_IN_MINUTE = 60;
    private static final long MINUTES_IN_HOUR = 60 * 60;
    private static final long HOUR_IN_DAY = 60 * 60 * 24;
    public static final String DEFAULT_FEED_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DEFAULT_KOL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String generateTime(String postTime) {
        return generateTime(postTime, DEFAULT_FEED_FORMAT);
    }

    public static String generateTime(String postTime, String format) {
        try {
            Locale localeID = new Locale("in", "ID");

            SimpleDateFormat sdf = new SimpleDateFormat(format, localeID);
            sdf.setTimeZone(TimeZone.getDefault());
            Date postDate = sdf.parse(postTime);
            return getFormattedTime(postDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return postTime;
        }
    }

    private static TimeZone getTimezone(String postTime) {
        if (postTime.endsWith("Z"))
            return TimeZone.getTimeZone("UTC");
        else {
            return TimeZone.getDefault();
        }
    }

    public static String getFormattedTime(Date postDate) {
        Locale localeID = new Locale("in", "ID");
        Date currentTime = new Date();

        Calendar calPostDate = Calendar.getInstance();
        calPostDate.setTime(postDate);

        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(currentTime);

        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMMM", localeID);
        SimpleDateFormat sdfYear = new SimpleDateFormat("dd MMMM yyyy", localeID);

        if (getDifference(currentTime, postDate) < 60) {
            return "Baru saja";
        } else if (getDifference(currentTime, postDate) / SECONDS_IN_MINUTE < 60) {
            return getDifference(currentTime, postDate) / SECONDS_IN_MINUTE + " menit yang lalu";
        } else if (getDifference(currentTime, postDate) / MINUTES_IN_HOUR < 24
                && calCurrentTime.get(Calendar.DAY_OF_MONTH) == calPostDate.get(Calendar.DAY_OF_MONTH)
                && calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
                && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)) {
            return getDifference(currentTime, postDate) / MINUTES_IN_HOUR + " jam yang lalu";
        } else if (calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
                && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)
                && isYesterday(calCurrentTime.get(Calendar.DAY_OF_MONTH), calPostDate.get(Calendar.DAY_OF_MONTH))) {
            return "Kemarin pukul " + sdfHour.format(postDate);
        } else if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR))
            return sdfDay.format(postDate) + " pukul " + sdfHour.format(postDate);
        else {
            return sdfYear.format(postDate);
        }
    }

    private static boolean isYesterday(int currentDay, int postDay) {
        return currentDay - postDay == 1;
    }

    private static long getDifference(Date currentTime, Date postDate) {
        return (currentTime.getTime() - postDate.getTime()) / 1000;
    }

    public static String formatUnix(long unix) {
        Date postTime = new Date(unix);
        return getFormattedTime(postTime);
    }

    public static String generateTimeYearly(String postTime) {
        Locale localeID = new Locale("in", "ID");
        try {
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMM", localeID);
            SimpleDateFormat sdfYear = new SimpleDateFormat("dd MMM yyyy", localeID);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm", localeID);
            Date postDate = sdf.parse(postTime);
            Date currentTime = new Date();

            Calendar calPostDate = Calendar.getInstance();
            calPostDate.setTime(postDate);

            Calendar calCurrentTime = Calendar.getInstance();
            calCurrentTime.setTime(currentTime);

            if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR))
                return sdfDay.format(postDate);
            else {
                return sdfYear.format(postDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return postTime;
        }
    }
}
