package com.tokopedia.notifications.inApp.ruleEngine.storage.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.ElapsedTime;

@Dao
public interface ElapsedTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ElapsedTime elapsedTime);

    @Query("DELETE FROM inapp_data")
    void deleteAll();

    @Query("SELECT * from elapsed_time")
    ElapsedTime getLastElapsedTime();
}
