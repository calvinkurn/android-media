package com.tokopedia.core.gcm.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.tokopedia.core.gcm.database.model.DbPushNotification;

import java.util.List;

@Dao
public interface PushNotificationDao {

    @Query("SELECT * FROM DbPushNotification")
    List<DbPushNotification> getData();

    @Query("SELECT * FROM DbPushNotification WHERE category=:category")
    List<DbPushNotification> getDataByCategory(String category);

    @Query("SELECT * FROM DbPushNotification WHERE category=:category ORDER BY customIndex ASC, id DESC")
    List<DbPushNotification> getDataByCategoryOrderAsc(String category);

    @Query("SELECT * FROM DbPushNotification WHERE category=:category ORDER BY customIndex DESC, id DESC")
    List<DbPushNotification> getDataByCategoryOrderDesc(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DbPushNotification... pushNotifications);

    @Query("DELETE FROM DbPushNotification WHERE category=:category")
    void deleteByCategory(String category);

    @Query("DELETE FROM DbPushNotification WHERE category=:category AND serverId=:serverId")
    void deleteByCategoryAndServerId(String category, String serverId);

    @Query("DELETE FROM DbPushNotification")
    void drop();
}
