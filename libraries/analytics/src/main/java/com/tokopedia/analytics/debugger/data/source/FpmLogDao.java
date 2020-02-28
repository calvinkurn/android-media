package com.tokopedia.analytics.debugger.data.source;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tokopedia.analytics.database.FpmLogDB;

import java.util.List;

import rx.Observable;

/**
 * Created by meta on 23/05/19.
 */
@Dao
public interface FpmLogDao {

    @Query("DELETE FROM fpm_log")
    void deleteAll();

    @Insert
    void insertAll(FpmLogDB... fpmLogDbs);

    @Query("SELECT * FROM fpm_log LIMIT 5")
    List<FpmLogDB> getData();

    @Query("SELECT * FROM fpm_log WHERE tracename LIKE :keyword OR attributes LIKE :keyword OR metrics LIKE :keyword " +
            "ORDER BY timestamp DESC LIMIT 20 OFFSET :offset")
    List<FpmLogDB> getData(String keyword, int offset);

    @Query("SELECT * FROM fpm_log ORDER BY timestamp DESC")
    List<FpmLogDB> getAllData();

    @Query("SELECT COUNT(id) FROM fpm_log")
    int getCount();
}
