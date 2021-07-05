package com.tokopedia.notifications.inApp.ruleEngine.repository;

import android.app.Application;
import android.util.Log;

import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMNotificationCacheHandler;
import com.tokopedia.notifications.common.CMRemoteConfigUtils;
import com.tokopedia.notifications.common.IrisAnalyticsEvents;
import com.tokopedia.notifications.database.RoomNotificationDB;
import com.tokopedia.notifications.inApp.ruleEngine.storage.StorageProvider;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import static com.tokopedia.notifications.common.PayloadConverterKt.HOURS_24_IN_MILLIS;

public class RepositoryManager implements StorageProvider.StorageProviderListener {

    private StorageProvider storageProvider;
    private static RepositoryManager repositoryManager;
    private final Application application;
    private CMNotificationCacheHandler cacheHandler;
    private CMRemoteConfigUtils cmRemoteConfigUtils;

    RepositoryManager(Application application) {
        RoomNotificationDB db = RoomNotificationDB.getDatabase(application);
        this.storageProvider = new StorageProvider(db.inAppDataDao(), db.elapsedTimeDao(), this);
        this.application = application;
        cacheHandler = new CMNotificationCacheHandler(application.getApplicationContext());
        cmRemoteConfigUtils = new CMRemoteConfigUtils(application.getApplicationContext());
    }

    public static void initRepository(Application application) {
        if (repositoryManager == null) {
            synchronized (RepositoryManager.class) {
                if (repositoryManager == null) {
                    repositoryManager = new RepositoryManager(application);
                }
            }
        }
    }

    public static RepositoryManager getInstance() {
        if (repositoryManager == null) {
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
        if (storageProvider.getStorageProviderListener() == null)
            storageProvider.setStorageProviderListener(this);
        return storageProvider;
    }

    public void setStorageProvider(StorageProvider storageProvider) {
        this.storageProvider = storageProvider;
    }

    @Override
    public void onInappDuplicate(CMInApp cmInApp) {
        IrisAnalyticsEvents.INSTANCE.sendInAppEvent(application.getApplicationContext(), IrisAnalyticsEvents.INAPP_CANCELLED, cmInApp);
    }

    @Override
    public void onInappWithScreenAlreadyExists(CMInApp cmInApp) {
        IrisAnalyticsEvents.INSTANCE.sendInAppEvent(application.getApplicationContext(), IrisAnalyticsEvents.INAPP_CANCELLED, cmInApp);
    }

    public void onInappExpired(CMInApp cmInApp) {
        IrisAnalyticsEvents.INSTANCE.sendInAppEvent(application.getApplicationContext(), IrisAnalyticsEvents.INAPP_EXPIRED, cmInApp);

    }

    @Override
    public void onInappFreqUpdated() {
        if (cacheHandler == null)
            cacheHandler = new CMNotificationCacheHandler(application.getApplicationContext());
        long inappDisplayCounter = cacheHandler.getLongValue(CMConstant.INAPP_DISPLAY_COUNTER);
        if (cmRemoteConfigUtils == null)
            cmRemoteConfigUtils = new CMRemoteConfigUtils(application.getApplicationContext());
        long maxInappDisplayCount = cmRemoteConfigUtils.getLongRemoteConfig(CMConstant.MAX_INAPP_DISPLAY_COUNT, 1);
        if (inappDisplayCounter < maxInappDisplayCount)
            cacheHandler.saveLongValue(CMConstant.INAPP_DISPLAY_COUNTER, inappDisplayCounter == -1 ? 1 : ++inappDisplayCounter);
    }

    @Override
    public boolean onFetchInappFromStore() {
        long currentTimeMillis = System.currentTimeMillis();
        if (cacheHandler == null)
            cacheHandler = new CMNotificationCacheHandler(application.getApplicationContext());
        long nextInappDisplayTime = cacheHandler.getLongValue(CMConstant.NEXT_INAPP_DISPLAY_TIME);
        long inappDisplayCounter = cacheHandler.getLongValue(CMConstant.INAPP_DISPLAY_COUNTER);
        if (cmRemoteConfigUtils == null)
            cmRemoteConfigUtils = new CMRemoteConfigUtils(application.getApplicationContext());
        long maxInappDisplayCount = cmRemoteConfigUtils.getLongRemoteConfig(CMConstant.MAX_INAPP_DISPLAY_COUNT, 1);
        boolean isMaxReached = inappDisplayCounter == maxInappDisplayCount;
        if (nextInappDisplayTime == 0) {
            cacheHandler.saveLongValue(CMConstant.NEXT_INAPP_DISPLAY_TIME, currentTimeMillis + HOURS_24_IN_MILLIS);
            return true;
        } else {
            if (nextInappDisplayTime > currentTimeMillis && isMaxReached) {
                return false;
            } else if (nextInappDisplayTime > currentTimeMillis && !isMaxReached) {
                return true;
            } else {
                cacheHandler.saveLongValue(CMConstant.NEXT_INAPP_DISPLAY_TIME, currentTimeMillis + HOURS_24_IN_MILLIS);
                cacheHandler.saveLongValue(CMConstant.INAPP_DISPLAY_COUNTER, 0);
                return true;
            }
        }
    }
}
