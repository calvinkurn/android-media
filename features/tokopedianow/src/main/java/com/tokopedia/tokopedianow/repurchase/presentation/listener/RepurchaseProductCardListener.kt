package com.tokopedia.tokopedianow.repurchase.presentation.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseProductViewHolder.RepurchaseProductCardListener
import com.tokopedia.tokopedianow.repurchase.presentation.viewmodel.TokoNowRepurchaseViewModel
import com.tokopedia.user.session.UserSessionInterface

class RepurchaseProductCardListener(
    private val context: Context,
    private val viewModel: TokoNowRepurchaseViewModel,
    private val userSession: UserSessionInterface,
    private val startActivityForResult: (Intent, Int) -> Unit
) : RepurchaseProductCardListener {
    override fun onClickProduct(item: RepurchaseProductUiModel) {
    }

    override fun onAddToCartVariant(item: RepurchaseProductUiModel) {
        AtcVariantHelper.goToAtcVariant(
            context = context,
            productId = item.id,
            pageSource = TokoNowHomeFragment.SOURCE,
            isTokoNow = true,
            shopId = item.shopId,
            startActivitResult = startActivityForResult
        )
    }

    override fun onAddToCartNonVariant(item: RepurchaseProductUiModel, quantity: Int) {
        if (userSession.isLoggedIn) {
            viewModel.onClickAddToCart(item.id, quantity, item.shopId)
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onProductImpressed(item: RepurchaseProductUiModel) {
    }
}
