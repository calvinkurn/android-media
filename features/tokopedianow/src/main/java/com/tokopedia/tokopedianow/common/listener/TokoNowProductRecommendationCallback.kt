package com.tokopedia.tokopedianow.common.listener

import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardCarouselView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel

class TokoNowProductRecommendationCallback(
    private val viewModel: TokoNowProductRecommendationViewModel,
    private val listener: TokoNowProductRecommendationListener?
): TokoNowProductCardCarouselView.TokoNowProductCardCarouselListener,
    TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {

    override fun onProductCardClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.productCardClicked(
            position = position,
            product = product,
            isLogin = viewModel.isLogin,
            userId = viewModel.userId
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.productCardImpressed(
            position = position,
            product = product,
            isLogin = viewModel.isLogin,
            userId = viewModel.userId
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    ) {
        if (!viewModel.isLogin) {
            listener?.openLoginPage()
        } else {
            viewModel.addProductToCart(
                position = position,
                shopId = product.shopId,
                quantity = quantity
            )
        }
    }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        listener?.productCardAddVariantClicked(
            productId = product.productCardModel.productId,
            shopId = product.shopId
        )
    }

    override fun onSeeMoreClicked(seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel) {
        listener?.seeMoreClicked(seeMoreUiModel)
    }

    override fun onSeeAllClicked(headerName: String, appLink: String) {
        listener?.seeAllClicked(appLink)
    }

    override fun onChannelExpired() { /* nothing to do */ }
}
