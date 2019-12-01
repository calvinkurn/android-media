package com.tokopedia.officialstore.official.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

class OfficialHomeMapper {

    companion object {

        val BANNER_POSITION = 0
        val BENEFIT_POSITION = 1
        val FEATURE_SHOP_POSITION = 2
        val loadingModel: LoadingModel? = null

        fun mappingBanners(banner: OfficialStoreBanners, adapter: OfficialHomeAdapter?, categoryName: String?) {
            notifyElement(BANNER_POSITION, OfficialBannerViewModel(banner.banners,
                    categoryName.toEmptyStringIfNull()), adapter)
        }

        fun mappingBenefit(benefits: OfficialStoreBenefits, adapter: OfficialHomeAdapter?) {
            notifyElement(BENEFIT_POSITION, OfficialBenefitViewModel(benefits.benefits), adapter)
        }

        fun mappingFeaturedShop(featuredShop: OfficialStoreFeaturedShop, adapter: OfficialHomeAdapter?, categoryName: String?) {
            notifyElement(FEATURE_SHOP_POSITION,
                    OfficialFeaturedShopViewModel(featuredShop.featuredShops, featuredShop.header,
                    categoryName.toEmptyStringIfNull()), adapter)
        }

        fun mappingDynamicChannel(dynamicChannel: DynamicChannel, adapter: OfficialHomeAdapter?) {
            if (dynamicChannel.channels.isNotEmpty()) {
                val availableScreens = setOf(
                        DynamicChannelIdentifiers.LAYOUT_BANNER_CAROUSEL,
                        DynamicChannelIdentifiers.LAYOUT_SPRINT_LEGO,
                        DynamicChannelIdentifiers.LAYOUT_6_IMAGE,
                        DynamicChannelIdentifiers.LAYOUT_LEGO_3_IMAGE
                )
                val views = mutableListOf<Visitable<OfficialHomeAdapterTypeFactory>>()

                dynamicChannel.channels.forEach { channel ->
                    if (availableScreens.contains(channel.layout)) {
                        views.add(DynamicChannelViewModel(channel))
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

//        fun mappingLoadingBanner(adapter: OfficialHomeAdapter?) {
//            loadingModel?.let {
//                adapter?.getVisitables()?.set(BANNER_POSITION, loadingModel)
//                adapter?.notifyItemChanged(BANNER_POSITION)
//            }
////            notifyElement(BANNER_POSITION, , adapter)
//        }

        fun notifyElement(position: Int, element: Visitable<*>, adapter: OfficialHomeAdapter?) {
            adapter?.getVisitables()?.set(position, element)
            adapter?.notifyItemChanged(position)
        }
    }
}
