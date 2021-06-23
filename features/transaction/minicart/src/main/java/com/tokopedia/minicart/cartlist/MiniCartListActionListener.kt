package com.tokopedia.minicart.cartlist

import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.Action

interface MiniCartListActionListener {

    fun onDeleteClicked(element: MiniCartProductUiModel)

    fun onBulkDeleteUnavailableItems()

    fun onQuantityChanged(productId: String, newQty: Int)

    fun onNotesChanged(productId: String, newNotes: String)

    fun onShowSimilarProductClicked(appLink: String, element: MiniCartProductUiModel)

    fun onTobaccoLiteUrlClicked(url: String, element: MiniCartProductUiModel, action: Action)

    fun onShowUnavailableItemsCLicked()

    fun onToggleShowHideUnavailableItemsClicked()

    fun onProductInfoClicked(element: MiniCartProductUiModel)

    fun onQuantityPlusClicked()

    fun onQuantityMinusClicked()

    fun onInputQuantityClicked(qty: Int)

    fun onWriteNotesClicked()

    fun onChangeNotesClicked()

    fun onShowUnavailableItem(element: MiniCartProductUiModel)
}