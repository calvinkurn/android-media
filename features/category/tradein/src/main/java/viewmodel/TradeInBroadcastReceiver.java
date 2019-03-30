package viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import view.customview.TradeInTextView;

public class TradeInBroadcastReceiver extends BroadcastReceiver {
    private BroadcastListener broadcastListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction()!=null && intent.getAction().equals(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE)) {
            broadcastListener.onReceiveTradeIn();
        }
    }

    public void setBroadcastListener(BroadcastListener listener) {
        this.broadcastListener = listener;
    }

    public interface BroadcastListener {
        void onReceiveTradeIn();
    }
}
