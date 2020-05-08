package com.tokopedia.entertainment.home.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author errysuprayogi on 07,February,2020
 */
public class DateUtils {


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
        }catch (Exception e){
            return "";
        }
    }
}
