package com.tokopedia.tokopedianow.common.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowProductCardViewHolder
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.user.session.UserSessionInterface

class TokoNowProductCardListener(
    private val context: Context?,
    private val viewModel: TokoNowHomeViewModel,
    private val analytics: HomeAnalytics,
    private val userSession: UserSessionInterface,
    private val startActivityForResult: (Intent, Int) -> Unit
) : TokoNowProductCardViewHolder.TokoNowProductCardListener {
    override fun onCartQuantityChanged(data: TokoNowProductCardUiModel, quantity: Int) {
        if (userSession.isLoggedIn) {
            viewModel.onCartQuantityChanged(
                channelId = data.channelId,
                productId = data.productId,
                quantity = quantity,
                shopId = data.shopId,
                stock = data.stock,
                isVariant = data.isVariant(),
                type = TokoNowLayoutType.REPURCHASE_PRODUCT
            )
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onProductCardImpressed(position: Int, data: TokoNowProductCardUiModel) {
        when (data.type) {
            TokoNowLayoutType.REPURCHASE_PRODUCT -> trackRepurchaseImpression(position, data)
        }
    }

    override fun onProductCardClicked(position: Int, data: TokoNowProductCardUiModel) {
        when (data.type) {
            TokoNowLayoutType.REPURCHASE_PRODUCT -> trackRepurchaseClick(position, data)
        }
    }

    override fun onAddVariantClicked(data: TokoNowProductCardUiModel) {
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

    private fun trackRepurchaseImpression(position: Int, data: TokoNowProductCardUiModel) {
        analytics.onImpressRepurchase(position, data)
    }

    private fun trackRepurchaseClick(position: Int, data: TokoNowProductCardUiModel) {
        analytics.onClickRepurchase(position, data)
    }
}
