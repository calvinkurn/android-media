package com.tokopedia.notifications.inApp.ruleEngine.storage;

import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.InterfaceDataStore;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.ElapsedTimeDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.dao.InAppDataDao;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Completable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


public class StorageProvider implements InterfaceDataStore {

    private static final String SERVER_LOGGER_TAG = "CM_VALIDATION";
    private static final String SERVER_LOGGER_KEY_TYPE = "type";
    private static final String SERVER_LOGGER_KEY_ERROR = "err";
    private static final String SERVER_LOGGER_KEY_DATA = "data";
    private static final String SERVER_LOGGER_TYPE_EXCEPTION = "exception";
    private static final String SERVER_LOGGER_DATA_VISIBLE_STATE = "CM InApp Update Visible State";

    private final InAppDataDao inAppDataDao;
    private final ElapsedTimeDao elapsedTimeDao;
    private StorageProviderListener storageProviderListener;

    public StorageProvider(InAppDataDao inAppDataDao, ElapsedTimeDao elapsedTimeDao, StorageProviderListener storageProviderListener) {
        this.inAppDataDao = inAppDataDao;
        this.elapsedTimeDao = elapsedTimeDao;
        this.storageProviderListener = storageProviderListener;
    }

    public void setStorageProviderListener(StorageProviderListener storageProviderListener) {
        this.storageProviderListener = storageProviderListener;
    }

    public StorageProviderListener getStorageProviderListener() {
        return storageProviderListener;
    }

    @Override
    public Boolean putDataToStore(final CMInApp value) {
        //if inapp is from test campaign then no need to check for persistence
        if (value.isTest()) {
            value.setPersistentToggle(true);
            inAppDataDao.insert(value);
            return true;
        }
        List<CMInApp> dataFromParentIDPerstOff = inAppDataDao.getDataFromParentIdForPerstOff(value.parentId);
        if (dataFromParentIDPerstOff == null || dataFromParentIDPerstOff.isEmpty()) {
            CMInApp oldData = inAppDataDao.getInAppData(value.id);

            checkIfInappAlreadyExists(oldData, value);

            String newScreenData = value.screen;
            CMInApp dataForParentIdAndScreen = inAppDataDao.getDataForParentIdAndScreen(value.parentId, newScreenData);
            if (dataForParentIdAndScreen != null) {
                String existingScreenData = dataForParentIdAndScreen.screen;
                //if both new inapp and existing inapp have multiple screen names then check
                // if they have any common screen to ignore the new popup
                if (newScreenData.contains(",") && existingScreenData.contains(",")) {
                    List<String> newScreenDataList = Arrays.asList(newScreenData.split(","));
                    String[] existingScreenDataList = existingScreenData.split(",");
                    for (String screen : existingScreenDataList) {
                        if (newScreenDataList.contains(screen)) {
                            if (storageProviderListener != null)
                                storageProviderListener.onInappWithScreenAlreadyExists(value);
                            return false;
                        }
                    }
                } else {
                    if (storageProviderListener != null)
                        storageProviderListener.onInappWithScreenAlreadyExists(value);
                    return false;
                }
            }
            CMInApp dataForParentId = inAppDataDao.getDataForParentId(value.parentId);
            if (dataForParentId == null) {
                inAppDataDao.insert(value);
                return true;
            } else if (dataForParentId.freq != 0) {
                //to keep the frequency consistent with inapp popups having the same parentId
                value.freq = dataForParentId.freq;
                inAppDataDao.insert(value);
                return true;
            }
        }
        return false;
    }

    private void checkIfInappAlreadyExists(CMInApp oldData, CMInApp value) {
        if (oldData != null) {
            if (storageProviderListener != null)
                storageProviderListener.onInappDuplicate(oldData);
            inAppDataDao.deleteRecord(value.id);
        }
    }


    @Override
    public List<CMInApp> getDataFromStore(String key, boolean isActivity) {
        boolean fetchInapp = true;
        if (storageProviderListener != null)
            fetchInapp = storageProviderListener.onFetchInappFromStore();
        List<CMInApp> list;
        if (fetchInapp) {
            list = inAppDataDao.getDataForScreen(key);
        } else
            list = inAppDataDao.getDataForScreenTestCampaign(key);
        List<CMInApp> finalList = new ArrayList<>();
        if (list != null) {
            for (CMInApp cmInApp : list) {
                String screenNames = cmInApp.getScreen();
                if (!TextUtils.isEmpty(screenNames)) {
                    String[] screenNamesArray = screenNames.split(",");
                    for (String screenName : screenNamesArray) {
                        if (key.equals(screenName) || (isActivity && screenName.equals("*"))) {
                            finalList.add(cmInApp);
                            break;
                        }
                    }
                }
            }
        }
        return finalList;
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
    public Completable updateInAppDataFreq(final long id) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                CMInApp inAppData = inAppDataDao.getInAppData(id);
                if (inAppData != null) {
                    inAppDataDao.updateFrequencyWithShownTime(inAppData.parentId, System.currentTimeMillis());
                    inAppDataDao.updateShown(id);
                    storageProviderListener.onInappFreqUpdated();
                }
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
    public Completable interactedWithView(final long id) {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                inAppDataDao.updateFreqWithPerst(id);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable updateVisibleStateForAlreadyShown() {
        return Completable.fromAction(new Action0() {
            @Override
            public void call() {
                try {
                    inAppDataDao.updateVisibleStateForAlreadyShown();
                } catch (Exception e) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put(SERVER_LOGGER_KEY_TYPE, SERVER_LOGGER_TYPE_EXCEPTION);
                    messageMap.put(SERVER_LOGGER_KEY_ERROR,
                            Log.getStackTraceString(e).substring(0, (Math.min(Log.getStackTraceString(e).length(), CMConstant.TimberTags.MAX_LIMIT))));
                    messageMap.put(SERVER_LOGGER_KEY_DATA, SERVER_LOGGER_DATA_VISIBLE_STATE);
                    ServerLogger.log(Priority.P2, SERVER_LOGGER_TAG, messageMap);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public interface StorageProviderListener {
        void onInappDuplicate(CMInApp cmInApp);

        void onInappWithScreenAlreadyExists(CMInApp cmInApp);

        void onInappFreqUpdated();

        boolean onFetchInappFromStore();
    }
}
