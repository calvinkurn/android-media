package com.tokopedia.db_inspector.domain.databases.interactors

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.domain.Interactors
import com.tokopedia.db_inspector.domain.databases.models.Operation
import java.io.File
import javax.inject.Inject

internal class GetDatabaseInteractor @Inject constructor(
    private val source: Sources.Raw
) : Interactors.GetDatabases {
    override suspend fun invoke(input: Operation): List<File> {
        return source.getDatabases(input)
    }
}