package com.tokopedia.notifications.inApp.ruleEngine;

import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager;
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;

public class RulesUtil {

    public interface Constants{
        int DEFAULT_FREQ = -2;
    }

    public static boolean isValidTimeFrame(long startTime, long endTime,
                                           long currentTimeStamp, ElapsedTime lastElapsedTime){
        //Should you delete all of the time data on reinitialization
        long deltaTime = 0l;
        deltaTime = android.os.SystemClock.elapsedRealtime() - lastElapsedTime.elapsedTime;
        lastElapsedTime.elapsedTime = android.os.SystemClock.elapsedRealtime();

        RepositoryManager.getInstance().getStorageProvider().
                putElapsedTimeToStore(lastElapsedTime);
        long correctedCurrentTime = currentTimeStamp + deltaTime;
        if(startTime <= correctedCurrentTime && (endTime >= correctedCurrentTime ||
                endTime == 0l)){
            return true;
        }
        else {
            return false;
        }
    }
}
