package com.tokopedia.officialstore.official.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialFeaturedShopViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

class OfficialHomeMapper {

    companion object {
        fun mappingBanners(banner: OfficialStoreBanners, adapter: OfficialHomeAdapter?) {
            adapter?.addElement(0, OfficialBannerViewModel(banner.banners))
            adapter?.notifyItemInserted(0)
        }

        fun mappingFeaturedShop(featuredShop: OfficialStoreFeaturedShop, adapter: OfficialHomeAdapter?) {
            if (adapter?.getDataByPosition(0) is OfficialBannerViewModel) { // check if banner is exist
                adapter.addElement(1, OfficialFeaturedShopViewModel(featuredShop.featuredShops))
                adapter.notifyItemInserted(1)
            }
        }

        fun mappingDynamicChannel(dynamicChannel: DynamicChannel, adapter: OfficialHomeAdapter?) {
            if (adapter?.getDataByPosition(1) is OfficialFeaturedShopViewModel) {
                val views = mutableListOf<Visitable<OfficialHomeAdapterTypeFactory>>()

                dynamicChannel.channels.forEach { channel ->
                    views.add(DynamicChannelViewModel(channel))
                }

                adapter.addElement(views)
            }
        }

        fun mappingProductRecommendation(productRecommendation: RecommendationWidget, adapter: OfficialHomeAdapter?, listener: RecommendationListener) {
            val defaultValue = ""
            val pageName = "official-store"
            val pageNumber = 1

//            adapter?.addElement(3, ProductRecommendationViewModel(productRecommendation.recommendationItemList.get(0), listener))
//            adapter?.notifyItemInserted(3)
        }
    }
}
