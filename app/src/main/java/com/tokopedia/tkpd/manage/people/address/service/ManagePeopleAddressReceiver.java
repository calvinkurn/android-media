package com.tokopedia.tkpd.manage.people.address.service;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created on 5/26/16.
 */
@SuppressLint("ParcelCreator")
public class ManagePeopleAddressReceiver  extends ResultReceiver {

    private Receiver mReceiver;

    public ManagePeopleAddressReceiver(Handler handler) {
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
