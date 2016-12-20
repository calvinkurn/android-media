package com.tokopedia.core.gcm;

import com.tokopedia.core.gcm.model.FCMTokenUpdateData;

import rx.Observable;

/**
 * @author  by alvarisi on 12/13/16.
 */

public interface IFCMRefreshTokenHandler {
    void onTokenReceive(Observable<FCMTokenUpdateData> tokenUpdate);
}