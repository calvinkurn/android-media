package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView.ProductCardCompactListener
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TabAdapterFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryProductListViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder.CategoryQuickFilterListener
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowAdsCarouselTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductItemTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProgressBarTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTickerTypeFactory
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowAdsCarouselViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProgressBarViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTickerViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder

class CategoryL2TabAdapterTypeFactory(
    private var adsCarouselListener: ProductAdsCarouselListener?,
    private var quickFilterListener: CategoryQuickFilterListener?,
    private var productItemListener: ProductItemListener?,
    private var productCardCompactListener: ProductCardCompactListener?,
    private var similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener?,
): BaseAdapterTypeFactory(),
   TokoNowTickerTypeFactory,
   CategoryL2TabAdapterFactory,
   TokoNowAdsCarouselTypeFactory,
   TokoNowProductItemTypeFactory,
   TokoNowProgressBarTypeFactory
{
    // region TokoNow Common Component
    override fun type(uiModel: TokoNowAdsCarouselUiModel): Int = TokoNowAdsCarouselViewHolder.LAYOUT

    override fun type(uiModel: TokoNowProgressBarUiModel): Int = TokoNowProgressBarViewHolder.LAYOUT

    override fun type(uiModel: TokoNowTickerUiModel): Int = TokoNowTickerViewHolder.LAYOUT
    // endregion

    // region Category Component
    override fun type(uiModel: CategoryQuickFilterUiModel): Int = CategoryQuickFilterViewHolder.LAYOUT

    override fun type(uiModel: CategoryProductListUiModel): Int = CategoryProductListViewHolder.LAYOUT

    override fun type(productItemDataView: ProductItemDataView): Int = ProductItemViewHolder.LAYOUT
    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            // region TokoNow Common Component
            TokoNowAdsCarouselViewHolder.LAYOUT -> {
                TokoNowAdsCarouselViewHolder(
                    itemView = view,
                    listener = adsCarouselListener
                )
            }
            TokoNowProgressBarViewHolder.LAYOUT -> {
                TokoNowProgressBarViewHolder(view)
            }
            TokoNowTickerViewHolder.LAYOUT -> {
                TokoNowTickerViewHolder(view)
            }
            // endregion

            // region Category Component
            CategoryQuickFilterViewHolder.LAYOUT -> {
                CategoryQuickFilterViewHolder(
                    itemView = view,
                    listener = quickFilterListener
                )
            }
            ProductItemViewHolder.LAYOUT -> {
                ProductItemViewHolder(
                    itemView = view,
                    listener = productItemListener,
                    productCardCompactListener = productCardCompactListener,
                    productCardCompactSimilarProductTrackerListener = similarProductTrackerListener
                )
            }
            CategoryProductListViewHolder.LAYOUT -> {
                CategoryProductListViewHolder(view)
            }
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }

    fun onDestroy() {
        adsCarouselListener = null
        quickFilterListener = null
        productItemListener = null
        productCardCompactListener = null
        similarProductTrackerListener = null
    }
}
