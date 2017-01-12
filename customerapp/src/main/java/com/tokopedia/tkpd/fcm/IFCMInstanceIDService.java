package com.tokopedia.tkpd.fcm;

/**
 * Created by Herdi_WORK on 12.01.17.
 */

public interface IFCMInstanceIDService {

    void updateLocalyticsPushRegistrationID(String token);

    void propagateIDtoServer(String token);

}
