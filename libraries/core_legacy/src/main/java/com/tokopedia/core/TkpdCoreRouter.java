package com.tokopedia.core;

import com.tokopedia.core.gcm.base.IAppNotificationReceiver;

public interface TkpdCoreRouter {

    IAppNotificationReceiver getAppNotificationReceiver();

    void onAppsFlyerInit();

    void refreshFCMTokenFromBackgroundToCM(String token, boolean force);

    void refreshFCMFromInstantIdService(String token);
}
