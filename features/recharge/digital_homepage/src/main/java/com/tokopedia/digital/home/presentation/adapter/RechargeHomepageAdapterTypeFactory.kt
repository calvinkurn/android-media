package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageDynamicLegoBannerCallback
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageReminderWidgetCallback
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.ReminderWidgetViewHolder
import com.tokopedia.home_component.visitable.*

class RechargeHomepageAdapterTypeFactory(
        val listener: RechargeHomepageItemListener,
        private val reminderWidgetCallback: RechargeHomepageReminderWidgetCallback,
        private val dynamicLegoBannerCallback: RechargeHomepageDynamicLegoBannerCallback)
    : BaseAdapterTypeFactory(), HomeComponentTypeFactory {

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

    override fun type(featuredShopDataModel: FeaturedShopDataModel): Int{
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

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RechargeHomepageLoadingViewholder.LAYOUT -> RechargeHomepageLoadingViewholder(parent)
            // Home Components
            ReminderWidgetViewHolder.LAYOUT -> ReminderWidgetViewHolder(parent, reminderWidgetCallback)
            DynamicLegoBannerViewHolder.LAYOUT -> DynamicLegoBannerViewHolder(parent, dynamicLegoBannerCallback, dynamicLegoBannerCallback)
            // Recharge
            RechargeHomepageFavoriteViewHolder.LAYOUT -> RechargeHomepageFavoriteViewHolder(parent, listener)
            RechargeHomepageCategoryViewHolder.LAYOUT -> RechargeHomepageCategoryViewHolder(parent, listener)
            RechargeHomepageDualIconsViewHolder.LAYOUT -> RechargeHomepageDualIconsViewHolder(parent, listener)
            RechargeHomepageBannerViewHolder.LAYOUT -> RechargeHomepageBannerViewHolder(parent, listener)
            RechargeHomepageBannerEmptyViewHolder.LAYOUT -> RechargeHomepageBannerEmptyViewHolder(parent, listener)
            RechargeHomepageVideoHighlightViewHolder.LAYOUT -> RechargeHomepageVideoHighlightViewHolder(parent, listener)
            RechargeHomepageSingleBannerViewHolder.LAYOUT -> RechargeHomepageSingleBannerViewHolder(parent, listener)
            RechargeHomepageDualBannersViewHolder.LAYOUT -> RechargeHomepageDualBannersViewHolder(parent, listener)
            RechargeHomepageProductCardsViewHolder.LAYOUT -> RechargeHomepageProductCardsViewHolder(parent, listener)
            RechargeHomepageProductBannerViewHolder.LAYOUT -> RechargeHomepageProductBannerViewHolder(parent, listener)
            RechargeHomepageProductCardCustomBannerViewHolder.LAYOUT -> RechargeHomepageProductCardCustomBannerViewHolder(parent, listener)
            RechargeHomepageCarousellViewHolder.LAYOUT -> RechargeHomepageCarousellViewHolder(parent, listener)
            RechargeHomepageTickerViewHolder.LAYOUT -> RechargeHomepageTickerViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
