package com.tokopedia.tokopedianow.repurchase.presentation.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.PRODUCT_REPURCHASE
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseProductViewHolder.RepurchaseProductCardListener
import com.tokopedia.tokopedianow.repurchase.presentation.viewmodel.TokoNowRepurchaseViewModel
import com.tokopedia.user.session.UserSessionInterface

class RepurchaseProductCardListener(
    private val context: Context,
    private val viewModel: TokoNowRepurchaseViewModel,
    private val userSession: UserSessionInterface,
    private val analytics: RepurchaseAnalytics,
    private val startActivityForResult: (Intent, Int) -> Unit,
    private val showToaster: (String, Int, String, () -> Unit) -> Unit
) : RepurchaseProductCardListener {
    override fun onClickProduct(item: RepurchaseProductUiModel, position: Int) {
        analytics.onClickProduct(userSession.userId, item, position)
    }

    override fun onAddToCartVariant(item: RepurchaseProductUiModel) {
        AtcVariantHelper.goToAtcVariant(
            context = context,
            productId = item.productCardModel.productId,
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = item.shopId,
            startActivitResult = startActivityForResult
        )
    }

    override fun onAddToCartNonVariant(item: RepurchaseProductUiModel, quantity: Int) {
        if (userSession.isLoggedIn) {
            viewModel.onClickAddToCart(item.productCardModel.productId, quantity, PRODUCT_REPURCHASE, item.shopId)
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onProductImpressed(item: RepurchaseProductUiModel) {}

    override fun onClickSimilarProduct() {
        analytics.onClickSimilarProduct(userSession.userId)
    }

    override fun onWishlistButtonClicked(
        productId: String,
        isWishlistSelected: Boolean,
        descriptionToaster: String,
        ctaToaster: String,
        type: Int,
        ctaClickListener: (() -> Unit)?
    ) {
        if(isWishlistSelected){
            analytics.trackClickAddToWishlist(ChooseAddressUtils.getLocalizingAddressData(context).warehouse_id, productId)
        }
        else{
            analytics.trackClickRemoveFromWishlist(ChooseAddressUtils.getLocalizingAddressData(context).warehouse_id, productId)
        }
        showToaster(
            descriptionToaster,
            type,
            ctaToaster
        ) {
            ctaClickListener?.invoke()
        }

        viewModel.updateWishlistStatus(
            productId = productId,
            hasBeenWishlist = isWishlistSelected
        )
    }
}
