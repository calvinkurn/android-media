package com.tokopedia.db_inspector.domain

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.db_inspector.data.models.cursor.input.Query
import com.tokopedia.db_inspector.data.models.cursor.output.QueryResult
import com.tokopedia.db_inspector.domain.databases.models.Operation
import com.tokopedia.db_inspector.domain.shared.base.BaseInteractor
import java.io.File

internal interface Interactors {

    interface GetDatabases : BaseInteractor<Operation, List<File>>

    interface OpenConnection : BaseInteractor<String, SQLiteDatabase>

    interface CloseConnection : BaseInteractor<String, Unit>

    interface GetUserVersion: BaseInteractor<Query, QueryResult>
}