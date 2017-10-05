package com.tokopedia.core.drawer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.drawer.listener.TokoCashUpdateListener;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;

/**
 * Created by kris on 4/21/17. Tokopedia
 */

public class TokoCashBroadcastReceiver extends BroadcastReceiver{

    public static final String EXTRA_RESULT_TOKOCASH_DATA = "EXTRA_RESULT_TOKOCASH_DATA";
    public static final String EXTRA_TOKO_CASH_MESSAGE_FAILED = "EXTRA_TOKO_CASH_MESSAGE_FAILED";
    public static final String ACTION_GET_TOKOCASH = TokoCashBroadcastReceiver
            .class.getCanonicalName() + ".ACTION_GET_TOKOCASH";
    private TokoCashUpdateListener listener;

    public TokoCashBroadcastReceiver(TokoCashUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras().containsKey(EXTRA_RESULT_TOKOCASH_DATA)) {
            listener.onReceivedTokoCashData((DrawerTokoCash) intent
                    .getParcelableExtra(EXTRA_RESULT_TOKOCASH_DATA));
        } else {
            listener.onTokoCashDataError(intent.getExtras()
                    .getString(EXTRA_TOKO_CASH_MESSAGE_FAILED));
        }
    }
}
