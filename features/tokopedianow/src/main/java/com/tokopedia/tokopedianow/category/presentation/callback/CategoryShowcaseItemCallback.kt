package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryShowcaseItemViewHolder
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking

class CategoryShowcaseItemCallback(
    private val shopId: String,
    private val categoryIdL1: String,
    private val onClickProductCard: (appLink: String) -> Unit,
    private val onAddToCartBlocked: () -> Unit,
    private val onProductCartQuantityChanged: (position: Int, product: CategoryShowcaseItemUiModel, quantity: Int) -> Unit,
    private val startActivityForResult: (Intent, Int) -> Unit,
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
        onProductCartQuantityChanged(
            position,
            product,
            quantity
        )
    }

    override fun onProductCardClicked(
        context: Context,
        position: Int,
        product: CategoryShowcaseItemUiModel
    ) {
        val appLink = UriUtil.buildUri(
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            product.parentProductId
        )
        onClickProductCard(appLink)
    }

    override fun onProductCardImpressed(
        position: Int,
        product: CategoryShowcaseItemUiModel
    ) { /* waiting for trackers */ }

    override fun onProductCardAddToCartBlocked() = onAddToCartBlocked()
}
