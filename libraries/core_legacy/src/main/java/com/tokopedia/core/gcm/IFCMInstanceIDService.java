package com.tokopedia.core.gcm;

/**
 * Created by Herdi_WORK on 09.12.16.
 */

public interface IFCMInstanceIDService {

    void updateMoEngageToken(String token);

    void propagateIDtoServer(String token);

}
