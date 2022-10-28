package com.tokopedia.data_explorer.db_explorer.extensions

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

internal fun RecyclerView.setupGrid() {
    val verticalDecorator = DividerItemDecoration(
        context,
        DividerItemDecoration.VERTICAL
    )
    addItemDecoration(verticalDecorator)
    val horizontalDecorator = DividerItemDecoration(
        context,
        DividerItemDecoration.HORIZONTAL
    )
    addItemDecoration(horizontalDecorator)
}