package com.tokopedia.db_inspector.domain

import android.database.sqlite.SQLiteDatabase
import com.tokopedia.db_inspector.domain.connection.models.DatabaseConnection
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.shared.base.BaseMapper
import java.io.File

internal interface Mappers {

    interface Database : BaseMapper<File, DatabaseDescriptor>
    interface Connection : BaseMapper<SQLiteDatabase, DatabaseConnection>

}
