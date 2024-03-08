package com.tokopedia.analyticsdebugger.debugger.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.analyticsdebugger.database.SERVER_LOG_TABLE_NAME
import com.tokopedia.analyticsdebugger.database.ServerLogDB

@Dao
interface ServerLogDao {
    @Query("DELETE FROM $SERVER_LOG_TABLE_NAME")
    fun deleteAll()

    @Insert
    fun insertAll(vararg serverLogDB: ServerLogDB)

    @get:Query("SELECT * FROM $SERVER_LOG_TABLE_NAME LIMIT 100")
    val data: List<ServerLogDB?>?

    @Query("SELECT * FROM $SERVER_LOG_TABLE_NAME WHERE data LIKE :keyword ORDER BY timestamp DESC LIMIT 100 OFFSET :offset")
    fun getData(keyword: String?, offset: Int): List<ServerLogDB?>?

    @get:Query("SELECT COUNT(timestamp) FROM $SERVER_LOG_TABLE_NAME")
    val count: Int
}