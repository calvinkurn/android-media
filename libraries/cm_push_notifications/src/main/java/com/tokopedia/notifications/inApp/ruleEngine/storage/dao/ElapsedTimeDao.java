package com.tokopedia.notifications.inApp.ruleEngine.storage.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
