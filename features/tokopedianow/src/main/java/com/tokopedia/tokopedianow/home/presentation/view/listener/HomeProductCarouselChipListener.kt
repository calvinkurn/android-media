package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.home.presentation.view.HomeProductCarouselChipsView.HomeProductCarouselChipsViewListener
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel

class HomeProductCarouselChipListener(
    private val context: Context,
    private val viewModel: TokoNowHomeViewModel,
    private val startActivityForResult: (Intent, Int) -> Unit
): HomeProductCarouselChipsViewListener {

    override fun onProductCardQuantityChanged(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    ) {
    }

    override fun onClickVariantAddToCart(productId: String, shopId: String) {
    }

    override fun onClickProductCard(position: Int, product: TokoNowProductCardCarouselItemUiModel) {
    }

    override fun onProductCardImpressed(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
    }

    override fun onClickChipItem(channelId: String, chip: TokoNowChipUiModel) {
        viewModel.switchProductCarouselChipTab(channelId, chip.id)
    }
}
