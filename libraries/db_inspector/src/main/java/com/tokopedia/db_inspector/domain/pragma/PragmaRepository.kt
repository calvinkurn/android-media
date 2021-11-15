package com.tokopedia.db_inspector.domain.pragma

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.shared.models.Page
import com.tokopedia.db_inspector.domain.shared.models.parameters.PragmaParameters
import javax.inject.Inject

internal class PragmaRepository @Inject constructor(
    val sources: Sources.Pragma,
    private val control: Control.Pragma,
) : Repositories.Pragma {
    override suspend fun getUserVersion(input: PragmaParameters.Version): Page {
        val query = control.converter version input
        val queryResult = sources.getUserVersion(query)
        return control.mapper(queryResult)
    }
         //control.mapper(userVersion(control.converter version input) )

}