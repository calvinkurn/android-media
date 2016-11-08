package com.tokopedia.core.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ricoharisin on 4/29/16.
 */
public class HadesBroadcastReceiver extends BroadcastReceiver {

    private HadesBroadcastReceiver.ReceiveListener mReceiver;

    public HadesBroadcastReceiver() {

    }

    public void setReceiver(HadesBroadcastReceiver.ReceiveListener receive) {
        this.mReceiver = receive;
    }

    public interface ReceiveListener {
        void onHadesRunning();

        void onHadesComplete();

        void onHadesNoConenction();

        void onHadesTimeout();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isStatusRunning(intent)) {
            mReceiver.onHadesRunning();
        } else if(isStatusTimeout(intent)) {
            mReceiver.onHadesTimeout();
        } else if (isStatusNoConnection(intent)) {
            mReceiver.onHadesNoConenction();
        } else {
            mReceiver.onHadesComplete();
        }
    }

    private Boolean isStatusRunning(Intent intent) {
        return intent.getIntExtra(HadesService.STATUS, -1) == HadesService.STATUS_RUNNING;
    }

    private Boolean isStatusTimeout(Intent intent) {
        return intent.getIntExtra(HadesService.STATUS, -1) == HadesService.STATUS_TIMEOUT;
    }

    private Boolean isStatusNoConnection(Intent intent) {
        return intent.getIntExtra(HadesService.STATUS, -1) == HadesService.STATUS_NO_CONNECTION;
    }
}
