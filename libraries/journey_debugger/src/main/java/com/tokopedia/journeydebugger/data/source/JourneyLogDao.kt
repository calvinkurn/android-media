package com.tokopedia.journeydebugger.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.journeydebugger.database.JourneyLogDB

@Dao
interface JourneyLogDao {

    @get:Query("SELECT * FROM applink_log LIMIT 5")
    val data: List<JourneyLogDB>

    @get:Query("SELECT COUNT(id) FROM applink_log")
    val count: Int

    @Query("DELETE FROM applink_log")
    fun deleteAll()

    @Insert
    fun insertAll(vararg applinkLogDBs: JourneyLogDB)

    @Query("SELECT * FROM applink_log WHERE applink LIKE :keyword OR traces LIKE :keyword " + "ORDER BY timestamp DESC LIMIT 20 OFFSET :offset")
    fun getData(keyword: String, offset: Int): List<JourneyLogDB>
}
