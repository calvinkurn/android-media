package com.tokopedia.data_explorer.db_explorer.domain

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.output.QueryResult
import com.tokopedia.data_explorer.db_explorer.domain.connection.models.DatabaseConnection
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.shared.base.BaseMapper
import java.io.File
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page as PageModel

internal interface Mappers {

    interface Database : BaseMapper<File, DatabaseDescriptor>

    interface Connection : BaseMapper<SQLiteDatabase, DatabaseConnection>

    interface Pragma: BaseMapper<QueryResult, PageModel>

    interface Content: BaseMapper<QueryResult, PageModel>

}
