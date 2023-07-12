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
    private val onClickProductCard: (appLink: String, headerName: String, index: Int, productId: String, productName: String, productPrice: String, isOos: Boolean) -> Unit,
    private val onImpressProductCard: (headerName: String, index: Int, productId: String, productName: String, productPrice: String, isOos: Boolean) -> Unit,
    private val onAddToCartBlocked: () -> Unit,
    private val onProductCartQuantityChanged: (product: CategoryShowcaseItemUiModel, quantity: Int) -> Unit,
    private val startActivityForResult: (Intent, Int) -> Unit,
    private val onWishlistButtonClicked: (productId: String, isWishlistSelected: Boolean, descriptionToaster: String, ctaToaster: String, type: Int, ctaClickListener: (() -> Unit)?) -> Unit
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
        onProductCartQuantityChanged(product, quantity)
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
        onClickProductCard(
            appLink,
            product.headerName,
            position,
            product.productCardModel.productId,
            product.productCardModel.name,
            product.productCardModel.price,
            product.productCardModel.isOos()
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: CategoryShowcaseItemUiModel
    ) = onImpressProductCard(
            product.headerName,
            position,
            product.productCardModel.productId,
            product.productCardModel.name,
            product.productCardModel.price,
            product.productCardModel.isOos()
    )

    override fun onProductCardAddToCartBlocked() = onAddToCartBlocked()

    override fun onWishlistButtonClicked(
        productId: String,
        isWishlistSelected: Boolean,
        descriptionToaster: String,
        ctaToaster: String,
        type: Int,
        ctaClickListener: (() -> Unit)?
    ) {
        onWishlistButtonClicked.invoke(
            productId,
            isWishlistSelected,
            descriptionToaster,
            ctaToaster,
            type,
            ctaClickListener
        )
    }
}
