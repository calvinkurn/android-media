package com.tokopedia.tkpd.talkview.intentservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by stevenfredian on 5/16/16.
 */
public class TalkDetailResultReceiver extends ResultReceiver{

    public TalkDetailResultReceiver(Handler handler) {
        super(handler);
    }

    private Receiver mReceiver;

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
