package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.HomeLoadingMoreModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.HomeRecommendationFeedDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.listener.DtHomeCategoryListener
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeLoadingStateUiModel
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.HomeLoadingMoreViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.HomeLoadingStateViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.recomendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.CategoryNavigationViewHolder
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

/**
 * Created by irpan on 12/09/22.
 */
class DtHomeAdapterTypeFactory(
    private val homeRecommendationFeedListener: DtHomeCategoryListener,
    private val featuredShopListener: FeaturedShopListener,
    private val bannerComponentListener: BannerComponentListener? = null,
    private val dynamicLegoBannerCallback: DynamicLegoBannerListener? = null,
    private val homeTopComponentListener: HomeComponentListener? = null,
    private val homeTopCarouselListener: MixTopComponentListener? = null,
    private val homeLeftCarouselListener: MixLeftComponentListener? = null
) : BaseAdapterTypeFactory(), HomeTypeFactory, HomeComponentTypeFactory {

    // region Global Home Component
    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = DynamicLegoBannerViewHolder.LAYOUT
    override fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int =
        DynamicLegoBannerSixAutoViewHolder.LAYOUT

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int =
        RecommendationListCarouselViewHolder.LAYOUT

    override fun type(reminderWidgetModel: ReminderWidgetModel): Int = ReminderWidgetViewHolder.LAYOUT
    override fun type(mixTopDataModel: MixTopDataModel): Int = MixTopComponentViewHolder.LAYOUT
    override fun type(productHighlightDataModel: ProductHighlightDataModel): Int = ProductHighlightComponentViewHolder.LAYOUT
    override fun type(lego4AutoDataModel: Lego4AutoDataModel) = Lego4AutoBannerViewHolder.LAYOUT
    override fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int = CategoryNavigationViewHolder.LAYOUT
    override fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int = DynamicIconViewHolder.LAYOUT
    override fun type(featuredBrandDataModel: FeaturedBrandDataModel): Int = FeaturedBrandViewHolder.LAYOUT
    override fun type(questWidgetModel: QuestWidgetModel): Int = QuestWidgetViewHolder.LAYOUT

    // current used in DT
    override fun type(bannerDataModel: BannerDataModel): Int = BannerComponentViewHolder.LAYOUT
    override fun type(mixLeftDataModel: MixLeftDataModel): Int = MixLeftComponentViewHolder.LAYOUT
    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int = FeaturedShopViewHolder.LAYOUT

    // loading from FE.
    override fun type(uiModel: HomeLoadingStateUiModel): Int = HomeLoadingStateViewHolder.LAYOUT
    override fun type(uiModel: HomeRecommendationFeedDataModel): Int = HomeRecommendationFeedViewHolder.LAYOUT
    override fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int = HomeLoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HomeLoadingStateViewHolder.LAYOUT -> HomeLoadingStateViewHolder(view)

            // loading more for recommendation feeds.
            HomeLoadingMoreViewHolder.LAYOUT -> HomeLoadingMoreViewHolder(view)

            HomeRecommendationFeedViewHolder.LAYOUT -> {
                HomeRecommendationFeedViewHolder(view, homeRecommendationFeedListener, cardInteraction = true)
            }

            BannerComponentViewHolder.LAYOUT -> {
                BannerComponentViewHolder(view, bannerComponentListener, null)
            }

            DynamicLegoBannerViewHolder.LAYOUT -> {
                DynamicLegoBannerViewHolder(view, dynamicLegoBannerCallback, null)
            }

            MixLeftComponentViewHolder.LAYOUT -> MixLeftComponentViewHolder(view, homeLeftCarouselListener, null)

            FeaturedShopViewHolder.LAYOUT -> FeaturedShopViewHolder(view, featuredShopListener, null)

            MixTopComponentViewHolder.LAYOUT -> MixTopComponentViewHolder(
                view,
                homeTopComponentListener,
                homeTopCarouselListener,
                true
            )

            // endregion
            else -> {
                super.createViewHolder(view, type)
            }
        }
    }
}
