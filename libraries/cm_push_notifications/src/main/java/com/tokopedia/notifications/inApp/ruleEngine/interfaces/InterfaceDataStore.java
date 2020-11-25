package com.tokopedia.notifications.inApp.ruleEngine.interfaces;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.List;

import rx.Completable;

public interface InterfaceDataStore {
    Completable putDataToStore(CMInApp value);
    Completable putDataToStore(List<CMInApp> inAppDataRecords);
    List<CMInApp> getDataFromStore(String key);
    Completable putElapsedTimeToStore(ElapsedTime elapsedTime);
    ElapsedTime getElapsedTimeFromStore();
    Completable deleteRecord(long id);
    CMInApp getInAppData(long id);
    Completable updateInAppDataFreq(long id);
    Completable updateInAppDataFreq(long id, long newSt);
    Completable viewDismissed(long id);
    Completable interactedWithView(long id);
}
