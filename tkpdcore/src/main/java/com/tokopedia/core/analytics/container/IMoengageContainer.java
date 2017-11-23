package com.tokopedia.core.analytics.container;

import com.tokopedia.core.analytics.model.CustomerWrapper;

import org.json.JSONObject;

/**
 * Created by Herdi_WORK on 21.02.17.
 */

public interface IMoengageContainer
{
    void initialize();

    void isExistingUser(boolean bol);

    void setUserProfile(CustomerWrapper customerWrapper);

    void sendEvent(JSONObject jsonObject, String eventName);

    void sendRegistrationStartEvent(String medium);

    void sendRegisterEvent(String fullName, String mobileNo);

    void setUserData(CustomerWrapper customerWrapper, String source);

    void logoutEvent();
}
