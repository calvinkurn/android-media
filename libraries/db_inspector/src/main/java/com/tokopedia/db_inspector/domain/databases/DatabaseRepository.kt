package com.tokopedia.db_inspector.domain.databases

import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Interactors
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.db_inspector.domain.shared.models.parameters.DatabaseParameters
import javax.inject.Inject

internal class DatabaseRepository @Inject constructor(
    private val getPageInteractor: Interactors.GetDatabases,
    private val control: Control.Database
): Repositories.Database {

    override suspend fun getPage(input: DatabaseParameters.Get): List<DatabaseDescriptor> =
        getPageInteractor.invoke(control.converter get input).map { control.mapper(it) }

}