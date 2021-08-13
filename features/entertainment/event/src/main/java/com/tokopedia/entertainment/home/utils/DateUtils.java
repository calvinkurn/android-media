package com.tokopedia.entertainment.home.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author errysuprayogi on 07,February,2020
 */
public class DateUtils {
    public static final String DEFAULT_VIEW_FORMAT = "dd MMM yyyy";
    public static final int SECOND_IN_MILIS = 1000;

    public static String dateToString(Date dateToBeFormatted, String outputFormat) {
        try {
            SimpleDateFormat newsdf = new SimpleDateFormat(outputFormat);
            return newsdf.format(dateToBeFormatted);
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatedSchedule(String schedule) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat newsdf = new SimpleDateFormat("dd\nMMM");
            Date date;
            if (schedule.contains("-")) {
                String[] arr = schedule.split("-");
                date = sdf.parse(arr[0].trim());
            } else {
                date = sdf.parse(schedule);
            }
            return newsdf.format(date).toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }
}
