package com.tokopedia.minicart.cartlist

import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel

interface MiniCartListActionListener {

    fun onDeleteClicked(element: MiniCartProductUiModel)

    fun onBulkDeleteUnavailableItems()

    fun onQuantityChanged(productId: String, newQty: Int)

    fun onNotesChanged(productId: String, newNotes: String)

    fun onShowSimilarProductClicked(appLink: String)

    fun onShowUnavailableItemsCLicked()

    fun onToggleShowHideUnavailableItemsClicked()

}