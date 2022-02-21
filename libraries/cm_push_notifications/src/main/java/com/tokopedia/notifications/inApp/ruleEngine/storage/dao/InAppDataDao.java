package com.tokopedia.notifications.inApp.ruleEngine.storage.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.List;

@Dao
public interface InAppDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(CMInApp inAppData);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<CMInApp> inAppDataList);

    @Query("DELETE FROM inapp_data")
    void deleteAll();

    @Query("DELETE FROM inapp_data where id = :id")
    void deleteRecord(long id);

    @Query("SELECT * from inapp_data where s LIKE '%' || :screenName || '%' or s = '*'")
    List<CMInApp> getDataForScreen(String screenName);

    @Query("SELECT * from inapp_data where (s LIKE '%' || :screenName || '%' or s = '*') " +
            "and st <= :currentUnixTimeStamp and et >= :currentUnixTimeStamp and freq > 0 and shown != 1")
    List<CMInApp> getActiveDataForScreen(String screenName, long currentUnixTimeStamp);


    @Query("SELECT * from inapp_data where is_test = 1 and  (s LIKE '%' || :screenName || '%' or s = '*') " +
            "and st <= :currentUnixTimeStamp and et >= :currentUnixTimeStamp and freq > 0 and shown != 1")
    List<CMInApp> getActiveDataForScreenTestCampaign(String screenName, long currentUnixTimeStamp);

    @Query("SELECT * from inapp_data where is_test = 1 and (s LIKE '%' || :screenName || '%' or s = '*')")
    List<CMInApp> getDataForScreenTestCampaign(String screenName);

    @Query("SELECT * from inapp_data where parentId  = :parentId LIMIT 1")
    CMInApp getDataForParentId(String parentId);

    @Query("SELECT * from inapp_data where parentId  = :parentId")
    List<CMInApp> getAllDataForParentId(String parentId);

    @Query("SELECT * from inapp_data where parentId  = :parentId and (s LIKE '%' || :screenName || '%' or s = '*') LIMIT 1")
    CMInApp getDataForParentIdAndScreen(String parentId, String screenName);

    @Query("SELECT * from inapp_data where parentId = :parentId and perst_on = 0 and freq = 0 and is_interacted = 1")
    List<CMInApp> getDataFromParentIdForPerstOff(String parentId);

    @Query("SELECT * from inapp_data where id = :id LIMIT 1")
    CMInApp getInAppData(long id);

    @Query("UPDATE inapp_data SET freq = freq-1, last_shown = :currentTime  where parentId = :parentId and freq != 0")
    void updateFrequencyWithShownTime(String parentId, long currentTime);

    @Query("UPDATE inapp_data SET shown = 1 where id = :id")
    void updateShown(long id);

    @Query("UPDATE inapp_data SET freq = freq-1,  shown = 1, st = :st  where id = :id")
    void updateFrequency(long id, long st);

    @Query("UPDATE inapp_data SET shown = 0 where id = :id")
    void updateVisibleState(long id);

    @Query("UPDATE inapp_data SET shown = 0 where shown = 1")
    void updateVisibleStateForAlreadyShown();

    @Query("UPDATE inapp_data SET freq = 0, is_interacted = 1 where id = :id and perst_on = 0")
    void updateFreqWithPerst(long id);

    /*if et(end time is 0 then inApp only expired when freq == 0)*/
    @Query("Select * from inapp_data where et < :currentUnixTimeStamp and shown == 0")
    List<CMInApp> getAllExpiredInApp(long currentUnixTimeStamp);

}
