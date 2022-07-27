package com.tokopedia.officialstore

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.visitable.*
import com.tokopedia.officialstore.official.data.mapper.OfficialStoreDynamicChannelComponentMapper
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel

object OfficialHomeMapper {
    const val BANNER_POSITION = 0
    const val BENEFIT_POSITION = 1
    const val FEATURE_SHOP_POSITION = 2
    const val RECOM_WIDGET_POSITION = 3
    const val WIDGET_NOT_FOUND = -1

    fun mappingDynamicChannel(
        officialStoreChannels: List<OfficialStoreChannel>,
        currentList: List<Visitable<*>>
    ): MutableList<Visitable<*>> {
        val newList = currentList.toMutableList()
        val dcList = mutableListOf<Visitable<*>>()
        officialStoreChannels.forEachIndexed { position, officialStore ->
            when (officialStore.channel.layout) {
                DynamicChannelLayout.LAYOUT_MIX_LEFT -> {
                    dcList.add(
                        MixLeftDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_MIX_TOP -> {
                    dcList.add(
                        MixTopDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_FEATURED_BRAND -> {
                    dcList.add(
                        FeaturedBrandDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_FEATURED_SHOP -> {
                    dcList.add(
                        FeaturedShopDataModel(
                            channelModel = OfficialStoreDynamicChannelComponentMapper.mapChannelToComponentBannerToHeader(
                                officialStore.channel,
                                position
                            ),
                            state = FeaturedShopDataModel.STATE_LOADING,
                            page = FeaturedShopDataModel.PAGE_OS
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_BEST_SELLING -> {
                    val channel = officialStore.channel
                    dcList.add(
                        BestSellerDataModel(
                            channelId = channel.id,
                            widgetParam = channel.widgetParam,
                            pageName = channel.pageName
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_6_IMAGE,
                DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE,
                DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE,
                DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> {
                    dcList.add(
                        DynamicLegoBannerDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_MERCHANT_VOUCHER -> {
                    dcList.add(
                        MerchantVoucherDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_SPRINT_LEGO,
                DynamicChannelLayout.LAYOUT_BANNER_CAROUSEL -> dcList.add(
                    DynamicChannelDataModel(
                        officialStore
                    )
                )
                DynamicChannelLayout.LAYOUT_CAMPAIGN_FEATURING -> dcList.add(
                    SpecialReleaseDataModel(
                        OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                            officialStore.channel,
                            position
                        )
                    )
                )
            }
        }
        if(dcList.isNotEmpty()){
            newList.removeAll { it is DynamicChannelDataModel || it is DynamicLegoBannerDataModel || it is HomeComponentVisitable }
            newList.addAll(dcList)
        }
        return newList
    }
}