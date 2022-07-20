package com.tokopedia.data_explorer.db_explorer.data.sources.connection

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.data_explorer.db_explorer.data.Sources

internal class AndroidConnectionSource: Sources.Memory {

    private val connectionPool: HashMap<String, SQLiteDatabase> = hashMapOf()

    override suspend fun openConnection(path: String): SQLiteDatabase {
        if (connectionPool.containsKey(path).not()) {
            connectionPool[path] = SQLiteDatabase.openOrCreateDatabase(path, null)
        }
        return connectionPool[path] ?: SQLiteDatabase.openOrCreateDatabase(path, null)
    }

    override suspend fun closeConnection(path: String) {
        if (connectionPool.containsKey(path)) {
            connectionPool[path]?.let {
                if (it.isOpen) {
                    it.close()
                    connectionPool.remove(path)
                }
            }
        }
    }
}