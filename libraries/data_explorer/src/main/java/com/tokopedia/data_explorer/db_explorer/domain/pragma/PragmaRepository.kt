package com.tokopedia.data_explorer.db_explorer.domain.pragma

import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.parameters.PragmaParameters
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

    override suspend fun getTableInfo(input: PragmaParameters.Pragma): Page {
        val query = control.converter pragma input
        val queryResult = sources.getTableInfo(query)
        return control.mapper(queryResult)
    }


}