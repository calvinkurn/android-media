package com.tokopedia.notifications.inApp.ruleEngine.repository;

import android.app.Application;
import android.util.Log;

import com.tokopedia.notifications.inApp.ruleEngine.storage.RoomDB;
import com.tokopedia.notifications.inApp.ruleEngine.storage.StorageProvider;

public class RepositoryManager {

    private StorageProvider storageProvider;
    private static RepositoryManager repositoryManager;
    private Application application;

    RepositoryManager(Application application) {
        RoomDB db = RoomDB.getDatabase(application);
        this.storageProvider = new StorageProvider(db.inAppDataDao(), db.elapsedTimeDao());
        this.application = application;
    }

    public static void initRepository(Application application){
        if(repositoryManager == null){
            synchronized (RepositoryManager.class){
                if(repositoryManager == null) {
                    repositoryManager = new RepositoryManager(application);
                }
            }
        }
    }

    public static RepositoryManager getInstance(){
        if(repositoryManager == null){
            try {
                throw new Exception();
            } catch (Exception e) {
                Log.e("Init RepositoryManager",
                        "Init RepositoryManager before using it");
            }
        }
        return repositoryManager;
    }

    public StorageProvider getStorageProvider() {
        return storageProvider;
    }

    public void setStorageProvider(StorageProvider storageProvider) {
        this.storageProvider = storageProvider;
    }
}
