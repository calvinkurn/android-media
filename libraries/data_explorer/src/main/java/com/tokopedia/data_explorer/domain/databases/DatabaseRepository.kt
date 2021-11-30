package com.tokopedia.data_explorer.domain.databases

import com.tokopedia.data_explorer.data.Sources
import com.tokopedia.data_explorer.domain.Control
import com.tokopedia.data_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.domain.shared.models.parameters.DatabaseParameters
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