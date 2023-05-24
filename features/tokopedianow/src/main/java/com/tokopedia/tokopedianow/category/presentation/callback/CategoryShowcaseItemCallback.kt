package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking

class CategoryShowcaseItemCallback(
    private val shopId: String,
    private val categoryIdL1: String,
    private val startActivityForResult: (Intent, Int) -> Unit,
    private val onAddToCartBlocked: () -> Unit,
    private val onCartQuantityChangedListener: (position: Int, product: CategoryShowcaseItemUiModel, quantity: Int) -> Unit
): CategoryShowcaseItemViewHolder.CategoryShowcaseItemListener {
    override fun onProductCardAddVariantClicked(
        context: Context,
        position: Int,
        product: CategoryShowcaseItemUiModel
    ) {
        AtcVariantHelper.goToAtcVariant(
            context = context,
            productId = product.productCardModel.productId,
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = shopId,
            trackerCdListName = String.format(CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC, categoryIdL1),
            startActivitResult = startActivityForResult
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: CategoryShowcaseItemUiModel,
        quantity: Int
    ) {
        onCartQuantityChangedListener(position, product, quantity)
    }

    override fun onProductCardClicked(
        context: Context,
        position: Int,
        product: CategoryShowcaseItemUiModel
    ) {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            product.parentProductId,
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: CategoryShowcaseItemUiModel
    ) { /* waiting for trackers */ }

    override fun onProductCardAddToCartBlocked() = onAddToCartBlocked()
}
