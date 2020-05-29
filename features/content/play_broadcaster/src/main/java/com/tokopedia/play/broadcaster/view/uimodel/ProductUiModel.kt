package com.tokopedia.play.broadcaster.view.uimodel

import com.tokopedia.play.broadcaster.view.state.SelectableState

/**
 * Created by jegul on 26/05/20
 */
data class ProductUiModel(
        val id: Long,
        val name: String,
        val imageUrl: String,
        val stock: Int,
        val isSelectedHandler: (Long) -> Boolean,
        val isSelectable: () -> SelectableState
) {

    val hasStock: Boolean
        get() = stock != 0
}