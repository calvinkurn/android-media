package com.tokopedia.sellerapp.fcm;

/**
 * Created by Herdi_WORK on 12.01.17.
 */

public interface IFCMInstanceIDService {


    void updateLocalyticsPushRegistrationID(String token);

    void propagateIDtoServer(String token);

}
