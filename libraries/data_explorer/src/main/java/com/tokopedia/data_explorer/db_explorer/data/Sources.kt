package com.tokopedia.data_explorer.db_explorer.data

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Query
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.QueryResult
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.Operation
import java.io.File

internal interface Sources {
    interface Raw {
        suspend fun getDatabases(operation: Operation): List<File>
        suspend fun removeDatabase(operation: Operation): List<File>
        suspend fun copyDatabase(operation: Operation): List<File>

    }

    interface Memory {
        suspend fun openConnection(path: String): SQLiteDatabase
        suspend fun closeConnection(path: String)
    }

    interface Local {
        interface Schema {
            suspend fun getTables(query: Query): QueryResult
            suspend fun getTableByName(query: Query): QueryResult
            suspend fun dropTableContentByName(query: Query): QueryResult
        }
    }

    interface Pragma {
        suspend fun getUserVersion(query: Query): QueryResult
        suspend fun getTableInfo(query: Query): QueryResult
    }

}