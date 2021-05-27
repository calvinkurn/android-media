package com.tokopedia.tokomart.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.CategoryNavigationViewHolder
import com.tokopedia.home_component.viewholders.DynamicIconViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerSixAutoViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.viewholders.Lego4AutoBannerViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.viewholders.ProductHighlightComponentViewHolder
import com.tokopedia.home_component.viewholders.RecommendationListCarouselViewHolder
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
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
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryItemUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel
import com.tokopedia.tokomart.home.presentation.view.listener.TokoMartDynamicLegoBannerCallback
import com.tokopedia.tokomart.home.presentation.viewholder.HomeCategoryItemViewHolder
import com.tokopedia.tokomart.home.presentation.viewholder.HomeCategoryGridViewHolder
import com.tokopedia.tokomart.home.presentation.viewholder.HomeSectionViewHolder

class TokoMartHomeAdapterTypeFactory: BaseAdapterTypeFactory(), TokoMartHomeTypeFactory, HomeComponentTypeFactory {

    // region Toko Mart Home Component
    override fun type(uiModel: HomeSectionUiModel): Int = HomeSectionViewHolder.LAYOUT
    override fun type(uiModel: HomeCategoryGridUiModel): Int = HomeCategoryGridViewHolder.LAYOUT
    override fun type(uiModel: HomeCategoryItemUiModel): Int = HomeCategoryItemViewHolder.LAYOUT
    // endregion

    // region Global Home Component
    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = DynamicLegoBannerViewHolder.LAYOUT
    override fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int = DynamicLegoBannerSixAutoViewHolder.LAYOUT
    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int = RecommendationListCarouselViewHolder.LAYOUT
    override fun type(reminderWidgetModel: ReminderWidgetModel): Int = ReminderWidgetViewHolder.LAYOUT
    override fun type(mixLeftDataModel: MixLeftDataModel): Int = MixLeftComponentViewHolder.LAYOUT
    override fun type(mixTopDataModel: MixTopDataModel): Int = MixTopComponentViewHolder.LAYOUT
    override fun type(productHighlightDataModel: ProductHighlightDataModel): Int = ProductHighlightComponentViewHolder.LAYOUT
    override fun type(lego4AutoDataModel: Lego4AutoDataModel) = Lego4AutoBannerViewHolder.LAYOUT
    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int = FeaturedShopViewHolder.LAYOUT
    override fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int = CategoryNavigationViewHolder.LAYOUT
    override fun type(bannerDataModel: BannerDataModel): Int = BannerComponentViewHolder.LAYOUT
    override fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int = DynamicIconViewHolder.LAYOUT
    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            // region Toko Mart Home Component
            HomeSectionViewHolder.LAYOUT -> HomeSectionViewHolder(view)
            HomeCategoryGridViewHolder.LAYOUT -> HomeCategoryGridViewHolder(view)
            HomeCategoryItemViewHolder.LAYOUT -> HomeCategoryItemViewHolder(view)
            // endregion

            // region Global Home Component
            DynamicLegoBannerViewHolder.LAYOUT -> {
                val listener = TokoMartDynamicLegoBannerCallback(view.context)
                DynamicLegoBannerViewHolder(view, listener, null)
            }
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }
}