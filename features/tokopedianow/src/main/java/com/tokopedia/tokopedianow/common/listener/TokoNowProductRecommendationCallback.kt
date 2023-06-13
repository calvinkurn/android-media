package com.tokopedia.tokopedianow.common.listener

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.productcard.compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel

class TokoNowProductRecommendationCallback(
    private val viewModel: TokoNowProductRecommendationViewModel,
    private val listener: TokoNowProductRecommendationListener?
): ProductCardCompactCarouselView.ProductCardCompactCarouselListener,
    TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {

    override fun onProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
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
        product: ProductCardCompactCarouselItemUiModel
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
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        if (!viewModel.isLogin) {
            listener?.openLoginPage()
        } else {
            viewModel.onCartQuantityChanged(
                position = position,
                shopId = product.shopId,
                quantity = quantity
            )
        }
    }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.productCardAddVariantClicked(
            productId = product.productCardModel.productId,
            shopId = product.shopId
        )
    }

    override fun onSeeMoreClicked(seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel) {
        listener?.seeMoreClicked(seeMoreUiModel)
    }

    override fun onProductCardAddToCartBlocked() {
        listener?.productCardAddToCartBlocked()
    }

    override fun onSeeAllClicked(headerName: String, appLink: String) {
        listener?.seeAllClicked(appLink)
    }

    override fun onChannelExpired() { /* nothing to do */ }
}
