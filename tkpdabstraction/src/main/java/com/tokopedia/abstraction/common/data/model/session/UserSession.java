package com.tokopedia.abstraction.common.data.model.session;

/**
 * Created by nathan on 11/28/17.
 */

public interface UserSession {

    String getAccessToken();

    String getFreshToken();

    String getUserId();

    String getDeviceId();

    boolean isLoggedIn();

    String getShopId();

    boolean hasShop();

    boolean isMsisdnVerified();
}
