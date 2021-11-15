package com.tokopedia.db_inspector.domain.schema

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.shared.models.Page
import com.tokopedia.db_inspector.domain.shared.models.parameters.ContentParameters
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