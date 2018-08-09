package com.tokopedia.challenges.view.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Utils {
    private static Utils singleInstance;
    public static final String QUERY_PARAM_CHALLENGE_ID="challenge-id";
    public static final String QUERY_PARAM_KEY_START="start";
    public static final String QUERY_PARAM_KEY_SIZE="size";
    public static final String QUERY_PARAM_KEY_SORT="sort";
    public static final String QUERY_PARAM_KEY_SORT_RECENT="recent";
    public static final String QUERY_PARAM_KEY_SORT_POINTS="points";

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
    }

    public static String convertUTCToString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date d = null;
        String formattedTime=null;
        try {
            d = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("d MM yyyy", new Locale("in", "ID", ""));
            sdf2.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            formattedTime = sdf2.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return " "+formattedTime;
    }

}
