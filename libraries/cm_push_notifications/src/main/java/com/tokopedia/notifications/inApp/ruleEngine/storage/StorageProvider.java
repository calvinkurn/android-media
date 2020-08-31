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

    public StorageProvider(InAppDataDao inAppDataDao, ElapsedTimeDao elapsedTimeDao) {
        this.inAppDataDao = inAppDataDao;
        this.elapsedTimeDao = elapsedTimeDao;
    }

    @Override
    public Completable putDataToStore(final CMInApp value) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                List<CMInApp> dataFromParentID = inAppDataDao.getDataFromParentIdForPerstOff(value.parentId);
                if (dataFromParentID == null || dataFromParentID.isEmpty())
                    inAppDataDao.insert(value);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable putDataToStore(final List<CMInApp> inAppDataRecords) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.insert(inAppDataRecords);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public List<CMInApp> getDataFromStore(String key) {
        return inAppDataDao.getDataForScreen(key);
    }

    @Override
    public Completable putElapsedTimeToStore(final ElapsedTime elapsedTime) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                elapsedTimeDao.insert(elapsedTime);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public ElapsedTime getElapsedTimeFromStore() {
        return elapsedTimeDao.getLastElapsedTime();
    }

    @Override
    public Completable deleteRecord(final long id) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.deleteRecord(id);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public CMInApp getInAppData(long id) {
        return inAppDataDao.getInAppData(id);
    }

    @Override
    public Completable updateInAppDataFreq(final long id) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.updateFrequency(id);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable updateInAppDataFreq(final long id, final long newSt) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.updateFrequency(id, newSt);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable viewDismissed(final long id) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.updateVisibleState(id);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void interactedWithView(final long id) {
        Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.updateFreqWithPerst(id);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }
}
