package com.tokopedia.notifications.inApp.ruleEngine;

import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;

public class RulesUtil {

    public interface Constants{
        int DEFAULT_FREQ = -2;

        interface RemoteConfig {
            String KEY_CM_INAPP_END_TIME_INTERVAL = "app_cm_inapp_end_time_interval";
        }

        interface Payload {
            String NOTIFICATION_ID = "notificationId";
            String NOTIFICATION_TYPE = "notificationType";
            String CUSTOM_VALUES = "customValues";
            String CAMPAIGN_ID = "campaignId";
            String CAMPAIGN_CODE = "campaignCode";
            String CAMPAIGN_USER_TOKEN = "campaignUserToken";
            String PARENT_ID = "parentId";
            String START_TIME = "st";
            String END_TIME = "et";
            String FREQUENCY = "freq";
            String CANCELLABLE = "d";
            String IS_TEST = "isTest";
            String PERST_ON = "perstOn";
            String SCREEN_NAME = "s";
            String MULTIPLE_SCREEN_NAME = "ss";
            String UI = "ui";
        }
    }

    public static boolean isValidTimeFrame(long startTime, long endTime,
                                           long currentTimeStamp, ElapsedTime lastElapsedTime){
        //Should you delete all of the time data on reinitialization
        RepositoryManager.getInstance().getStorageProvider().
                putElapsedTimeToStore(lastElapsedTime).subscribe();
        return startTime <= currentTimeStamp && (endTime >= currentTimeStamp ||
                endTime == 0l);
    }
}
