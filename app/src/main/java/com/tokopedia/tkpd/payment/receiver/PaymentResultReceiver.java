package com.tokopedia.tkpd.payment.receiver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * @author by Angga.Prasetiyo on 20/05/2016.
 */
@SuppressLint("ParcelCreator")
public class PaymentResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public PaymentResultReceiver(Handler handler, Receiver receiver) {
        super(handler);
        this.mReceiver = receiver;
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
