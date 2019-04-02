package com.tokopedia.posapp.cache.view.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author okasurya on 8/28/2017
 */
public class StartUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(SchedulerService.getDefaultServiceIntent(context));
    }
}
