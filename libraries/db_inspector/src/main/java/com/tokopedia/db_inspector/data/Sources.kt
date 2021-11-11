package com.tokopedia.db_inspector.data

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.db_inspector.data.models.cursor.input.Query
import com.tokopedia.db_inspector.data.models.cursor.output.QueryResult
import com.tokopedia.db_inspector.domain.databases.models.Operation
import java.io.File

internal interface Sources {
    interface Raw {
        suspend fun getDatabases(operation: Operation): List<File>
        //suspend fun removeDatabase(operation: Operation): List<File>
        //suspend fun renameDatabase(operation: Operation): List<File>
        //suspend fun copyDatabase(operation: Operation): List<File>
    }

    interface Memory {
        suspend fun openConnection(path: String): SQLiteDatabase
        suspend fun closeConnection(path: String)
    }

    interface Local {
        interface Schema {

        }
    }

    interface Pragma {
        suspend fun getUserVersion(query: Query): QueryResult

    }

}