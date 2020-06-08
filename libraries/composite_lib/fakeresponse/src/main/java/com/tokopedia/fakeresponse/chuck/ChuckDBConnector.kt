package com.tokopedia.fakeresponse.chuck

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase

object ChuckDBConnector {

    val chuckDbName = "chucker.db"
    private var database: SQLiteDatabase? = null

    fun getDatabase(context: Context):SQLiteDatabase? {
        if (database == null)
            database = context.applicationContext.openOrCreateDatabase(chuckDbName, MODE_PRIVATE, null)
        return database
    }

    fun close(){
        database?.close()
        database = null
    }


}