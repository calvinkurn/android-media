package com.tokopedia.posapp.view.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.tokopedia.core.util.SessionHandler;

import java.util.Date;
import java.util.Random;

/**
 * Created by okasurya on 10/2/17.
 */

public class SchedulerService extends IntentService {
    private static final String ACTION_START = "com.tokopedia.posapp.view.SchedulerService.action.START";
    public static final int CACHE_SERVICE_REQUEST_CODE = 1001;

    private static final long SIX_HOUR = 1000 * 60 * 60 * 6;

    public SchedulerService() {
        super("SchedulerService");
    }

    public static Intent getDefaultServiceIntent(Context context) {
        Intent intent = new Intent(context, SchedulerService.class);
        intent.setAction(ACTION_START);
        return intent;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null
                && intent.getAction().equals(ACTION_START)
                && SessionHandler.isV4Login(this)) {
            handleActionStart();
        }
        stopSelf();
    }

    private void handleActionStart() {
//        if(isAlarmManagerNotRunning()) {
            startService(CacheService.getServiceIntent(getApplicationContext()));

            PendingIntent pendingIntent = PendingIntent.getService(
                    getApplicationContext(),
                    CACHE_SERVICE_REQUEST_CODE,
                    CacheService.getServiceIntent(getApplicationContext()),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime() + SIX_HOUR, SIX_HOUR, pendingIntent);
//        }
    }

    private boolean isAlarmManagerNotRunning() {
        return PendingIntent.getService(
                getApplicationContext(),
                CACHE_SERVICE_REQUEST_CODE,
                CacheService.getServiceIntent(getApplicationContext()),
                PendingIntent.FLAG_NO_CREATE) == null;
    }

    /**
     * The server could be overloaded just because each client request data at the same time,
     * hence I created a random time range.
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
