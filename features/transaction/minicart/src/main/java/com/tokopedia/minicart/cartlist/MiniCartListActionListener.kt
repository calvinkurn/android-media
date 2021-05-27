package com.tokopedia.minicart.cartlist

interface MiniCartListActionListener {

    fun onDeleteClicked()

    fun onBulkDeleteUnavailableItems()

    fun onQuantityChanged()

    fun onNotesChanged()

    fun onShowSimilarProductClicked()

}