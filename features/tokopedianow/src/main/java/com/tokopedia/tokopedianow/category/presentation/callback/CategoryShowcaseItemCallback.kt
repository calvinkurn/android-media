package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder

class CategoryShowcaseItemCallback(
    private val context: Context?,
    private val shopId: String,
    private val startActivityForResult: (Intent, Int) -> Unit,
    private val onCartQuantityChangedListener: (position: Int, product: CategoryShowcaseItemUiModel, quantity: Int) -> Unit
): CategoryShowcaseItemViewHolder.CategoryShowcaseItemListener {
    override fun onProductCardAddVariantClicked(
        position: Int,
        product: CategoryShowcaseItemUiModel
    ) {
        context?.apply {
            AtcVariantHelper.goToAtcVariant(
                context = this,
                productId = product.productCardModel.productId,
                pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                isTokoNow = true,
                shopId = shopId,
                trackerCdListName = "categoryIdLv1",
                startActivitResult = startActivityForResult
            )
        }
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: CategoryShowcaseItemUiModel,
        quantity: Int
    ) {
        onCartQuantityChangedListener(position, product, quantity)
    }

    override fun onProductCardClicked(position: Int, product: CategoryShowcaseItemUiModel) {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            product.parentProductId,
        )
    }

    override fun onProductCardImpressed(position: Int, product: CategoryShowcaseItemUiModel) {

    }
}
