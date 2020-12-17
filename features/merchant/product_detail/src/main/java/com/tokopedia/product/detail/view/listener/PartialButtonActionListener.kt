package com.tokopedia.product.detail.view.listener

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
}