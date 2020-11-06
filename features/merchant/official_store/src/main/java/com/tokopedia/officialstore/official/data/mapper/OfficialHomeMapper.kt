package com.tokopedia.officialstore.official.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

class OfficialHomeMapper {
    private val listOfficialStore = mutableListOf<Visitable<*>>()
    companion object {
        private const val BANNER_POSITION = 0
        private const val BENEFIT_POSITION = 1
        private const val FEATURE_SHOP_POSITION = 2
    }

    fun mappingBanners(banner: OfficialStoreBanners, adapter: OfficialHomeAdapter?, categoryName: String?) {
        listOfficialStore.run {
            val index = indexOfFirst { it is OfficialBannerViewModel }
            val officialBanner = OfficialBannerViewModel(banner.banners, categoryName.toEmptyStringIfNull())
            removeAll { it is OfficialLoadingMoreViewModel || it is OfficialLoadingViewModel}

            if(index == -1) add(officialBanner)
            else set(index, officialBanner)
            adapter?.submitList(this.toMutableList())
        }
    }

    fun mappingBenefit(benefits: OfficialStoreBenefits, adapter: OfficialHomeAdapter?) {
        listOfficialStore.run {
            val index = indexOfFirst { it is OfficialBenefitViewModel }

            val benefit = OfficialBenefitViewModel(benefits.benefits)

            if(index == -1) add(BENEFIT_POSITION, benefit)
            else set(index, benefit)

            adapter?.submitList(this.toMutableList())
        }
    }

    fun mappingFeaturedShop(featuredShop: OfficialStoreFeaturedShop, adapter: OfficialHomeAdapter?, categoryName: String?, listener: FeaturedShopListener) {
        listOfficialStore.run {
            val index = indexOfFirst { it is OfficialFeaturedShopViewModel }

            val officialFeaturedShop = OfficialFeaturedShopViewModel(
                    featuredShop.featuredShops,
                    featuredShop.header,
                    categoryName.toEmptyStringIfNull(),
                    listener
            )
            if(index == -1) add(FEATURE_SHOP_POSITION, officialFeaturedShop)
            else set(index, officialFeaturedShop)

            adapter?.submitList(this.toMutableList())
        }
    }

    fun mappingDynamicChannel(dynamicChannel: DynamicChannel, adapter: OfficialHomeAdapter?, remoteConfig: RemoteConfig?) {
        if (dynamicChannel.channels.isNotEmpty()) {

            val availableScreens: Set<String>
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
            listOfficialStore.removeAll { it is DynamicChannelViewModel || it is DynamicLegoBannerDataModel }
            listOfficialStore.addAll(views)
            adapter?.submitList(listOfficialStore.toMutableList())
        }
    }

    fun mappingProductRecommendationTitle(title: String, adapter: OfficialHomeAdapter?) {
        listOfficialStore.add(ProductRecommendationTitleViewModel(title))
        adapter?.submitList(listOfficialStore.toMutableList())
    }

    fun mappingProductRecommendation(productRecommendation: RecommendationWidget, adapter: OfficialHomeAdapter?, listener: RecommendationListener) {
        productRecommendation.recommendationItemList.forEach {
            listOfficialStore.add(ProductRecommendationViewModel(it, listener))
        }
        listOfficialStore.removeAll { it is OfficialLoadingViewModel || it is OfficialLoadingMoreViewModel }
        adapter?.submitList(listOfficialStore.toMutableList())
    }

    fun removeRecommendation(adapter: OfficialHomeAdapter?){
        listOfficialStore.run {
            removeAll { it is ProductRecommendationViewModel || it is ProductRecommendationTitleViewModel }
            adapter?.submitList(this.toMutableList())
        }
    }

    fun showLoadingMore(adapter: OfficialHomeAdapter?){
        listOfficialStore.run {
            this.add(OfficialLoadingMoreViewModel())
            adapter?.submitList(this.toMutableList())
        }
    }

    fun removeFlashSale(adapter: OfficialHomeAdapter?){
        listOfficialStore.run {
            removeAll {
                it is DynamicChannelViewModel || it is ProductRecommendationViewModel
            }
            adapter?.submitList(this.toMutableList())
        }
    }

    fun updateWishlist(wishlist: Boolean, position: Int, adapter: OfficialHomeAdapter?) {
        listOfficialStore.run {
            (getOrNull(position) as? ProductRecommendationViewModel)?.let {recom ->
                val newRecom = recom.copy(
                        productItem = recom.productItem.copy(isWishlist = wishlist)
                )
                this[position] = newRecom
                adapter?.submitList(this.toMutableList())
            }
        }
    }

    fun resetState(adapter: OfficialHomeAdapter?) {
        listOfficialStore.clear()
        listOfficialStore.add(BANNER_POSITION, OfficialLoadingViewModel())
        adapter?.submitList(listOfficialStore.toMutableList())
    }
}
