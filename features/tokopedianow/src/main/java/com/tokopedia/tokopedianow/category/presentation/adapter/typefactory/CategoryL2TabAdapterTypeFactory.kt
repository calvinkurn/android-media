package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView.ProductCardCompactListener
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TabAdapterFactory
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateDivider
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryEmptyStateDividerViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryProductListViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder.CategoryQuickFilterListener
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowAdsCarouselTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateNoResultTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductItemTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductRecommendationTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProgressBarTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTickerTypeFactory
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowAdsCarouselViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProgressBarViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTickerViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTickerViewHolder.TokoNowTickerTrackerListener
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.TokoNowFeedbackWidgetTypeFactory
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener

class CategoryL2TabAdapterTypeFactory(
    private var adsCarouselListener: ProductAdsCarouselListener?,
    private var quickFilterListener: CategoryQuickFilterListener?,
    private var productItemListener: ProductItemListener?,
    private var productCardCompactListener: ProductCardCompactListener?,
    private var similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener?,
    private var emptyStateNoResultListener: TokoNowEmptyStateNoResultListener? = null,
    private var productRecommendationListener: TokoNowProductRecommendationListener? = null,
    private var categoryMenuListener: TokoNowCategoryMenuListener? = null,
    private var feedbackWidgetListener: FeedbackWidgetListener? = null,
    private var tickerTrackerListener: TokoNowTickerTrackerListener? = null
) : BaseAdapterTypeFactory(),
    TokoNowTickerTypeFactory,
    CategoryL2TabAdapterFactory,
    TokoNowAdsCarouselTypeFactory,
    TokoNowProductItemTypeFactory,
    TokoNowProgressBarTypeFactory,
    TokoNowEmptyStateNoResultTypeFactory,
    TokoNowProductRecommendationTypeFactory,
    TokoNowCategoryMenuTypeFactory,
    TokoNowFeedbackWidgetTypeFactory
{
    // region TokoNow Common Component
    override fun type(uiModel: TokoNowAdsCarouselUiModel): Int = TokoNowAdsCarouselViewHolder.LAYOUT

    override fun type(uiModel: TokoNowProgressBarUiModel): Int = TokoNowProgressBarViewHolder.LAYOUT

    override fun type(uiModel: TokoNowTickerUiModel): Int = TokoNowTickerViewHolder.LAYOUT

    override fun type(uiModel: TokoNowEmptyStateNoResultUiModel) = TokoNowEmptyStateNoResultViewHolder.LAYOUT

    override fun type(uiModel: TokoNowProductRecommendationUiModel): Int = TokoNowProductRecommendationViewHolder.LAYOUT

    override fun type(uiModel: TokoNowCategoryMenuUiModel): Int = TokoNowCategoryMenuViewHolder.LAYOUT

    override fun type(uiModel: TokoNowFeedbackWidgetUiModel): Int = TokoNowFeedbackWidgetViewHolder.LAYOUT
    // endregion

    // region Category Component
    override fun type(uiModel: CategoryQuickFilterUiModel): Int = CategoryQuickFilterViewHolder.LAYOUT

    override fun type(uiModel: CategoryProductListUiModel): Int = CategoryProductListViewHolder.LAYOUT

    override fun type(productItemDataView: ProductItemDataView): Int = ProductItemViewHolder.LAYOUT

    override fun type(uiModel: CategoryEmptyStateDivider): Int = CategoryEmptyStateDividerViewHolder.LAYOUT
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
                TokoNowTickerViewHolder(view, tickerTrackerListener)
            }
            TokoNowEmptyStateNoResultViewHolder.LAYOUT -> TokoNowEmptyStateNoResultViewHolder(
                itemView = view,
                tokoNowEmptyStateNoResultListener = emptyStateNoResultListener
            )
            TokoNowProductRecommendationViewHolder.LAYOUT -> TokoNowProductRecommendationViewHolder(
                itemView = view,
                listener = productRecommendationListener
            )
            TokoNowCategoryMenuViewHolder.LAYOUT -> TokoNowCategoryMenuViewHolder(
                itemView = view,
                listener = categoryMenuListener
            )
            TokoNowFeedbackWidgetViewHolder.LAYOUT -> TokoNowFeedbackWidgetViewHolder(
                itemView = view,
                listener = feedbackWidgetListener
            )
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
            CategoryProductListViewHolder.LAYOUT -> CategoryProductListViewHolder(view)
            CategoryEmptyStateDividerViewHolder.LAYOUT -> CategoryEmptyStateDividerViewHolder(view)
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
        emptyStateNoResultListener = null
        productRecommendationListener = null
        categoryMenuListener = null
        feedbackWidgetListener = null
        tickerTrackerListener = null
    }
}
