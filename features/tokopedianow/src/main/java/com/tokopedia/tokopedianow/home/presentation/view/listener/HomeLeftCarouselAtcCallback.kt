package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardSeeMoreViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardSpaceViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class HomeLeftCarouselAtcCallback (
    private val context: Context,
    private val userSession: UserSessionInterface,
    private val viewModel: TokoNowHomeViewModel,
    private val analytics: HomeAnalytics,
    private val startActivityForResult: (Intent, Int) -> Unit
): HomeLeftCarouselAtcProductCardViewHolder.HomeLeftCarouselAtcProductCardListener,
   HomeLeftCarouselAtcProductCardSeeMoreViewHolder.HomeLeftCarouselAtcProductCardSeeMoreListener,
   HomeLeftCarouselAtcProductCardSpaceViewHolder.HomeLeftCarouselAtcProductCardSpaceListener,
   HomeLeftCarouselAtcViewHolder.HomeLeftCarouselAtcListener
{

    override fun onProductCardAddVariantClicked(product: HomeLeftCarouselAtcProductCardUiModel) {
        AtcVariantHelper.goToAtcVariant(
            context = context,
            productId = product.id.orEmpty(),
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = product.shopId,
            startActivitResult =startActivityForResult
        )
    }

    override fun onProductCardQuantityChanged(
        product: HomeLeftCarouselAtcProductCardUiModel,
        quantity: Int
    ) {
        if (userSession.isLoggedIn) {
            viewModel.addProductToCart(
                channelId = product.channelId,
                productId = product.id.orEmpty(),
                quantity = quantity,
                shopId = product.shopId,
                type = TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC
            )
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onProductCardClicked(position: Int, product: HomeLeftCarouselAtcProductCardUiModel) {
        openAppLink(product.appLink)

        trackClickProduct(
            position = position,
            product = product
        )
    }

    override fun onProductCardImpressed(position: Int, product: HomeLeftCarouselAtcProductCardUiModel) {
        trackProductImpression(
            position = position,
            product = product
        )
    }

    override fun onSeeMoreClicked(appLink: String, channelId: String, headerName: String) {
        openAppLink(appLink)

        trackClickViewAllEvent(
            channelId = channelId,
            channelHeaderName = headerName
        )
    }

    override fun onLeftCarouselImpressed(channelId: String, headerName: String) {
        trackImpression(
            channelId = channelId,
            channelHeaderName = headerName
        )
    }

    override fun onLeftCarouselLeftImageClicked(
        appLink: String,
        channelId: String,
        headerName: String
    ) {
        openAppLink(appLink)

        trackClickBanner(
            channelId = channelId,
            channelHeaderName = headerName
        )
    }

    override fun onRemoveLeftCarouselAtc(channelId: String) {
        viewModel.removeLeftCarouselAtc(channelId)
    }

    override fun onProductCardSpaceClicked(
        appLink: String,
        channelId: String,
        headerName: String
    ) {
        openAppLink(appLink)

        trackClickBanner(
            channelId = channelId,
            channelHeaderName = headerName
        )
    }

    override fun onProductCardSeeMoreClickListener(product: HomeLeftCarouselAtcProductCardSeeMoreUiModel) {
        openAppLink(product.appLink)

        trackClickViewAllEvent(
            channelId = product.channelId,
            channelHeaderName = product.channelHeaderName
        )
    }

    private fun openAppLink(appLink: String) {
        if(appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }

    private fun trackProductImpression(position: Int, product: HomeLeftCarouselAtcProductCardUiModel) {
        analytics.trackImpressionProductLeftCarousel(position, product)
    }

    private fun trackClickProduct(position: Int, product: HomeLeftCarouselAtcProductCardUiModel) {
        analytics.trackClickProductLeftCarousel(position, product)
    }

    private fun trackClickViewAllEvent(channelId: String, channelHeaderName: String) {
        analytics.trackClickViewAllLeftCarousel(channelId, channelHeaderName)
    }

    private fun trackImpression(channelId: String, channelHeaderName: String) {
        analytics.trackImpressionLeftCarousel(channelId, channelHeaderName)
    }

    private fun trackClickBanner(channelId: String, channelHeaderName: String) {
        analytics.trackClickBannerLeftCarousel(channelId, channelHeaderName)
    }
}
