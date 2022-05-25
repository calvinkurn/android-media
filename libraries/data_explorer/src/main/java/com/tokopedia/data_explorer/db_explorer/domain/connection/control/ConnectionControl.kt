package com.tokopedia.data_explorer.db_explorer.domain.connection.control

import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.Mappers

internal class ConnectionControl(
    override val mapper: Mappers.Connection,
    override val converter: Converters.Connection
) : Control.Connection