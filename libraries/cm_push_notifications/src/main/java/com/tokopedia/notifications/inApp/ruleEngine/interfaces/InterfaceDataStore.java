package com.tokopedia.notifications.inApp.ruleEngine.interfaces;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.List;

public interface InterfaceDataStore {
    void putDataToStore(CMInApp value);
    void putDataToStore(List<CMInApp> inAppDataRecords);
    List<CMInApp> getDataFromStore(String key);
    void putElapsedTimeToStore(ElapsedTime elapsedTime);
    ElapsedTime getElapsedTimeFromStore();
    void deleteRecord(long id);
    CMInApp getInAppData(long id);
    void updateInAppDataFreq(long id);
    void updateInAppDataFreq(long id, long newSt);
    void viewDismissed(long id);
}
