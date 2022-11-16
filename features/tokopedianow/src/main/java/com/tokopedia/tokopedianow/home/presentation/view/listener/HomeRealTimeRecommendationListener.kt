package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class HomeRealTimeRecommendationListener(
    private val tokoNowView: TokoNowView,
    private val viewModel: TokoNowHomeViewModel,
    private val userSession: UserSessionInterface
) : RealTimeRecommendationListener {

    private val context by lazy { tokoNowView.getFragmentPage().context }

    override fun onRecomProductCardClicked(
        recomItem: RecommendationItem,
        headerName: String,
        position: String,
        applink: String
    ) {
        RouteManager.route(context, applink)
    }

    override fun onAddToCartProductNonVariant(
        channelId: String,
        recomItem: RecommendationItem,
        quantity: Int,
        headerName: String,
        position: String
    ) {
        if (userSession.isLoggedIn) {
            viewModel.addProductToCart(
                channelId = channelId,
                productId = recomItem.productId.toString(),
                quantity = quantity,
                shopId = recomItem.shopId.toString(),
                type = TokoNowLayoutType.PRODUCT_RECOM
            )
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onAddToCartProductVariantClick(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int
    ) {
        context?.let {
            if (userSession.isLoggedIn) {
                val fragment = tokoNowView.getFragmentPage() as TokoNowHomeFragment
                AtcVariantHelper.goToAtcVariant(
                    context = it,
                    productId = recomItem.productId.toString(),
                    pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                    isTokoNow = true,
                    shopId = recomItem.shopId.toString(),
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
}
