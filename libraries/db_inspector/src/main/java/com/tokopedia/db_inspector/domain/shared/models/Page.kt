package com.tokopedia.db_inspector.domain.shared.models

internal data class Page(
    val nextPage: Int? = null,
    val beforeCount: Int = 0,
    val afterCount: Int = 0,
    val cells: List<Cell>
)

internal data class Cell(
    val text: String? = null
)
