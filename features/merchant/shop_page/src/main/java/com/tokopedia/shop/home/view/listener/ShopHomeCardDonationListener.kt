package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeCardDonationUiModel

interface ShopHomeCardDonationListener {
    fun onCardDonationClick(model: ShopHomeCardDonationUiModel)
    fun onImpressCardDonation(model: ShopHomeCardDonationUiModel, position: Int)
}