package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.category.analytic.CategoryProductAdsAnalytic
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking

class CategoryProductCardAdsCallback(
    private val context: Context?,
    private val viewModel: TokoNowCategoryViewModel,
    private val analytic: CategoryProductAdsAnalytic,
    private val categoryIdL1: String,
    private val startActivityResult: (Intent, Int) -> Unit,
    private val showToasterWhenAddToCartBlocked: () -> Unit = {}
) : ProductAdsCarouselListener {

    override fun onProductCardClicked(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        RouteManager.route(context, product.appLink)
        analytic.trackProductClick(position, title, product)
    }

    override fun onProductCardImpressed(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        analytic.trackProductImpression(position, title, product)
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        viewModel.onCartQuantityChanged(
            product = product.productCardModel,
            shopId = product.shopId,
            quantity = quantity,
            layoutType = PRODUCT_ADS_CAROUSEL
        )
    }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        context?.apply {
            AtcVariantHelper.goToAtcVariant(
                context = this,
                productId = product.getProductId(),
                pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                isTokoNow = true,
                shopId = product.shopId,
                trackerCdListName = String.format(CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC, categoryIdL1),
                startActivitResult = startActivityResult
            )
        }
    }

    override fun onProductCardAddToCartBlocked() {
        showToasterWhenAddToCartBlocked.invoke()
    }
}
