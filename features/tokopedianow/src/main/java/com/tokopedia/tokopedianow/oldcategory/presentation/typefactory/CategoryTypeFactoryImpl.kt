package com.tokopedia.tokopedianow.oldcategory.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder
import com.tokopedia.tokopedianow.common.viewholder.categorymenu.TokoNowCategoryMenuViewHolder.TokoNowCategoryMenuListener
import com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowProductCardViewHolder.TokoNowProductCardListener
import com.tokopedia.tokopedianow.common.viewholder.oldrepurchase.TokoNowRepurchaseViewHolder
import com.tokopedia.tokopedianow.oldcategory.presentation.listener.CategoryAisleListener
import com.tokopedia.tokopedianow.oldcategory.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.oldcategory.presentation.viewholder.CategoryAisleViewHolder
import com.tokopedia.tokopedianow.oldcategory.presentation.viewholder.CategoryChooseAddressViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactoryImpl
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder

class CategoryTypeFactoryImpl(
    tokoNowEmptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener,
    chooseAddressListener: ChooseAddressListener,
    titleListener: TitleListener,
    bannerListener: BannerComponentListener,
    quickFilterListener: QuickFilterListener,
    categoryFilterListener: CategoryFilterListener,
    productItemListener: ProductItemListener,
    productCardCompactSimilarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener,
    switcherWidgetListener: SwitcherWidgetListener,
    tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    productRecommendationOocBindListener: TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener,
    productRecommendationOocListener: TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener,
    productRecommendationListener: TokoNowProductRecommendationView.TokoNowProductRecommendationListener?,
    productCardCompactListener: ProductCardCompactView.ProductCardCompactListener,
    private val categoryAisleListener: CategoryAisleListener,
    private val tokoNowCategoryMenuListener: TokoNowCategoryMenuListener,
    private val tokoNowProductCardListener: TokoNowProductCardListener,
    feedbackWidgetListener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener
) : BaseSearchCategoryTypeFactoryImpl(
    tokoNowEmptyStateOocListener,
    chooseAddressListener,
    titleListener,
    bannerListener,
    quickFilterListener,
    categoryFilterListener,
    productItemListener,
    switcherWidgetListener,
    tokoNowEmptyStateNoResultListener,
    feedbackWidgetListener,
    productCardCompactListener,
    productCardCompactSimilarProductTrackerListener,
    productRecommendationOocBindListener,
    productRecommendationOocListener,
    productRecommendationListener
),
    CategoryTypeFactory {

    override fun type(categoryAisleDataView: CategoryAisleDataView) = CategoryAisleViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BaseChooseAddressViewHolder.LAYOUT ->
                CategoryChooseAddressViewHolder(view, chooseAddressListener)
            CategoryAisleViewHolder.LAYOUT ->
                CategoryAisleViewHolder(view, categoryAisleListener)
            TokoNowCategoryMenuViewHolder.LAYOUT ->
                TokoNowCategoryMenuViewHolder(view, tokoNowCategoryMenuListener)
            TokoNowRepurchaseViewHolder.LAYOUT ->
                TokoNowRepurchaseViewHolder(view, tokoNowProductCardListener)
            else -> super.createViewHolder(view, type)
        }
    }
}
