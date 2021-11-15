package com.tokopedia.db_inspector.domain.databases

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.shared.models.parameters.DatabaseParameters
import javax.inject.Inject

internal class DatabaseRepository @Inject constructor(
    private val source: Sources.Raw,
    private val control: Control.Database
) : Repositories.Database {

    override suspend fun getPage(input: DatabaseParameters.Get): List<DatabaseDescriptor> {
        val operation = control.converter get input
        return source.getDatabases(operation).map { control.mapper(it) }
    }

}