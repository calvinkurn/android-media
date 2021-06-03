package com.tokopedia.product.detail.view.listener

import com.tokopedia.minicart.common.domain.data.MiniCartItem

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
    fun leasingButtonClicked()
    fun editProductButtonClicked()
    fun updateQuantityNonVarTokoNow(quantity: Int, miniCart: MiniCartItem)
}