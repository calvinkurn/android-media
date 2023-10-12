package com.tokopedia.tokopedianow.common.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseProductViewHolder.TokoNowRepurchaseProductListener
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class TokoNowRepurchaseProductListener(
    private val context: Context?,
    private val viewModel: TokoNowHomeViewModel,
    private val analytics: HomeAnalytics,
    private val userSession: UserSessionInterface,
    private val startActivityForResult: (Intent, Int) -> Unit
) : TokoNowRepurchaseProductListener {
    override fun onCartQuantityChanged(data: TokoNowRepurchaseProductUiModel, quantity: Int) {
        if (userSession.isLoggedIn) {
            viewModel.onCartQuantityChanged(
                channelId = data.channelId,
                productId = data.productId,
                quantity = quantity,
                shopId = data.shopId,
                stock = data.availableStock,
                isVariant = data.isVariant,
                type = TokoNowLayoutType.REPURCHASE_PRODUCT
            )
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onProductCardImpressed(position: Int, data: TokoNowRepurchaseProductUiModel) {
        trackRepurchaseImpression(position, data)
    }

    override fun onProductCardClicked(position: Int, data: TokoNowRepurchaseProductUiModel) {
        trackRepurchaseClick(position, data)
    }

    override fun onAddVariantClicked(data: TokoNowRepurchaseProductUiModel) {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                context = it,
                productId = data.productId,
                pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                isTokoNow = true,
                shopId = data.shopId,
                startActivitResult = startActivityForResult
            )
        }
    }

    override fun createAffiliateLink(url: String): String {
        return viewModel.createAffiliateLink(url)
    }

    private fun trackRepurchaseImpression(position: Int, data: TokoNowRepurchaseProductUiModel) {
        analytics.onImpressRepurchase(position, data)
    }

    private fun trackRepurchaseClick(position: Int, data: TokoNowRepurchaseProductUiModel) {
        analytics.onClickRepurchase(position, data)
    }
}
