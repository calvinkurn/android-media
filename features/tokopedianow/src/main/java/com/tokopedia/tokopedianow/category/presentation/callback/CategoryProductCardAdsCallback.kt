package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView.ProductCardCompactCarouselListener
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.category.analytic.CategoryProductAdsAnalytic
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking

class CategoryProductCardAdsCallback(
    private val context: Context?,
    private val viewModel: TokoNowCategoryViewModel,
    private val analytic: CategoryProductAdsAnalytic,
    private val categoryIdL1: String,
    private val startActivityResult: (Intent, Int) -> Unit
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
            productId = product.productCardModel.productId,
            quantity = quantity,
            stock = product.productCardModel.availableStock,
            shopId = product.shopId,
            position = position,
            isOos = product.productCardModel.isOos(),
            name = product.productCardModel.name,
            categoryIdL1 = categoryIdL1,
            price = product.productCardModel.price.getDigits().orZero(),
            headerName = product.headerName,
            layoutType = TokoNowStaticLayoutType.PRODUCT_ADS_CAROUSEL
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
}
