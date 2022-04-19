package com.tokopedia.sellerapp.receiver;

import android.content.Context;
import android.content.Intent;

import androidx.media.session.MediaButtonReceiver;

import timber.log.Timber;

/**
 * Created By @ilhamsuaib on 21/09/20
 */

public class SellerAppMediaButtonReceiver extends MediaButtonReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            super.onReceive(context, intent);
        } catch (IllegalStateException e) {
            Timber.i(e.toString());
        }
    }
}
