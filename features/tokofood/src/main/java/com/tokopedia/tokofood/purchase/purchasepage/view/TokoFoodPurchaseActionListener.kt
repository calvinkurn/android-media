package com.tokopedia.tokofood.purchase.purchasepage.view

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface TokoFoodPurchaseActionListener {

    fun getPreviousItems(currentIndex: Int, count: Int): List<Visitable<*>>

    fun onTextChangeShippingAddressClicked()

    fun onTextSetPinpointClicked()

    fun onTextAddItemClicked()

    fun onTextBulkDeleteUnavailableProductsClicked()

    fun onQuantityChanged(newQuantity: Int)

    fun onIconDeleteProductClicked()

    fun onTextChangeNotesClicked()

    fun onTextChangeNoteAndVariantClicked()
}