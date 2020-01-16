package com.tokopedia.notifications.inApp.ruleEngine.storage;

import com.tokopedia.notifications.inApp.ruleEngine.interfaces.InterfaceDataStore;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.ElapsedTimeDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.InAppDataDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.List;

import rx.Completable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class StorageProvider implements InterfaceDataStore {
    private InAppDataDao inAppDataDao;
    private ElapsedTimeDao elapsedTimeDao;

    public StorageProvider(InAppDataDao inAppDataDao, ElapsedTimeDao elapsedTimeDao){
        this.inAppDataDao = inAppDataDao;
        this.elapsedTimeDao = elapsedTimeDao;
    }

    @Override
    public void putDataToStore(final CMInApp value) {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.insert(value);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void putDataToStore(final List<CMInApp> inAppDataRecords) {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.insert(inAppDataRecords);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public List<CMInApp> getDataFromStore(String key) {
        return inAppDataDao.getDataForScreen(key);
    }

    @Override
    public void putElapsedTimeToStore(final ElapsedTime elapsedTime) {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                elapsedTimeDao.insert(elapsedTime);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public ElapsedTime getElapsedTimeFromStore() {
        return  elapsedTimeDao.getLastElapsedTime();
    }

    @Override
    public void deleteRecord(final long id) {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.deleteRecord(id);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public CMInApp getInAppData(long id) {
        return inAppDataDao.getInAppData(id);
    }

    @Override
    public void updateInAppDataFreq(final long id) {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.updateFrequency(id);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void updateInAppDataFreq(final long id, final long newSt) {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.updateFrequency(id, newSt);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void viewDismissed(final long id){
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.updateVisibleState(id);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }
}
