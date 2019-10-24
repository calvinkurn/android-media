package com.tokopedia.officialstore.official.data.mapper

import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

class OfficialHomeMapper {

    companion object {

        fun mappingBanners(banner: OfficialStoreBanners, adapter: OfficialHomeAdapter?) {
            adapter?.addElement(0, OfficialBannerViewModel(banner.banners))
            adapter?.notifyItemInserted(0)
        }

        fun mappingBenefit(benefits: OfficialStoreBenefits, adapter: OfficialHomeAdapter?) {
            if (benefits.benefits.size > 0) {
                /**
                 * This component are not contains in Figma & Zeplin, so I just commented this code in case it needed you can uncomment :)
                 */
                val position = if (adapter?.itemCount?:0 >= 1 ) 1 else 0
                adapter?.addElement(position, OfficialBenefitViewModel(benefits.benefits))
                adapter?.notifyItemInserted(position)
            }
        }

        fun mappingFeaturedShop(featuredShop: OfficialStoreFeaturedShop, adapter: OfficialHomeAdapter?) {
            if (featuredShop.featuredShops.size > 0) {
                val position = if (adapter?.itemCount?:0 >= 2 ) 2 else 1
                adapter?.addElement(position, OfficialFeaturedShopViewModel(featuredShop.featuredShops, featuredShop.header))
                adapter?.notifyItemInserted(position)
            }
        }

        fun mappingDynamicChannel(dynamicChannel: DynamicChannel, adapter: OfficialHomeAdapter?) {
            /**
             * i just wondering what if the server not return featured shop
             * so I commented this condition, because we have to show dynamic channel whether the featured shop shows or not
              */
//            if (adapter?.getDataByPosition(1) is OfficialFeaturedShopViewModel) {
//
//            }
            val position = if (adapter?.itemCount?:0 > 3 ) 3 else adapter?.itemCount?:0
            adapter?.addElement(position, DynamicChannelViewModel(dynamicChannel.channels))
            adapter?.notifyItemInserted(position)
        }

        fun mappingProductrecommendationTitle(title: String, adapter: OfficialHomeAdapter?) {
            adapter?.addElement(ProductRecommendationTitleViewModel("Test title"))
            adapter?.notifyItemInserted(adapter.lastIndex)
        }

        fun mappingProductRecommendation(productRecommendation: RecommendationWidget, adapter: OfficialHomeAdapter?, listener: RecommendationListener) {
            productRecommendation.recommendationItemList.forEach {
                adapter?.addElement(ProductRecommendationViewModel(it, listener))
            }
            adapter?.notifyItemInserted(adapter.lastIndex)
        }
    }
}
