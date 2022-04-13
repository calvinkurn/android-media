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
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeFakeTabViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeIconsViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeLoadingViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeMerchantListViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeNoPinPoinViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeOutOfCoverageViewHolder
import com.tokopedia.tokofood.home.presentation.adapter.viewholder.TokoFoodHomeUSPViewHolder
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeFakeTabUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeMerchantListUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeNoPinPoinUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeOutOfCoverageUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeBannerComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.home.presentation.view.listener.TokoFoodHomeLegoComponentCallback

class TokoFoodHomeAdapterTypeFactory (
    private val dynamicLegoBannerCallback: TokoFoodHomeLegoComponentCallback? = null,
    private val bannerComponentCallback: TokoFoodHomeBannerComponentCallback? = null,
    private val categoryWidgetCallback: TokoFoodHomeCategoryWidgetV2ComponentCallback? = null
):  BaseAdapterTypeFactory(),
    TokoFoodHomeTypeFactory,
    HomeComponentTypeFactory {

    // region TokoFood Home Component
    override fun type(uiModel: TokoFoodHomeFakeTabUiModel): Int = TokoFoodHomeFakeTabViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeUSPUiModel): Int = TokoFoodHomeUSPViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeNoPinPoinUiModel): Int = TokoFoodHomeNoPinPoinViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeOutOfCoverageUiModel): Int = TokoFoodHomeOutOfCoverageViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeLoadingStateUiModel): Int = TokoFoodHomeLoadingViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeMerchantListUiModel): Int = TokoFoodHomeMerchantListViewHolder.LAYOUT
    override fun type(uiModel: TokoFoodHomeIconsUiModel): Int = TokoFoodHomeIconsViewHolder.LAYOUT
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
            TokoFoodHomeFakeTabViewHolder.LAYOUT -> TokoFoodHomeFakeTabViewHolder(view)
            TokoFoodHomeUSPViewHolder.LAYOUT -> TokoFoodHomeUSPViewHolder(view)
            TokoFoodHomeNoPinPoinViewHolder.LAYOUT -> TokoFoodHomeNoPinPoinViewHolder(view)
            TokoFoodHomeOutOfCoverageViewHolder.LAYOUT -> TokoFoodHomeOutOfCoverageViewHolder(view)
            TokoFoodHomeLoadingViewHolder.LAYOUT -> TokoFoodHomeLoadingViewHolder(view)
            TokoFoodHomeMerchantListViewHolder.LAYOUT -> TokoFoodHomeMerchantListViewHolder(view)
            TokoFoodHomeIconsViewHolder.LAYOUT -> TokoFoodHomeIconsViewHolder(view)
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