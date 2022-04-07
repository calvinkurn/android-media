package com.tokopedia.tokofood.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.CategoryNavigationViewHolder
import com.tokopedia.home_component.viewholders.CategoryWidgetV2ViewHolder
import com.tokopedia.home_component.viewholders.DynamicIconViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerSixAutoViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.FeaturedBrandViewHolder
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.viewholders.Lego4AutoBannerViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.viewholders.ProductHighlightComponentViewHolder
import com.tokopedia.home_component.viewholders.QuestWidgetViewHolder
import com.tokopedia.home_component.viewholders.RecommendationListCarouselViewHolder
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.CategoryNavigationDataModel
import com.tokopedia.home_component.visitable.CategoryWidgetV2DataModel
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerSixAutoDataModel
import com.tokopedia.home_component.visitable.FeaturedBrandDataModel
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodFakeTabViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodIconsViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodLoadingViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodMerchantListViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodNoPinPoinViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodOutOfCoverageViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodUSPViewHolder
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodFakeTabUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodNoPinPoinUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodOutOfCoverageUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodUSPUiModel
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodBannerComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodLegoComponentCallback

class TokoFoodHomeAdapterTypeFactory (
    private val dynamicLegoBannerCallback: TokoFoodLegoComponentCallback? = null,
    private val bannerComponentCallback: TokoFoodBannerComponentCallback? = null,
    private val categoryWidgetCallback: TokoFoodCategoryWidgetV2ComponentCallback? = null
):  BaseAdapterTypeFactory(),
    TokoFoodHomeTypeFactory,
    HomeComponentTypeFactory {

    // region TokoFood Home Component
    override fun type(uiModel: TokoFoodFakeTabUiModel): Int = TokoFoodFakeTabViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodUSPUiModel): Int = TokoFoodUSPViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodNoPinPoinUiModel): Int = TokoFoodNoPinPoinViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodOutOfCoverageUiModel): Int = TokoFoodOutOfCoverageViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodLoadingStateUiModel): Int = TokoFoodLoadingViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodMerchantListUiModel): Int = TokoFoodMerchantListViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodIconsUiModel): Int = TokoFoodIconsViewHolder.LAYOUT
    // endregion


    // region Global Home Component
    override fun type(categoryWidgetV2DataModel: CategoryWidgetV2DataModel): Int = CategoryWidgetV2ViewHolder.LAYOUT
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
    override fun type(featuredBrandDataModel: FeaturedBrandDataModel): Int = FeaturedBrandViewHolder.LAYOUT
    override fun type(questWidgetModel: QuestWidgetModel): Int = QuestWidgetViewHolder.LAYOUT
    // endregion

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){

            // region TokoFood Home Component
            TokoFoodFakeTabViewHolder.LAYOUT -> TokoFoodFakeTabViewHolder(view)
            TokoFoodUSPViewHolder.LAYOUT -> TokoFoodUSPViewHolder(view)
            TokoFoodNoPinPoinViewHolder.LAYOUT -> TokoFoodNoPinPoinViewHolder(view)
            TokoFoodOutOfCoverageViewHolder.LAYOUT -> TokoFoodOutOfCoverageViewHolder(view)
            TokoFoodLoadingViewHolder.LAYOUT -> TokoFoodLoadingViewHolder(view)
            TokoFoodMerchantListViewHolder.LAYOUT -> TokoFoodMerchantListViewHolder(view)
            TokoFoodIconsViewHolder.LAYOUT -> TokoFoodIconsViewHolder(view)
            // endregion

            // region Global Home Component
            DynamicLegoBannerViewHolder.LAYOUT -> {
                DynamicLegoBannerViewHolder(view, dynamicLegoBannerCallback, null)
            }
            BannerComponentViewHolder.LAYOUT -> {
                BannerComponentViewHolder(view, bannerComponentCallback, null)
            }
            CategoryWidgetV2ViewHolder.LAYOUT -> {
                CategoryWidgetV2ViewHolder(view, categoryWidgetCallback)
            }
            // endregion
            else -> super.createViewHolder(view, type)
        }
    }


}