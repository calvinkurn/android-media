package com.tokopedia.core.addtocart.receiver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Angga.Prasetiyo on 29/03/2016.
 */
@SuppressLint("ParcelCreator")
public class ATCResultReceiver extends ResultReceiver {
    private static final String TAG = ATCResultReceiver.class.getSimpleName();
    private Receiver mReceiver;

    public ATCResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
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
