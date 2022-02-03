package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardCustomLastItemBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardsUnifyBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeRecommendationBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageBannerEmptyModel
import com.tokopedia.digital.home.model.RechargeHomepageBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageCarousellModel
import com.tokopedia.digital.home.model.RechargeHomepageCategoryModel
import com.tokopedia.digital.home.model.RechargeHomepageDualBannersModel
import com.tokopedia.digital.home.model.RechargeHomepageFavoriteModel
import com.tokopedia.digital.home.model.RechargeHomepageProductBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageProductCardCustomBannerV2Model
import com.tokopedia.digital.home.model.RechargeHomepageProductCardCustomLastItemModel
import com.tokopedia.digital.home.model.RechargeHomepageProductCardsModel
import com.tokopedia.digital.home.model.RechargeHomepageRecommendationBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSingleBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSwipeBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageThreeIconsModel
import com.tokopedia.digital.home.model.RechargeHomepageTrustMarkModel
import com.tokopedia.digital.home.model.RechargeHomepageVideoHighlightModel
import com.tokopedia.digital.home.model.RechargeProductCardCustomBannerModel
import com.tokopedia.digital.home.model.RechargeProductCardUnifyModel
import com.tokopedia.digital.home.model.RechargeTickerHomepageModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageBannerEmptyViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageCarousellViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageCategoryViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageDualBannersViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageDualIconsViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageFavoriteViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageLastItemViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageLoadingViewholder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageProductBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageProductCardCustomBannerV2ViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageProductCardCustomBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageProductCardCustomLastItemViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageProductCardUnifyViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageProductCardsViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageRecommendationBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageSingleBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageSwipeBannerViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageThreeIconsViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageTickerViewHolder
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageVideoHighlightViewHolder
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageDynamicLegoBannerCallback
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageReminderWidgetCallback
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
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

class RechargeHomepageAdapterTypeFactory(
    val listener: RechargeHomepageItemListener,
    private val reminderWidgetCallback: RechargeHomepageReminderWidgetCallback,
    private val dynamicLegoBannerCallback: RechargeHomepageDynamicLegoBannerCallback
) : BaseAdapterTypeFactory(), HomeComponentTypeFactory {

    fun type(bannerModel: RechargeHomepageBannerModel): Int {
        return RechargeHomepageBannerViewHolder.LAYOUT
    }

    fun type(emptyBannerModel: RechargeHomepageBannerEmptyModel): Int {
        return RechargeHomepageBannerEmptyViewHolder.LAYOUT
    }

    fun type(favoriteModel: RechargeHomepageFavoriteModel): Int {
        return RechargeHomepageFavoriteViewHolder.LAYOUT
    }

    fun type(categoryModel: RechargeHomepageCategoryModel): Int {
        return RechargeHomepageCategoryViewHolder.LAYOUT
    }

    fun type(trustMarkModel: RechargeHomepageTrustMarkModel): Int {
        return RechargeHomepageDualIconsViewHolder.LAYOUT
    }

    fun type(videoHighlightModel: RechargeHomepageVideoHighlightModel): Int {
        return RechargeHomepageVideoHighlightViewHolder.LAYOUT
    }

    fun type(singleBannerModel: RechargeHomepageSingleBannerModel): Int {
        return RechargeHomepageSingleBannerViewHolder.LAYOUT
    }

    fun type(dualBannersModel: RechargeHomepageDualBannersModel): Int {
        return RechargeHomepageDualBannersViewHolder.LAYOUT
    }

    fun type(productCardsModel: RechargeHomepageProductCardsModel): Int {
        return RechargeHomepageProductCardsViewHolder.LAYOUT
    }

    fun type(productBannerModel: RechargeHomepageProductBannerModel): Int {
        return RechargeHomepageProductBannerViewHolder.LAYOUT
    }

    fun type(productCardCustomModel: RechargeProductCardCustomBannerModel): Int {
        return RechargeHomepageProductCardCustomBannerViewHolder.LAYOUT
    }

    fun type(carousellModel: RechargeHomepageCarousellModel): Int {
        return RechargeHomepageCarousellViewHolder.LAYOUT
    }

    fun type(tickerModel: RechargeTickerHomepageModel): Int {
        return RechargeHomepageTickerViewHolder.LAYOUT
    }

    fun type(swipeBannerModel: RechargeHomepageSwipeBannerModel): Int {
        return RechargeHomepageSwipeBannerViewHolder.LAYOUT
    }

    fun type(productCardUnifyModel: RechargeProductCardUnifyModel): Int =
        RechargeHomepageProductCardUnifyViewHolder.LAYOUT

    fun type(threeIconsModel: RechargeHomepageThreeIconsModel): Int =
        RechargeHomepageThreeIconsViewHolder.LAYOUT

    fun type(recommendationBannerModel: RechargeHomepageRecommendationBannerModel): Int =
        RechargeHomepageRecommendationBannerViewHolder.LAYOUT

    fun type(productCardCustomBanner: RechargeHomepageProductCardCustomBannerV2Model): Int =
        RechargeHomepageProductCardCustomBannerV2ViewHolder.LAYOUT

    fun type(customLastItem: RechargeHomepageProductCardCustomLastItemModel): Int =
        RechargeHomepageProductCardCustomLastItemViewHolder.LAYOUT

    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int {
        return DynamicLegoBannerViewHolder.LAYOUT
    }

    override fun type(lego4AutoDataModel: Lego4AutoDataModel): Int {
        return 0
    }

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int {
        return 0
    }

    override fun type(reminderWidgetModel: ReminderWidgetModel): Int {
        return ReminderWidgetViewHolder.LAYOUT
    }

    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int {
        return 0
    }

    override fun type(mixLeftDataModel: MixLeftDataModel): Int {
        return 0
    }

    override fun type(mixTopDataModel: MixTopDataModel): Int {
        return 0
    }

    override fun type(productHighlightDataModel: ProductHighlightDataModel): Int {
        return 0
    }

    override fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int {
        return 0
    }

    override fun type(bannerDataModel: BannerDataModel): Int {
        return BannerComponentViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return RechargeHomepageLoadingViewholder.LAYOUT
    }

    override fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int {
        return -1
    }

    override fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int {
        return -1
    }

    override fun type(featuredBrandDataModel: FeaturedBrandDataModel): Int {
        return 0
    }

    override fun type(questWidgetModel: QuestWidgetModel): Int {
        return -1
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RechargeHomepageLoadingViewholder.LAYOUT -> RechargeHomepageLoadingViewholder(parent)
            // Home Components
            ReminderWidgetViewHolder.LAYOUT -> ReminderWidgetViewHolder(
                parent,
                reminderWidgetCallback
            )
            DynamicLegoBannerViewHolder.LAYOUT -> DynamicLegoBannerViewHolder(
                parent,
                dynamicLegoBannerCallback,
                dynamicLegoBannerCallback
            )
            // Recharge
            RechargeHomepageFavoriteViewHolder.LAYOUT -> RechargeHomepageFavoriteViewHolder(
                parent,
                listener
            )
            RechargeHomepageCategoryViewHolder.LAYOUT -> RechargeHomepageCategoryViewHolder(
                parent,
                listener
            )
            RechargeHomepageDualIconsViewHolder.LAYOUT -> RechargeHomepageDualIconsViewHolder(
                parent,
                listener
            )
            RechargeHomepageBannerViewHolder.LAYOUT -> RechargeHomepageBannerViewHolder(
                parent,
                listener
            )
            RechargeHomepageBannerEmptyViewHolder.LAYOUT -> RechargeHomepageBannerEmptyViewHolder(
                parent,
                listener
            )
            RechargeHomepageVideoHighlightViewHolder.LAYOUT -> RechargeHomepageVideoHighlightViewHolder(
                parent,
                listener
            )
            RechargeHomepageSingleBannerViewHolder.LAYOUT -> RechargeHomepageSingleBannerViewHolder(
                parent,
                listener
            )
            RechargeHomepageDualBannersViewHolder.LAYOUT -> RechargeHomepageDualBannersViewHolder(
                parent,
                listener
            )
            RechargeHomepageProductCardsViewHolder.LAYOUT -> RechargeHomepageProductCardsViewHolder(
                parent,
                listener
            )
            RechargeHomepageProductBannerViewHolder.LAYOUT -> RechargeHomepageProductBannerViewHolder(
                parent,
                listener
            )
            RechargeHomepageProductCardCustomBannerViewHolder.LAYOUT -> RechargeHomepageProductCardCustomBannerViewHolder(
                parent,
                listener
            )
            RechargeHomepageCarousellViewHolder.LAYOUT -> RechargeHomepageCarousellViewHolder(
                parent,
                listener
            )
            RechargeHomepageTickerViewHolder.LAYOUT -> RechargeHomepageTickerViewHolder(
                parent,
                listener
            )
            RechargeHomepageSwipeBannerViewHolder.LAYOUT -> RechargeHomepageSwipeBannerViewHolder(
                parent,
                listener
            )
            RechargeHomepageProductCardUnifyViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeProductCardsUnifyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                RechargeHomepageProductCardUnifyViewHolder(binding, listener)
            }
            RechargeHomepageThreeIconsViewHolder.LAYOUT -> RechargeHomepageThreeIconsViewHolder(
                parent,
                listener
            )
            RechargeHomepageProductCardCustomBannerV2ViewHolder.LAYOUT -> RechargeHomepageProductCardCustomBannerV2ViewHolder(
                parent,
                listener
            )
            RechargeHomepageRecommendationBannerViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeRecommendationBannerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )

                RechargeHomepageRecommendationBannerViewHolder(binding, listener)
            }
            RechargeHomepageProductCardCustomLastItemViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeProductCardCustomLastItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                RechargeHomepageProductCardCustomLastItemViewHolder(binding, listener)
            }
            else -> super.createViewHolder(parent, type)
        }
    }

}
