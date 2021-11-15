package com.tokopedia.db_inspector.domain.schema.control

import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.Mappers

internal data class SchemaControl(
    override val mapper: Mappers.Content,
    override val converter: Converters.Content
) : Control.Content