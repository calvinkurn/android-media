package com.tokopedia.officialstore.official.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

class OfficialHomeMapper {

    companion object {

        val BANNER_POSITION = 0
        val BENEFIT_POSITION = 1
        val FEATURE_SHOP_POSITION = 2

        fun mappingBanners(banner: OfficialStoreBanners, adapter: OfficialHomeAdapter?, categoryName: String?) {
            notifyElement(BANNER_POSITION, OfficialBannerViewModel(banner.banners,
                    categoryName.toEmptyStringIfNull()), adapter)
        }

        fun mappingBenefit(benefits: OfficialStoreBenefits, adapter: OfficialHomeAdapter?) {
            notifyElement(BENEFIT_POSITION, OfficialBenefitViewModel(benefits.benefits), adapter)
        }

        fun mappingFeaturedShop(featuredShop: OfficialStoreFeaturedShop, adapter: OfficialHomeAdapter?, categoryName: String?, listener: FeaturedShopListener) {
            notifyElement(
                    FEATURE_SHOP_POSITION,
                    OfficialFeaturedShopViewModel(
                            featuredShop.featuredShops,
                            featuredShop.header,
                            categoryName.toEmptyStringIfNull(),
                            listener
                    ),
                    adapter
            )
        }

        fun mappingDynamicChannel(dynamicChannel: DynamicChannel, adapter: OfficialHomeAdapter?, remoteConfig: RemoteConfig?) {
            if (dynamicChannel.channels.isNotEmpty()) {
                var availableScreens = setOf<String>()
                var availableLegoBannerScreens = setOf<String>()
                if (remoteConfig?.getBoolean(RemoteConfigKey.HOME_USE_GLOBAL_COMPONENT) == true) {
                    availableScreens = setOf(
                            DynamicChannelIdentifiers.LAYOUT_BANNER_CAROUSEL,
                            DynamicChannelIdentifiers.LAYOUT_SPRINT_LEGO,
                            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT,
                            DynamicChannelIdentifiers.LAYOUT_MIX_TOP
                    )
                    availableLegoBannerScreens = setOf(
                            DynamicChannelIdentifiers.LAYOUT_6_IMAGE,
                            DynamicChannelIdentifiers.LAYOUT_LEGO_3_IMAGE
                    )
                } else {
                    availableScreens = setOf(
                            DynamicChannelIdentifiers.LAYOUT_BANNER_CAROUSEL,
                            DynamicChannelIdentifiers.LAYOUT_SPRINT_LEGO,
                            DynamicChannelIdentifiers.LAYOUT_6_IMAGE,
                            DynamicChannelIdentifiers.LAYOUT_LEGO_3_IMAGE,
                            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT,
                            DynamicChannelIdentifiers.LAYOUT_MIX_TOP
                    )
                }

                val views = mutableListOf<Visitable<*>>()

                dynamicChannel.channels.forEachIndexed { position, channel ->
                    if (availableScreens.contains(channel.layout)) {
                        views.add(DynamicChannelViewModel(channel))
                    } else if (availableLegoBannerScreens.contains(channel.layout)) {
                        views.add(DynamicLegoBannerDataModel(
                                OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(channel, position)
                        ))
                    }
                }
                adapter?.getVisitables()?.addAll(views)
                adapter?.notifyItemInserted(adapter.lastIndex)
            }
        }

        fun mappingProductrecommendationTitle(title: String, adapter: OfficialHomeAdapter?) {
            adapter?.getVisitables()?.add(ProductRecommendationTitleViewModel(title))
            adapter?.notifyItemInserted(adapter.lastIndex)
        }

        fun mappingProductRecommendation(productRecommendation: RecommendationWidget, adapter: OfficialHomeAdapter?, listener: RecommendationListener) {
            productRecommendation.recommendationItemList.forEach {
                adapter?.getVisitables()?.add(ProductRecommendationViewModel(it, listener))
            }
            adapter?.notifyItemRangeInserted(adapter.lastIndex, productRecommendation.recommendationItemList.size)
        }

        fun notifyElement(position: Int, element: Visitable<*>, adapter: OfficialHomeAdapter?) {
            adapter?.getVisitables()?.set(position, element)
            adapter?.notifyItemChanged(position)
        }
    }
}
