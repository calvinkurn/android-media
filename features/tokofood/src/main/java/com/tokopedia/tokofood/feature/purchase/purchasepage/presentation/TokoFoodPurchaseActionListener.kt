package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModelOld

interface TokoFoodPurchaseActionListener {

    fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>>

    fun onTextChangeShippingAddressClicked()

    fun onTextSetPinpointClicked()

    fun onTextAddItemClicked()

    fun onTextBulkDeleteUnavailableProductsClicked()

    fun onQuantityChanged()

    fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel)

    fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModelOld)

    fun onTextChangeNotesClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel)

    fun onTextChangeNotesClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModelOld)

    fun onTextChangeNoteAndVariantClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel)

    fun onTextChangeNoteAndVariantClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModelOld)

    fun onToggleShowHideUnavailableItemsClicked()

    fun onTextShowUnavailableItemClicked()

    fun onPromoWidgetClicked()

    fun onButtonCheckoutClicked()

    fun onSurgePriceIconClicked(title: String, desc: String)
}
