package com.tokopedia.journeydebugger.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.journeydebugger.database.JourneyLogDB

@Dao
interface JourneyLogDao {

    @get:Query("SELECT * FROM journey_log LIMIT 5")
    val data: List<JourneyLogDB>

    @get:Query("SELECT COUNT(id) FROM journey_log")
    val count: Int

    @Query("DELETE FROM journey_log")
    fun deleteAll()

    @Insert
    fun insertAll(vararg journeyLogDBs: JourneyLogDB)

    @Query("SELECT * FROM journey_log WHERE journey LIKE :keyword ORDER BY timestamp DESC LIMIT 20 OFFSET :offset")
    fun getData(keyword: String, offset: Int): List<JourneyLogDB>
}
