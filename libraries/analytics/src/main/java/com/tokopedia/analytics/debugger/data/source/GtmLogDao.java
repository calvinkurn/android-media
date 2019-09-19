package com.tokopedia.analytics.debugger.data.source;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.tokopedia.analytics.database.GtmLogDB;

import java.util.List;

/**
 * Created by meta on 23/05/19.
 */
@Dao
public interface GtmLogDao {

    @Query("DELETE FROM gtm_log")
    void deleteAll();

    @Insert
    void insertAll(GtmLogDB... gtmLogDbs);

    @Query("SELECT * FROM gtm_log LIMIT 5")
    List<GtmLogDB> getData();

    @Query("SELECT * FROM gtm_log WHERE name LIKE :keyword OR data LIKE :keyword OR category LIKE :keyword " +
            "ORDER BY timestamp DESC LIMIT 20 OFFSET :offset")
    List<GtmLogDB> getData(String keyword, int offset);

    @Query("SELECT COUNT(id) FROM gtm_log")
    int getCount();
}
