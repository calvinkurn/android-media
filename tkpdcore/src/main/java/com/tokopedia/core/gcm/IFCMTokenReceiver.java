package com.tokopedia.core.gcm;

import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import rx.Observable;

/**
 * @author  by alvarisi on 12/13/16.
 */

public interface IFCMTokenReceiver {
    void onTokenReceive(Observable<FCMTokenUpdate> tokenUpdate);
}