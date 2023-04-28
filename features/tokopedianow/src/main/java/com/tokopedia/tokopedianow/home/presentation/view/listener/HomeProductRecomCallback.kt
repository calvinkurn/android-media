package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder.HomeProductRecomListener
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class HomeProductRecomCallback(
    private val context: Context?,
    private val userSession: UserSessionInterface,
    private val viewModel: TokoNowHomeViewModel,
    private val analytics: HomeAnalytics,
    private val startActivityForResult: (Intent, Int) -> Unit
) : HomeProductRecomListener {

    override fun onProductRecomClicked(
        product: ProductCardCompactCarouselItemUiModel,
        channelId: String,
        headerName: String,
        position: Int
    ) {
        val appLink = viewModel.createAffiliateLink(product.appLink)

        analytics.onClickProductRecom(
            channelId = channelId,
            headerName = headerName,
            recommendationItem = product,
            position = position
        )

        openAppLink(appLink)
    }

    override fun onProductRecomImpressed(
        product: ProductCardCompactCarouselItemUiModel,
        channelId: String,
        headerName: String,
        position: Int
    ) {
        analytics.onImpressProductRecom(
            channelId = channelId,
            headerName = headerName,
            recommendationItem = product,
            position = position
        )
    }

    override fun onSeeAllClicked(
        channelId: String,
        appLink: String,
        headerName: String
    ) {
        analytics.onClickAllProductRecom(
            channelId = channelId,
            headerName = headerName,
            isOoc = false
        )

        RouteManager.route(context, appLink)
    }

    override fun onSeeMoreClicked(
        channelId: String,
        appLink: String,
        headerName: String
    ) {
        RouteManager.route(context, appLink)
    }

    override fun onProductRecomQuantityChanged(
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int,
        channelId: String
    ) {
        if (userSession.isLoggedIn) {
            viewModel.onCartQuantityChanged(
                channelId = channelId,
                productId = product.productCardModel.productId,
                quantity = quantity,
                shopId = product.shopId,
                stock = product.productCardModel.availableStock,
                isVariant = product.productCardModel.isVariant,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onProductCardAddVariantClicked(
        product: ProductCardCompactCarouselItemUiModel,
        position: Int
    ) {
        context?.apply {
            AtcVariantHelper.goToAtcVariant(
                context = this,
                productId = product.productCardModel.productId,
                pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                isTokoNow = true,
                shopId = product.shopId,
                startActivitResult = startActivityForResult
            )
        }
    }

    private fun openAppLink(appLink: String) {
        if (appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }
}
