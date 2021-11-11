package com.tokopedia.db_inspector.domain

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.db_inspector.data.models.cursor.output.QueryResult
import com.tokopedia.db_inspector.domain.connection.models.DatabaseConnection
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.shared.base.BaseMapper
import java.io.File
import com.tokopedia.db_inspector.domain.shared.models.Page as PageModel

internal interface Mappers {

    interface Database : BaseMapper<File, DatabaseDescriptor>

    interface Connection : BaseMapper<SQLiteDatabase, DatabaseConnection>

    interface Pragma: BaseMapper<QueryResult, PageModel>

}
