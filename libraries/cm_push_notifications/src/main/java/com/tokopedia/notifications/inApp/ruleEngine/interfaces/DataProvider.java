package com.tokopedia.notifications.inApp.ruleEngine.interfaces;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.List;

public interface DataProvider {
    void notificationsDataResult(List<CMInApp> inAppDataList, int entityHashCode, String name);
    void sendEventInAppDelivered(CMInApp cmInApp);
}
