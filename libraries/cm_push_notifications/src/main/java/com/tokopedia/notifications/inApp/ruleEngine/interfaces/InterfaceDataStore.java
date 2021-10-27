package com.tokopedia.notifications.inApp.ruleEngine.interfaces;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.List;

import rx.Completable;

public interface InterfaceDataStore {
    Boolean putDataToStore(CMInApp value);
    List<CMInApp> getDataFromStore(String key, boolean isActivity);
    Completable putElapsedTimeToStore(ElapsedTime elapsedTime);
    ElapsedTime getElapsedTimeFromStore();
    Completable deleteRecord(long id);
    Completable updateInAppDataFreq(long id);
    Completable viewDismissed(long id);
    Completable interactedWithView(long id);
    Completable updateVisibleStateForAlreadyShown();
}
