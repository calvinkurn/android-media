package com.tokopedia.data_explorer.db_explorer.domain.databases

import com.tokopedia.data_explorer.db_explorer.domain.connection.models.DatabaseConnection
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.shared.base.BaseRepository
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ConnectionParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.ContentParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.DatabaseParameters
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.PragmaParameters

internal interface Repositories {

    interface Database: BaseRepository<DatabaseParameters.Get, List<DatabaseDescriptor>> {
        suspend fun remove(input: DatabaseParameters.Command): List<DatabaseDescriptor>
        suspend fun copy(input: DatabaseParameters.Command): List<DatabaseDescriptor>

    }

    interface Connection {
        suspend fun open(input: ConnectionParameters): DatabaseConnection
        suspend fun close(input: ConnectionParameters)
    }

    interface Pragma {
        suspend fun getUserVersion(input: PragmaParameters.Version): Page
        suspend fun getTableInfo(input: PragmaParameters.Pragma): Page
    }

    interface Schema: BaseRepository<ContentParameters, Page> {
        suspend fun getByName(input: ContentParameters): Page
        suspend fun dropByName(input: ContentParameters): Page
    }


}