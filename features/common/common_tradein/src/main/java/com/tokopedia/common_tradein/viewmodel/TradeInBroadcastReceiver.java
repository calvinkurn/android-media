package com.tokopedia.common_tradein.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.common_tradein.customviews.TradeInTextView;

public class TradeInBroadcastReceiver extends BroadcastReceiver {
    private BroadcastListener broadcastListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE)) {
            broadcastListener.onReceiveTradeIn(intent.getBooleanExtra(TradeInTextView.EXTRA_ISELLIGIBLE, false), intent.getStringExtra(TradeInTextView.EXTRA_DESCRIPTION));
        }
    }

    public void setBroadcastListener(BroadcastListener listener) {
        this.broadcastListener = listener;
    }

    public interface BroadcastListener {
        void onReceiveTradeIn(boolean isElligible, String description);
    }
}
