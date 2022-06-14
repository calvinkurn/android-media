package com.tokopedia.oldminicart.cartlist

import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartProductUiModel

interface MiniCartListActionListener {

    fun onDeleteClicked(element: MiniCartProductUiModel)

    fun onBulkDeleteUnavailableItems()

    fun onQuantityChanged(productId: String, newQty: Int)

    fun onNotesChanged(productId: String, newNotes: String)

    fun onShowSimilarProductClicked(appLink: String, element: MiniCartProductUiModel)

    fun onShowUnavailableItemsCLicked()

    fun onToggleShowHideUnavailableItemsClicked()

    fun onProductInfoClicked(element: MiniCartProductUiModel)

    fun onQuantityPlusClicked()

    fun onQuantityMinusClicked()

    fun onInputQuantityClicked(qty: Int)

    fun onWriteNotesClicked()

    fun onChangeNotesClicked()
}