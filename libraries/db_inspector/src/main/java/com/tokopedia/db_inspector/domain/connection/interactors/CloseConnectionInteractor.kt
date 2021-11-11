package com.tokopedia.db_inspector.domain.connection.interactors

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.domain.Interactors
import javax.inject.Inject

internal class CloseConnectionInteractor @Inject constructor(
    val sources: Sources.Memory
): Interactors.CloseConnection {
    override suspend fun invoke(input: String) {
        sources.closeConnection(input)
    }
}