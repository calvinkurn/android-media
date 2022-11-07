package com.tokopedia.tokopedianow.category.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryAisleViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryChooseAddressViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRepurchaseViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardViewHolder.TokoNowProductCardListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRecommendationCarouselViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
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
    switcherWidgetListener: SwitcherWidgetListener,
    tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    private val categoryAisleListener: CategoryAisleListener,
    recommendationCarouselListener: TokoNowRecommendationCarouselViewHolder.TokoNowRecommendationCarouselListener,
    private val tokoNowCategoryGridListener: TokoNowCategoryGridListener,
    private val tokoNowProductCardListener: TokoNowProductCardListener,
    private val recomWidgetBindPageNameListener: TokoNowRecommendationCarouselViewHolder.TokonowRecomBindPageNameListener?,
    feedbackWidgetListener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener
): BaseSearchCategoryTypeFactoryImpl(
        tokoNowEmptyStateOocListener,
        chooseAddressListener,
        titleListener,
        bannerListener,
        quickFilterListener,
        categoryFilterListener,
        productItemListener,
        switcherWidgetListener,
        tokoNowEmptyStateNoResultListener,
        recommendationCarouselListener,
        recomWidgetBindPageNameListener,
        feedbackWidgetListener
), CategoryTypeFactory {

    override fun type(categoryAisleDataView: CategoryAisleDataView) = CategoryAisleViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BaseChooseAddressViewHolder.LAYOUT ->
                CategoryChooseAddressViewHolder(view, chooseAddressListener)
            CategoryAisleViewHolder.LAYOUT ->
                CategoryAisleViewHolder(view, categoryAisleListener)
            TokoNowCategoryGridViewHolder.LAYOUT ->
                TokoNowCategoryGridViewHolder(view, tokoNowCategoryGridListener)
            TokoNowRepurchaseViewHolder.LAYOUT ->
                TokoNowRepurchaseViewHolder(view, tokoNowProductCardListener)
            else -> super.createViewHolder(view, type)
        }
    }
}
