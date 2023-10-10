package com.tokopedia.thankyou_native.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetListener
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.model.*
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent.*
import com.tokopedia.thankyou_native.presentation.views.RegisterMemberShipListener
import com.tokopedia.thankyou_native.presentation.views.listener.BannerListener
import com.tokopedia.thankyou_native.presentation.views.listener.MarketplaceRecommendationListener

class BottomContentFactory(
    private val registerMemberShipListener: RegisterMemberShipListener,
    private val marketplaceRecommendationListener: MarketplaceRecommendationListener,
    private val bannerListener: BannerListener,
    private val mixTopComponentListener: MixTopComponentListener,
): BaseAdapterTypeFactory(), HomeComponentTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TopAdsItemViewHolder.LAYOUT_ID -> return TopAdsItemViewHolder(parent)
            GyroRecommendationItemViewHolder.LAYOUT_ID -> return GyroRecommendationItemViewHolder(parent, registerMemberShipListener)
            MarketplaceRecommendationItemViewHolder.LAYOUT_ID -> return MarketplaceRecommendationItemViewHolder(parent, marketplaceRecommendationListener)
            DigitalRecommendationItemViewHolder.LAYOUT_ID -> return DigitalRecommendationItemViewHolder(parent)
            HeadlineAdsItemViewHolder.LAYOUT_ID -> return HeadlineAdsItemViewHolder(parent)
            BannerItemViewHolder.LAYOUT_ID -> return BannerItemViewHolder(parent, bannerListener)
            MixTopComponentViewHolder.LAYOUT -> return MixTopComponentViewHolder(parent, null, mixTopComponentListener, true)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(topAdsRequestParams: TopAdsRequestParams): Int {
        return TopAdsItemViewHolder.LAYOUT_ID
    }

    fun type(gyroRecommendationWidgetModel: GyroRecommendationWidgetModel): Int {
        return GyroRecommendationItemViewHolder.LAYOUT_ID
    }

    fun type(marketplaceRecommendationWidgetModel: MarketplaceRecommendationWidgetModel): Int {
        return MarketplaceRecommendationItemViewHolder.LAYOUT_ID
    }

    fun type(digitalRecommendationWidgetModel: DigitalRecommendationWidgetModel): Int {
        return DigitalRecommendationItemViewHolder.LAYOUT_ID
    }

    fun type(headlineAdsWidgetModel: HeadlineAdsWidgetModel): Int {
        return HeadlineAdsItemViewHolder.LAYOUT_ID
    }

    fun type(bannerWidgetModel: BannerWidgetModel): Int {
        return BannerItemViewHolder.LAYOUT_ID
    }

    override fun type(flashSaleWidgetModel: ShopFlashSaleWidgetDataModel): Int {
        return ShopFlashSaleWidgetViewHolder.LAYOUT
    }

    override fun type(mixTopDataModel: MixTopDataModel): Int {
        return MixTopComponentViewHolder.LAYOUT
    }
}
