package com.tokopedia.tokopedianow.home.presentation.view.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder.TokoNowSeeMoreCardCarouselViewHolder
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class HomeLeftCarouselAtcCallback(
    private val context: Context?,
    private val userSession: UserSessionInterface,
    private val viewModel: TokoNowHomeViewModel,
    private val analytics: HomeAnalytics,
    private val startActivityForResult: (Intent, Int) -> Unit
) : HomeLeftCarouselAtcProductCardViewHolder.HomeLeftCarouselAtcProductCardListener,
    TokoNowSeeMoreCardCarouselViewHolder.TokoNowCarouselProductCardSeeMoreListener,
    HomeLeftCarouselAtcViewHolder.HomeLeftCarouselAtcListener {

    override fun onProductCardAddVariantClicked(
        product: HomeLeftCarouselAtcProductCardUiModel
    ) {
        context?.apply {
            AtcVariantHelper.goToAtcVariant(
                context = this,
                productId = product.id.orEmpty(),
                pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                isTokoNow = true,
                shopId = product.shopId,
                startActivitResult = startActivityForResult
            )
        }
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

    override fun onProductCardClicked(
        position: Int,
        product: HomeLeftCarouselAtcProductCardUiModel
    ) {
        openAppLink(product.appLink)

        analytics.trackClickProductLeftCarousel(
            position = position,
            product = product
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: HomeLeftCarouselAtcProductCardUiModel
    ) {
        analytics.trackImpressionProductLeftCarousel(
            position = position,
            product = product
        )
    }

    override fun onSeeMoreClicked(
        appLink: String,
        channelId: String,
        headerName: String
    ) {
        openAppLink(appLink)

        analytics.trackClickViewAllLeftCarousel(
            id = channelId,
            headerName = headerName
        )
    }

    override fun onLeftCarouselImpressed(
        channelId: String,
        headerName: String
    ) {
        analytics.trackImpressionLeftCarousel(
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

        analytics.trackClickBannerLeftCarousel(
            channelId = channelId,
            channelHeaderName = headerName
        )
    }

    override fun onRemoveLeftCarouselAtc(channelId: String) {
        viewModel.removeLeftCarouselAtc(channelId)
    }

    override fun onProductCardSeeMoreClickListener(seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel) {
        openAppLink(seeMoreUiModel.appLink)

        analytics.trackClickViewAllLeftCarousel(
            id = seeMoreUiModel.id,
            headerName = seeMoreUiModel.headerName
        )
    }

    private fun openAppLink(appLink: String) {
        if (appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }
}
