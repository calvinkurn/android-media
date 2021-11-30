package com.tokopedia.data_explorer.domain.schema

import com.tokopedia.data_explorer.data.Sources
import com.tokopedia.data_explorer.domain.Control
import com.tokopedia.data_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.domain.shared.models.parameters.ContentParameters
import javax.inject.Inject

internal class TableRepository @Inject constructor(
    private val schemaSource: Sources.Local.Schema,
    private val control: Control.Content
): Repositories.Schema {

    override suspend fun getPage(input: ContentParameters): Page {
        val query = control.converter(input)
        return control.mapper(schemaSource.getTables(query))
    }
}