package com.tokopedia.chat_common.util;

import android.content.Context;
import android.text.format.DateUtils;

import com.tokopedia.chat_common.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stevenfredian on 9/19/17.
 */

public class ChatTimeConverter {
    private static String TODAY = "Hari ini";
    private static String YESTERDAY = "Kemarin";

    public static Calendar unixToCalendar(long unixTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        return calendar;
    }

    public static String formatdiff(long unixTime) {
        Locale localeID = new Locale("in", "ID");

        Date postTime = new Date(unixTime);

        Calendar calPostDate = Calendar.getInstance();
        calPostDate.setTime(postTime);

        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(new Date());

        if (DateUtils.isToday(unixTime)) {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
            return sdfHour.format(postTime);
        } else if (calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
                && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)
                && isYesterday(calCurrentTime.get(Calendar.DAY_OF_MONTH), calPostDate.get(Calendar.DAY_OF_MONTH))) {
            return YESTERDAY;
        } else if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)) {
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMM", localeID);
            return sdfDay.format(postTime);
        } else {
            SimpleDateFormat sdfYear = new SimpleDateFormat("dd MMM yyyy", localeID);
            return sdfYear.format(postTime);
        }
    }

    private static boolean isYesterday(int currentDay, int postDay) {
        return currentDay - postDay == 1;
    }

    public static String formatTime(long unixTime) {
        Locale localeID = new Locale("in", "ID");
        Date postTime = new Date(unixTime);
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
        return sdfHour.format(postTime);
    }

    public static String formatFullTime(long unixTime) {
        Locale localeID = new Locale("in", "ID");

        Date postTime = new Date(unixTime);

        Calendar calPostDate = Calendar.getInstance();
        calPostDate.setTime(postTime);

        Calendar calCurrentTime = Calendar.getInstance();
        calCurrentTime.setTime(new Date());

        if (DateUtils.isToday(unixTime)) {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
            return (String.format("%s, %s", TODAY, sdfHour.format(postTime)));
        } else if (calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
                && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)
                && isYesterday(calCurrentTime.get(Calendar.DAY_OF_MONTH), calPostDate.get(Calendar.DAY_OF_MONTH))) {
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm", localeID);
            return (String.format("%s, %s", YESTERDAY, sdfHour.format(postTime)));
        } else if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)) {
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMM, HH:mm", localeID);
            return sdfDay.format(postTime);
        } else {
            SimpleDateFormat sdfYear = new SimpleDateFormat("dd MMM yyyy, HH:mm", localeID);
            return sdfYear.format(postTime);
        }
    }

    public static String getRelativeDate(Context context, long unixTime) {
        long diff = (Calendar.getInstance().getTimeInMillis() / 1000) - unixTime;
        String status;
        long minuteDivider = 60;
        long hourDivider = minuteDivider * 60;
        long dayDivider = hourDivider * 24;
        long monthDivider = dayDivider * 30;
        if ((diff / monthDivider) > 0) {
            status = context.getString(R.string
                    .topchat_online_months_ago, diff / monthDivider);
        } else if ((diff / dayDivider) > 0) {
            status = context.getString(R.string
                    .topchat_online_days_ago, diff / dayDivider);
        } else if ((diff / hourDivider) > 0) {
            status = context.getString(R.string.topchat_online_hours_ago,
                    diff / hourDivider);
        } else {
            long minutes = diff / minuteDivider;
            if (minutes <= 0) minutes = 1;
            status = context.getString(R.string
                    .topchat_online_minutes_ago, minutes);
        }
        
        return status;
    }

}
