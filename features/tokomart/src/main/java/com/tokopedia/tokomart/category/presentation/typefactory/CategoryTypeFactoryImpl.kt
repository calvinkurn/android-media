package com.tokopedia.tokomart.category.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import com.tokopedia.tokomart.category.presentation.viewholder.CategoryIsleViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryTypeFactoryImpl
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.visitable.CategoryNavigationDataModel
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerSixAutoDataModel
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel

class CategoryTypeFactoryImpl(): BaseSearchCategoryTypeFactoryImpl(), CategoryTypeFactory, HomeComponentTypeFactory {

    override fun type(categoryIsleDataView: CategoryIsleDataView) = CategoryIsleViewHolder.LAYOUT

    override fun type(bannerDataModel: BannerDataModel) = BannerComponentViewHolder.LAYOUT

    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = 0

    override fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int = 0

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int = 0

    override fun type(reminderWidgetModel: ReminderWidgetModel): Int = 0

    override fun type(mixLeftDataModel: MixLeftDataModel): Int = 0

    override fun type(mixTopDataModel: MixTopDataModel): Int = 0

    override fun type(productHighlightDataModel: ProductHighlightDataModel): Int = 0

    override fun type(lego4AutoDataModel: Lego4AutoDataModel): Int = 0

    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int = 0

    override fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int = 0

    override fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int = 0

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            CategoryIsleViewHolder.LAYOUT -> CategoryIsleViewHolder(view)
            BannerComponentViewHolder.LAYOUT -> BannerComponentViewHolder(view, null, null)
            else -> super.createViewHolder(view, type)
        }
    }
}