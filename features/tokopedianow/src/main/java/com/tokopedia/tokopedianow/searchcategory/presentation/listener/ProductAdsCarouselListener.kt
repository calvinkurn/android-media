package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.search.analytics.SearchTracking
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductAdsCarouselAnalytics
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView.Shop
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel

class ProductAdsCarouselListener(
    private val context: Context?,
    private val viewModel: BaseSearchCategoryViewModel,
    private val analytics: ProductAdsCarouselAnalytics,
    private val startActivityResult: (Intent, Int) -> Unit,
    private val showToasterWhenAddToCartBlocked: () -> Unit = {}
): ProductAdsCarouselListener {

    override fun onProductCardClicked(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        RouteManager.route(context, product.appLink)
        analytics.trackProductClick(position, title, product)
    }

    override fun onProductCardImpressed(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        analytics.trackProductImpression(position, title, product)
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        val productItem = ProductItemDataView(
            shop = Shop(product.shopId, product.shopName),
            productCardModel = product.productCardModel,
            widgetTitle = product.headerName,
            shopId = product.shopId,
            shopName = product.shopName,
            shopType = product.shopType,
            categoryBreadcrumbs = product.categoryBreadcrumbs,
            position = product.position,
            type = PRODUCT_ADS_CAROUSEL
        )
        viewModel.onViewATCProductNonVariant(productItem, quantity)
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
                trackerCdListName = SearchTracking.Misc.TOKONOW_SEARCH_PRODUCT_ATC_VARIANT,
                startActivitResult = startActivityResult
            )
        }
    }

    override fun onProductCardAddToCartBlocked() {
        showToasterWhenAddToCartBlocked.invoke()
    }
}
