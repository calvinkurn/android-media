package com.tokopedia.product.detail.view.listener

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import rx.subscriptions.CompositeSubscription

/**
 * Created by Yehezkiel on 28/09/20
 */
interface PartialButtonActionListener {
    fun advertiseProductClicked()
    fun rincianTopAdsClicked()
    fun onButtonFallbackClick(button: AvailableButton)
    fun buttonCartTypeClick(button: AvailableButton)

    fun topChatButtonClicked()
    fun editProductButtonClicked()
    fun getRxCompositeSubcription(): CompositeSubscription
    fun updateQuantityNonVarTokoNow(quantity: Int, miniCart: MiniCartItem.MiniCartItemProduct, oldValue:Int)
    fun onDeleteAtcClicked()
    fun onButtonsShowed(cartTypes: List<String>)
}
