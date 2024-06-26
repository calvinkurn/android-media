package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class HomeRealTimeRecommendationListener(
    private val tokoNowView: TokoNowView,
    private val viewModel: TokoNowHomeViewModel,
    private val userSession: UserSessionInterface,
    private val onAddToCartBlocked: () -> Unit,
) : RealTimeRecommendationListener {

    private val context by lazy { tokoNowView.getFragmentPage().context }

    override fun onRecomProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val appLink = viewModel.createAffiliateLink(product.appLink)
        RouteManager.route(context, appLink)
    }

    override fun onAddToCartProductNonVariant(
        channelId: String,
        item: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        if (userSession.isLoggedIn) {
            viewModel.onCartQuantityChanged(
                channelId = channelId,
                productId = item.getProductId(),
                quantity = quantity,
                shopId = item.shopId,
                stock = item.productCardModel.availableStock,
                isVariant = item.productCardModel.isVariant,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onAddToCartProductVariantClick(
        position: Int,
        item: ProductCardCompactCarouselItemUiModel
    ) {
        context?.let {
            if (userSession.isLoggedIn) {
                val fragment = tokoNowView.getFragmentPage() as TokoNowHomeFragment
                AtcVariantHelper.goToAtcVariant(
                    context = it,
                    productId = item.getProductId(),
                    pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                    isTokoNow = true,
                    shopId = item.shopId,
                    startActivitResult = fragment::startActivityForResult
                )
            } else {
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
        }
    }

    override fun refreshRealTimeRecommendation(data: HomeRealTimeRecomUiModel) {
        viewModel.refreshRealTimeRecommendation(data.channelId, data.parentProductId, data.type)
    }

    override fun removeRealTimeRecommendation(data: HomeRealTimeRecomUiModel) {
        viewModel.removeRealTimeRecommendation(data.channelId, data.type)
    }

    override fun onAddToCartProductBlocked() = onAddToCartBlocked()
}
