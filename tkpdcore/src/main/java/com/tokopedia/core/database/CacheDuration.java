package com.tokopedia.core.database;

/**
 * Created by ricoharisin on 5/10/16.
 */
public class CacheDuration {

    public static final long SECOND = 1000;
    public static final long MINUTE = 60000;

    public static long onSecond(int second) {
        return SECOND * second;
    }

    public static long onMinute(int minute) {
        return MINUTE * minute;
    }

    public static long onHour(int hour) {
        return (MINUTE *  60) * hour;
    }

    public static long onDay(int day) {
        return onHour(24) * day;
    }
}
