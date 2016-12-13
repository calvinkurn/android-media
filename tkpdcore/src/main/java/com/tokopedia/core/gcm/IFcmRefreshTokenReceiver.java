package com.tokopedia.core.gcm;

import com.tokopedia.core.gcm.model.FcmTokenUpdate;

import rx.Observable;

/**
 * @author  by alvarisi on 12/13/16.
 */

public interface IFcmRefreshTokenReceiver {
    void onTokenReceive(Observable<FcmTokenUpdate> tokenUpdate);
}