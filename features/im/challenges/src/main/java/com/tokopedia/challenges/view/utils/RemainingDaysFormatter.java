package com.tokopedia.challenges.view.utils;

public class RemainingDaysFormatter {
    private long remainingDays;
    private long remainingHours;
    private long remainingMinutes;
    private long remainingSeconds;
    private long secondsInMilli = 1000;
    private long minutesInMilli = secondsInMilli * 60;
    private long hoursInMilli = minutesInMilli * 60;
    private long daysInMilli = hoursInMilli * 24;



    public RemainingDaysFormatter(long startTimeInMillis, long endTimeInMillis) {
        long difference = endTimeInMillis - startTimeInMillis;
        remainingDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        remainingHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        remainingMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        remainingSeconds = difference / secondsInMilli;

    }

    public long getRemainingDays() {
        return remainingDays;
    }

    public long getRemainingHours() {
        return remainingHours;
    }

    public long getRemainingMinutes() {
        return remainingMinutes;
    }

    public long getRemainingSeconds() {
        return remainingSeconds;
    }
}
