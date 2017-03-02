package com.tokopedia.core.analytics.container;

import com.tokopedia.core.analytics.model.CustomerWrapper;

/**
 * Created by Herdi_WORK on 21.02.17.
 */

public interface IMoengageContainer
{
    void initialize();

    void isExistingUser(boolean bol);

    void setUserProfile(CustomerWrapper customerWrapper);
}
