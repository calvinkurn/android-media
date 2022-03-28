package com.tokopedia.tokofood.purchase.purchasepage.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel

interface TokoFoodPurchaseActionListener {

    fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>>

    fun onTextChangeShippingAddressClicked()

    fun onTextSetPinpointClicked()

    fun onTextAddItemClicked()

    fun onTextBulkDeleteUnavailableProductsClicked()

    fun onQuantityChanged()

    fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel)

    fun onTextChangeNotesClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel)

    fun onTextChangeNoteAndVariantClicked()

    fun onToggleShowHideUnavailableItemsClicked()

    fun onTextShowUnavailableItemClicked()

    fun onButtonCheckoutClicked()
}