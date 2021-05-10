package com.tokopedia.tokomart.searchcategory.presentation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
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
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.BannerViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ChooseAddressViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ProductCountViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.QuickFilterViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.TitleViewHolder

abstract class BaseSearchCategoryTypeFactoryImpl(

): BaseAdapterTypeFactory(), BaseSearchCategoryTypeFactory, HomeComponentTypeFactory {

    override fun type(chooseAddressDataView: ChooseAddressDataView) = ChooseAddressViewHolder.LAYOUT

    override fun type(bannerDataView: BannerDataView) = BannerViewHolder.LAYOUT

    override fun type(titleDataView: TitleDataView) = TitleViewHolder.LAYOUT

    override fun type(quickFilterDataView: QuickFilterDataView) = QuickFilterViewHolder.LAYOUT

    override fun type(productCountDataView: ProductCountDataView) = ProductCountViewHolder.LAYOUT

    override fun type(productItemDataView: ProductItemDataView) = ProductItemViewHolder.LAYOUT

    // Home Component Section

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
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(view)
            ChooseAddressViewHolder.LAYOUT -> ChooseAddressViewHolder(view)
            BannerViewHolder.LAYOUT -> BannerViewHolder(view)
            TitleViewHolder.LAYOUT -> TitleViewHolder(view)
            QuickFilterViewHolder.LAYOUT -> QuickFilterViewHolder(view)
            ProductCountViewHolder.LAYOUT -> ProductCountViewHolder(view)
            BannerComponentViewHolder.LAYOUT -> BannerComponentViewHolder(view, null, null)
            else -> super.createViewHolder(view, type)
        }
    }
}