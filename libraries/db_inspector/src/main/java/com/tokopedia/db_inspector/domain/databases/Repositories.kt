package com.tokopedia.db_inspector.domain.databases

import com.tokopedia.db_inspector.domain.connection.models.DatabaseConnection
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.shared.base.BaseRepository
import com.tokopedia.db_inspector.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.db_inspector.domain.shared.models.parameters.DatabaseParameters

internal interface Repositories {

    interface Database: BaseRepository<DatabaseParameters.Get, List<DatabaseDescriptor>> {

    }

    interface Connection {
        suspend fun open(input: ConnectionParameters): DatabaseConnection
        suspend fun close(input: ConnectionParameters)
    }


}