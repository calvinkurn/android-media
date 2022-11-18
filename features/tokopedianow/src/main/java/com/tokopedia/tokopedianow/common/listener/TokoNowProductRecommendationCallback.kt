package com.tokopedia.tokopedianow.common.listener

import android.os.Parcelable
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardCarouselView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel

class TokoNowProductRecommendationCallback(
    private val viewModel: TokoNowProductRecommendationViewModel?,
    private val listener: TokoNowProductRecommendationListener?
): TokoNowProductCardCarouselView.TokoNowProductCardCarouselListener {
    override fun onProductCardClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
    }

    override fun onProductCardImpressed(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
    }

    override fun onProductCardAnimationFinished(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    ) {
        viewModel?.updateUi(
            productId = product.productCardModel.productId,
            quantity = quantity
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    ) {
        viewModel?.addProductToCart(
            productId = product.productCardModel.productId,
            shopId = product.shopId,
            quantity = quantity
        )
    }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {

    }

    override fun onSeeMoreClicked(seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel) {
    }

    override fun saveScrollState(state: Parcelable?) {}

    override fun getScrollState(): Parcelable? = null

}
