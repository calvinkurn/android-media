package com.tokopedia.notifications.inApp.ruleEngine.storage.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp;

import java.util.List;

@Dao
public interface InAppDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CMInApp inAppData);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<CMInApp> inAppDataList);

    @Query("DELETE FROM inapp_data")
    void deleteAll();

    @Query("DELETE FROM inapp_data where id = :id")
    void deleteRecord(long id);

    @Query("SELECT * from inapp_data where s = :screenName")
    List<CMInApp> getDataForScreen(String screenName);

    @Query("SELECT * from inapp_data where id = :id LIMIT 1")
    CMInApp getInAppData(long id);

    @Query("UPDATE inapp_data SET freq = freq-1,  shown = 1 where id = :id")
    void updateFrequency(long id);

    @Query("UPDATE inapp_data SET freq = freq-1,  shown = 1, st = :st  where id = :id")
    void updateFrequency(long id, long st);

    @Query("UPDATE inapp_data SET shown = 0 where id = :id")
    void updateVisibleState(long id);
}
