package com.tokopedia.data_explorer.db_explorer.domain.schema.control

import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.Mappers

internal data class SchemaControl(
    override val mapper: Mappers.Content,
    override val converter: Converters.Content
) : Control.Content