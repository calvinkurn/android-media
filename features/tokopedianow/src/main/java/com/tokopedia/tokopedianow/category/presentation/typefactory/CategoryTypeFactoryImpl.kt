package com.tokopedia.tokopedianow.category.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.listener.CategoryAisleListener
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryAisleViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryChooseAddressViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener
import com.tokopedia.tokopedianow.common.viewholder.*
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.*
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactoryImpl
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder

class CategoryTypeFactoryImpl(
    chooseAddressListener: ChooseAddressListener,
    titleListener: TitleListener,
    bannerListener: BannerComponentListener,
    quickFilterListener: QuickFilterListener,
    categoryFilterListener: CategoryFilterListener,
    productItemListener: ProductItemListener,
    tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    private val categoryAisleListener: CategoryAisleListener,
    outOfCoverageListener: OutOfCoverageListener,
    recommendationCarouselListener: TokoNowRecommendationCarouselViewHolder.TokoNowRecommendationCarouselListener,
    private val tokoNowCategoryGridListener: TokoNowCategoryGridListener,
): BaseSearchCategoryTypeFactoryImpl(
        chooseAddressListener,
        titleListener,
        bannerListener,
        quickFilterListener,
        categoryFilterListener,
        productItemListener,
        tokoNowEmptyStateNoResultListener,
        outOfCoverageListener,
        recommendationCarouselListener
), CategoryTypeFactory {

    override fun type(categoryAisleDataView: CategoryAisleDataView) = CategoryAisleViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BaseChooseAddressViewHolder.LAYOUT -> CategoryChooseAddressViewHolder(view, chooseAddressListener)
            CategoryAisleViewHolder.LAYOUT -> CategoryAisleViewHolder(view, categoryAisleListener)
            TokoNowCategoryGridViewHolder.LAYOUT -> TokoNowCategoryGridViewHolder(view, tokoNowCategoryGridListener)
            else -> super.createViewHolder(view, type)
        }
    }
}