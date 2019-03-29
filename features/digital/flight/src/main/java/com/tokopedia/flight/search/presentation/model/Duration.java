package com.tokopedia.flight.search.presentation.model;

/**
 * Created by User on 11/17/2017.
 */

public class Duration {
    private int day;
    private int hour;
    private int minute;

    public Duration(int day, int hour, int minute) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
