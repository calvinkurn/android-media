package com.tokopedia.play.broadcaster.ui.model

import com.tokopedia.play.broadcaster.view.state.SelectableState

/**
 * Created by jegul on 26/05/20
 */
sealed class ProductUiModel

data class ProductContentUiModel(
        val id: Long,
        val name: String,
        val imageUrl: String,
        val stock: Int,
        val isSelectedHandler: (Long) -> Boolean,
        val isSelectable: () -> SelectableState
) : ProductUiModel() {

    val hasStock: Boolean
        get() = stock != 0
}

object ProductLoadingUiModel : ProductUiModel()