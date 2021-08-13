package com.tokopedia.product.detail.view.listener

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import rx.subscriptions.CompositeSubscription

/**
 * Created by Yehezkiel on 28/09/20
 */
interface PartialButtonActionListener {
    fun advertiseProductClicked()
    fun rincianTopAdsClicked()
    fun addToCartClick(buttonText: String)
    fun buyNowClick(buttonText: String)
    fun buttonCartTypeClick(cartType: String, buttonText: String, isAtcButton: Boolean)

    fun topChatButtonClicked()
    fun editProductButtonClicked()
    fun getRxCompositeSubcription(): CompositeSubscription
    fun updateQuantityNonVarTokoNow(quantity: Int, miniCart: MiniCartItem, oldValue:Int)
    fun onDeleteAtcClicked()
}