package com.tokopedia.analyticsdebugger.debugger.data.source;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tokopedia.analyticsdebugger.database.ApplinkLogDB;

import java.util.List;

@Dao
public interface ApplinkLogDao {

    @Query("DELETE FROM applink_log")
    void deleteAll();

    @Insert
    void insertAll(ApplinkLogDB... applinkLogDBs);

    @Query("SELECT * FROM applink_log LIMIT 5")
    List<ApplinkLogDB> getData();

    @Query("SELECT * FROM applink_log WHERE applink LIKE :keyword OR traces LIKE :keyword " +
            "ORDER BY timestamp DESC LIMIT 20 OFFSET :offset")
    List<ApplinkLogDB> getData(String keyword, int offset);

    @Query("SELECT COUNT(id) FROM applink_log")
    int getCount();
}
