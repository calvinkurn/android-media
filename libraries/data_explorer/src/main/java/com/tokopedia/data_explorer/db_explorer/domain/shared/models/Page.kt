package com.tokopedia.data_explorer.db_explorer.domain.shared.models

import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Order

internal data class Page(
    val nextPage: Int? = null,
    val beforeCount: Int = 0,
    val afterCount: Int = 0,
    val cells: List<Cell>
)

internal data class Cell(
    val text: String? = null,
    val active: Boolean = false,
    val order: Order = Order.ASCENDING
)
