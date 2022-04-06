package com.tokopedia.minicart.cartlist

import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel

interface MiniCartListActionListener {

    fun onDeleteClicked(element: MiniCartProductUiModel)

    fun onBulkDeleteUnavailableItems()

    fun onQuantityChanged(element: MiniCartProductUiModel, newQty: Int)

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