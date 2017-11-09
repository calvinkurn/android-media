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

    private static final long SIX_HOURS = 1000 * 60 * 60 * 6;
    private static final long THREE_HOURS = 1000 * 60 * 60 * 3;

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
            setCacheScheduler();
        }
        stopSelf();
    }

    private void setCacheScheduler() {
        startService(CacheService.getServiceIntent(getApplicationContext()));
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                new Date().getTime() + THREE_HOURS,
                THREE_HOURS,
                getCachePendingIntent(getApplicationContext())
        );
    }

    private static PendingIntent getCachePendingIntent(Context context) {
        return PendingIntent.getService(
            context,
            CACHE_SERVICE_REQUEST_CODE,
            CacheService.getServiceIntent(context),
            PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static void cancelCacheScheduler(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getCachePendingIntent(context));
        }
    }

    private boolean isAlarmManagerNotRunning() {
        return PendingIntent.getService(
                getApplicationContext(),
                CACHE_SERVICE_REQUEST_CODE,
                CacheService.getServiceIntent(getApplicationContext()),
                PendingIntent.FLAG_NO_CREATE) == null;
    }
}
