package com.tokopedia.minicart.cartlist

interface MiniCartListActionListener {

    fun onDeleteClicked()

    fun onBulkDeleteUnavailableItems()

    fun onQuantityChanged(productId: String, newQty: Int)

    fun onNotesChanged()

    fun onShowSimilarProductClicked()

    fun onShowUnavailableItemsCLicked()

    fun onToggleShowHideUnavailableItemsClicked()

}