package com.tokopedia.db_inspector.domain.connection.control

import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.Mappers

internal class ConnectionControl(
    override val mapper: Mappers.Connection,
    override val converter: Converters.Connection
) : Control.Connection