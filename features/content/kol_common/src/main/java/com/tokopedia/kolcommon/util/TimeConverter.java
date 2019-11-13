package com.tokopedia.kolcommon.util;

import android.content.Context;

import com.tokopedia.kolcommon.R;

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
    private static final String DEFAULT_FEED_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DEFAULT_KOL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String HOUR_MINUTE_FORMAT = "HH:mm";
    private static final String DAY_MONTH_FORMAT = "dd MMMM";
    private static final String DAY_MONTH_YEAR_FORMAT = "dd MMMM yyyy";
    private static final String DAY_MONTH_SHORT_FORMAT = "dd MMM";
    private static final String DAY_MONTH_YEAR_SHORT_FORMAT = "dd MMM yyyy";
    private static final String DAY_MONTH_YEAR_SHORT_TIME_FORMAT = "dd MMM yyyy, hh:mm";
    private static final String COUNTRY_ID = "ID";
    private static final String LANGUAGE_ID = "in";

    public static String generateTime(Context context, String postTime) {
        return generateTime(context, postTime, DEFAULT_FEED_FORMAT);
    }

    private static String generateTime(Context context, String postTime, String format) {
        try {
            Locale localeID = new Locale(LANGUAGE_ID, COUNTRY_ID);

            SimpleDateFormat sdf = new SimpleDateFormat(format, localeID);
            sdf.setTimeZone(TimeZone.getDefault());
            Date postDate = sdf.parse(postTime);
            return getFormattedTime(context, postDate);

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

    private static String getFormattedTime(Context context, Date postDate) {
        Locale localeID = new Locale(LANGUAGE_ID, COUNTRY_ID);
        Date currentTime = new Date();

        Calendar calPostDate = Calendar.getInstance();
        calPostDate.setTime(postDate);

        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(currentTime);

        SimpleDateFormat sdfHour = new SimpleDateFormat(HOUR_MINUTE_FORMAT, localeID);
        SimpleDateFormat sdfDay = new SimpleDateFormat(DAY_MONTH_FORMAT, localeID);
        SimpleDateFormat sdfYear = new SimpleDateFormat(DAY_MONTH_YEAR_FORMAT, localeID);

        if (getDifference(currentTime, postDate) < 60) {
            return context.getString(R.string.post_time_just_now);

        } else if (getDifference(currentTime, postDate) / SECONDS_IN_MINUTE < 60) {
            return getDifference(currentTime, postDate) / SECONDS_IN_MINUTE
                    + context.getString(R.string.post_time_minutes_ago);

        } else if (getDifference(currentTime, postDate) / MINUTES_IN_HOUR < 24
                && calCurrentTime.get(Calendar.DAY_OF_MONTH) == calPostDate.get(Calendar.DAY_OF_MONTH)
                && calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
                && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)) {
            return getDifference(currentTime, postDate) / MINUTES_IN_HOUR
                    + context.getString(R.string.post_time_hours_ago);

        } else if (calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
                && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)
                && isYesterday(calCurrentTime.get(Calendar.DAY_OF_MONTH), calPostDate.get(Calendar.DAY_OF_MONTH))) {
            return context.getString(R.string.post_time_yesterday_at) + sdfHour.format(postDate);

        } else if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR))
            return sdfDay.format(postDate)
                    + context.getString(R.string.post_time_hour)
                    + sdfHour.format(postDate);

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

    public static String formatUnix(Context context, long unix) {
        Date postTime = new Date(unix);
        return getFormattedTime(context, postTime);
    }

    public static String generateTimeYearly(String postTime) {
        Locale localeID = new Locale(LANGUAGE_ID, COUNTRY_ID);
        try {
            SimpleDateFormat sdfDay = new SimpleDateFormat(DAY_MONTH_SHORT_FORMAT, localeID);
            SimpleDateFormat sdfYear = new SimpleDateFormat(DAY_MONTH_YEAR_SHORT_FORMAT, localeID);

            SimpleDateFormat sdf = new SimpleDateFormat(DAY_MONTH_YEAR_SHORT_TIME_FORMAT, localeID);
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
