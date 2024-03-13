package com.tokopedia.buy_more_get_more.minicart.presentation.model.event

import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

sealed interface MiniCartEditorEvent {

    data class FetchData(val param: MiniCartParam) : MiniCartEditorEvent

    data class AdjustQuantity(val product: BmgmMiniCartVisitable.ProductUiModel, val newQty: Int) : MiniCartEditorEvent

    data class DeleteCart(val product: BmgmMiniCartVisitable.ProductUiModel) : MiniCartEditorEvent

    object SetCartListCheckboxState : MiniCartEditorEvent
}
