package com.tokopedia.digital.tokocash.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.digital.tokocash.listener.TokoCashReceivedListener;

/**
 * Created by kris on 7/17/17. Tokopedia
 */

public class TokoCashBroadcastReceiver extends BroadcastReceiver {

    public static final String EXTRA_RESULT_TOKOCASH_DATA_DIGITAL = "EXTRA_RESULT_TOKOCASH_DATA_DIGITAL";
    public static final String EXTRA_TOKO_CASH_MESSAGE_FAILED_DIGITAL = "EXTRA_TOKO_CASH_MESSAGE_FAILED_DIGITAL";
    public static final String ACTION_GET_TOKOCASH_DIGITAL = TokoCashBroadcastReceiver
            .class.getCanonicalName() + ".ACTION_GET_TOKOCASH_DIGITAL";
    private TokoCashReceivedListener listener;

    public TokoCashBroadcastReceiver(TokoCashReceivedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras().containsKey(EXTRA_RESULT_TOKOCASH_DATA_DIGITAL)) {
            listener.onReceivedTokoCashData((TokoCashData) intent
                    .getParcelableExtra(EXTRA_RESULT_TOKOCASH_DATA_DIGITAL));
        } else {
            listener.onTokoCashDataError(intent.getExtras()
                    .getString(EXTRA_TOKO_CASH_MESSAGE_FAILED_DIGITAL));
        }
    }
}
