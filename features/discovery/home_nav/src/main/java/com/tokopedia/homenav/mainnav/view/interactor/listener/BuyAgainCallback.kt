package com.tokopedia.homenav.mainnav.view.interactor.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainView
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource

class BuyAgainCallback constructor(
    private val context: Context,
    private val addToCart: (String, String) -> Unit
) : BuyAgainView.Listener {

    override fun onProductCardClicked(productId: String) {
        val intent = RouteManager
            .getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)

        context.startActivity(intent)
    }

    override fun onAtcButtonClicked(productId: String, shopId: String, hasVariant: Boolean) {
        if (hasVariant) {
            AtcVariantHelper.goToAtcVariant(
                context = context,
                productId = productId,
                pageSource = VariantPageSource.ME_PAGE_PAGESOURCE,
                shopId = shopId,
                startActivitResult = { intent, _ ->
                    context.startActivity(intent)
                }
            )
        } else {
            addToCart(productId, shopId)
        }
    }
}
