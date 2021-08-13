package com.tokopedia.tkpd.receiver;

import android.content.Context;
import android.content.Intent;

import androidx.media.session.MediaButtonReceiver;

import timber.log.Timber;

public class AppMediaButtonReceiver extends MediaButtonReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            super.onReceive(context, intent);
        } catch (IllegalStateException e) {
            Timber.i(e.toString());
        }
    }
}
