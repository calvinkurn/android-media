package com.tokopedia.posapp.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Random;

/**
 * @author okasurya on 8/28/2017
 */
public class StartUpReceiver extends BroadcastReceiver {
    public static final int FETCHER_SERVICE_REQUEST_CODE = 1001;

    private static final int MORNING_HOUR_SCHEDULE = 12;
    private static final int EVENING_HOUR_SCHEDULE = 21;
    private static final long ONE_DAY = 1000 * 60 * 60 * 24;

    private Context context;
    private Intent intentService;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        // start service after device completed booting
        intentService = FetcherService.getServiceIntent(context);
        context.startService(intentService);

        setAlarm(MORNING_HOUR_SCHEDULE);
        setAlarm(EVENING_HOUR_SCHEDULE);
    }

    private void setAlarm(int hourOfDay) {
        long time = getScheduledTime(hourOfDay);
        PendingIntent pendingIntent = PendingIntent.getService(context,
                FETCHER_SERVICE_REQUEST_CODE, intentService, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, ONE_DAY, pendingIntent);
    }

    private long getScheduledTime(int hour) {
        Calendar beginTimeRange = Calendar.getInstance();
        Calendar endTimeRange = Calendar.getInstance();

        beginTimeRange.set(Calendar.HOUR_OF_DAY, hour);
        endTimeRange.set(Calendar.HOUR_OF_DAY, hour+1);

        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > hour) {
            beginTimeRange.add(Calendar.DATE, 1);
            endTimeRange.add(Calendar.DATE, 1);
        }

        return getRandomTime(beginTimeRange.getTimeInMillis(), endTimeRange.getTimeInMillis());
    }

    /**
     * The server could be overloaded just because each client request data at the same time,
     * hence we created a random time range.
     *
     * @param begin beginning hour of time range
     * @param end ending hour of time range
     * @return random time in long value
     */
    private long getRandomTime(long begin, long end) {
        Random random = new Random();

        return begin + (long) (random.nextDouble() * (end - begin));
    }
}
