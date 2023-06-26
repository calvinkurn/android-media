package com.tokopedia.tokopedianow.common.listener

import android.content.Context
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
            isLogin = viewModel.isLoggedIn(),
            userId = viewModel.getUserId()
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.productCardImpressed(
            position = position,
            product = product,
            isLogin = viewModel.isLoggedIn(),
            userId = viewModel.getUserId()
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        if (!viewModel.isLoggedIn()) {
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

    override fun onSeeAllClicked(
        context: Context,
        headerName: String,
        appLink: String,
        widgetId: String
    ) {
        listener?.seeAllClicked(appLink)
    }

    override fun onChannelExpired() { /* nothing to do */ }
}
