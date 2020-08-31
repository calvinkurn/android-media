package com.tokopedia.analyticsdebugger.debugger.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.analyticsdebugger.database.IrisSaveLogDB
import com.tokopedia.analyticsdebugger.database.IrisSendLogDB
import com.tokopedia.analyticsdebugger.database.SAVE_TABLE_IRIS_NAME
import com.tokopedia.analyticsdebugger.database.SEND_TABLE_IRIS_NAME

@Dao
interface IrisLogSaveDao {
    @Query("DELETE FROM $SAVE_TABLE_IRIS_NAME")
    fun deleteAll()

    @Insert
    fun insertAll(vararg irisSaveLogDB: IrisSaveLogDB)

    @get:Query("SELECT * FROM $SAVE_TABLE_IRIS_NAME LIMIT 100")
    val data: List<IrisSaveLogDB?>?

    @Query("SELECT * FROM $SAVE_TABLE_IRIS_NAME WHERE data LIKE :keyword ORDER BY timestamp DESC LIMIT 100 OFFSET :offset")
    fun getData(keyword: String?, offset: Int): List<IrisSaveLogDB?>?

    @get:Query("SELECT COUNT(timestamp) FROM $SAVE_TABLE_IRIS_NAME")
    val count: Int
}

@Dao
interface IrisLogSendDao {
    @Query("DELETE FROM $SEND_TABLE_IRIS_NAME")
    fun deleteAll()

    @Insert
    fun insertAll(vararg irisSendLogDB: IrisSendLogDB)

    @get:Query("SELECT * FROM $SEND_TABLE_IRIS_NAME LIMIT 100")
    val data: List<IrisSendLogDB?>?

    @Query("SELECT * FROM $SEND_TABLE_IRIS_NAME WHERE data LIKE :keyword ORDER BY timestamp DESC LIMIT 100 OFFSET :offset")
    fun getData(keyword: String?, offset: Int): List<IrisSendLogDB?>?

    @get:Query("SELECT COUNT(timestamp) FROM $SEND_TABLE_IRIS_NAME")
    val count: Int
}