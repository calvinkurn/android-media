package com.tokopedia.officialstore.official.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.FeaturedBrandDataModel
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.SpecialReleaseDataModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.official.data.mapper.OfficialStoreDynamicChannelComponentMapper
import com.tokopedia.officialstore.official.data.model.HeaderShop
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingMoreDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBenefitDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialFeaturedShopDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBannerDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationWithTopAdsHeadline
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialTopAdsBannerDataModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

object OfficialHomeMapper {
    const val BANNER_POSITION = 0
    const val BENEFIT_POSITION = 1
    const val FEATURE_SHOP_POSITION = 2
    const val RECOM_WIDGET_POSITION = 3
    const val WIDGET_NOT_FOUND = -1
    private val atfSize = 3

    fun mappingBanners(
        banner: OfficialStoreBanners,
        currentList: List<Visitable<*>>,
        categoryName: String?,
        action: (updatedList: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = mutableListOf<Visitable<*>>()
        val officialBanner = OfficialBannerDataModel(banner.banners, categoryName.toEmptyStringIfNull())
        currentList.forEach {
            if (it is OfficialBannerDataModel) {
                newList.add(officialBanner)
            }
            else if (it !is OfficialLoadingMoreDataModel && it !is OfficialLoadingDataModel){
                newList.add(it)
            }
        }
        val isOfficialBannerDataNotExist = currentList.indexOfFirst { it is OfficialBannerDataModel } == WIDGET_NOT_FOUND
        if (isOfficialBannerDataNotExist) {
            newList.add(BANNER_POSITION, officialBanner)
        }
        action.invoke(newList)
    }

    fun mappingBenefit(
        benefits: OfficialStoreBenefits,
        currentList: List<Visitable<*>>,
        action: (updatedList: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = mutableListOf<Visitable<*>>()
        val benefit = OfficialBenefitDataModel(benefits.benefits)
        currentList.forEach {
            if(it is OfficialBenefitDataModel) {
                newList.add(benefit)
            }
            else {
                newList.add(it)
            }
        }
        val isBenefitNotExisted = newList.indexOfFirst { it is OfficialBenefitDataModel } == WIDGET_NOT_FOUND
        if(isBenefitNotExisted) {
            if(newList.size > BENEFIT_POSITION) {
                newList.add(BENEFIT_POSITION, benefit)
            }
            else {
                newList.add(benefit)
            }
        }
        action.invoke(newList)
    }

    fun mappingFeaturedShop(
        featuredShop: OfficialStoreFeaturedShop,
        currentList: List<Visitable<*>>,
        categoryName: String?,
        action: (updatedList: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = mutableListOf<Visitable<*>>()
        val officialFeaturedShop = OfficialFeaturedShopDataModel(
            featuredShop.featuredShops,
            featuredShop.header,
            categoryName.toEmptyStringIfNull())
        currentList.forEach {
            if(it is OfficialFeaturedShopDataModel) {
                newList.add(officialFeaturedShop)
            }
            else {
                newList.add(it)
            }
        }
        val isOfficialFeaturedShopNotExisted = newList.indexOfFirst { it is OfficialFeaturedShopDataModel } == WIDGET_NOT_FOUND
        if (isOfficialFeaturedShopNotExisted && newList.size > FEATURE_SHOP_POSITION) {
            newList.add(FEATURE_SHOP_POSITION, officialFeaturedShop)
        }
        else {
            newList.add(officialFeaturedShop)
        }
        action.invoke(newList)
    }

    fun mappingDynamicChannel(
        officialStoreChannels: List<OfficialStoreChannel>,
        currentList: List<Visitable<*>>,
        action: (updatedList: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = mutableListOf<Visitable<*>>()
        val atfList = currentList.subList(0, atfSize.coerceAtMost(currentList.size)).filter {
            it is OfficialBannerDataModel || it is OfficialBenefitDataModel || it is OfficialFeaturedShopDataModel
        }
        newList.addAll(atfList)
        officialStoreChannels.forEachIndexed { pos, officialStore ->
            val position = pos + atfList.size + 1
            when (officialStore.channel.layout) {
                DynamicChannelLayout.LAYOUT_MIX_LEFT -> {
                    newList.add(
                        MixLeftDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_MIX_TOP -> {
                    newList.add(
                        MixTopDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_BANNER_ADS_CAROUSEL -> {
                    newList.add(
                        OfficialTopAdsBannerDataModel(officialStore.channel.header?.name)
                    )
                }
                DynamicChannelLayout.LAYOUT_FEATURED_BRAND -> {
                    newList.add(
                        FeaturedBrandDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_FEATURED_SHOP -> {
                    newList.add(
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
                    newList.add(
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
                    newList.add(
                        DynamicLegoBannerDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_MERCHANT_VOUCHER -> {
                    newList.add(
                        MerchantVoucherDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
                DynamicChannelLayout.LAYOUT_SPRINT_LEGO -> newList.add(
                    DynamicChannelDataModel(
                        officialStore
                    )
                )
                DynamicChannelLayout.LAYOUT_CAMPAIGN_FEATURING -> newList.add(
                    SpecialReleaseDataModel(
                        OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                            officialStore.channel,
                            position
                        )
                    )
                )
            }
        }
        action.invoke(newList)
    }

    fun mappingRecomProducts(
        productRecommendationWithTopAdsHeadline: ProductRecommendationWithTopAdsHeadline,
        currentList: List<Visitable<*>>,
        action: (updatedList: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = currentList.toMutableList()
        val headlineIndex = productRecommendationWithTopAdsHeadline.officialTopAdsHeadlineDataModel?.topAdsHeadlineResponse?.displayAds?.data?.firstOrNull()?.cpm?.position
        productRecommendationWithTopAdsHeadline.recommendationWidget.recommendationItemList.forEachIndexed { index, recommendationItem ->
            if (index == headlineIndex) productRecommendationWithTopAdsHeadline.officialTopAdsHeadlineDataModel.let {
                newList.add(it)
            }
            newList.add(ProductRecommendationDataModel(recommendationItem))
        }
        newList.removeAll { it is OfficialLoadingDataModel || it is OfficialLoadingMoreDataModel }
        action.invoke(newList)
    }

    fun mappingRecomWidget(
        bestSellerDataModel: BestSellerDataModel,
        currentList: List<Visitable<*>>,
        action: (updatedList: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = mutableListOf<Visitable<*>>()
        currentList.forEach {
            if (it is BestSellerDataModel && it.channelId == bestSellerDataModel.channelId) {
                newList.add(bestSellerDataModel)
            } else {
                newList.add(it)
            }
        }
        val isBestSellerWidgetNotExist =
            newList.indexOfFirst { it is BestSellerDataModel } == WIDGET_NOT_FOUND
        if (isBestSellerWidgetNotExist) {
            if (newList.size > RECOM_WIDGET_POSITION) {
                newList.add(RECOM_WIDGET_POSITION, bestSellerDataModel)
            } else {
                newList.add(bestSellerDataModel)
            }
        }
        action.invoke(newList)
    }

    fun updateFeaturedShop(
        featuredShopDataModel: FeaturedShopDataModel,
        currentList: List<Visitable<*>>,
        action: (updatedList: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = mutableListOf<Visitable<*>>()
        currentList.forEach {
            if (it is FeaturedShopDataModel && it.channelModel.id == featuredShopDataModel.channelModel.id) {
                featuredShopDataModel.channelModel.verticalPosition = it.channelModel.verticalPosition
                featuredShopDataModel.channelModel.channelHeader = it.channelModel.channelHeader
                newList.add(featuredShopDataModel)
            }
            else {
                newList.add(it)
            }
        }
        action.invoke(newList)
    }

    fun updateTopAdsBanner(
        officialTopAdsBannerDataModel: OfficialTopAdsBannerDataModel,
        tdnBanner: ArrayList<TopAdsImageViewModel>,
        currentList: List<Visitable<*>>,
        action: (updatedList: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = mutableListOf<Visitable<*>>()
        currentList.forEach {
            if (it is OfficialTopAdsBannerDataModel) {
                officialTopAdsBannerDataModel.tdnBanner = tdnBanner
                newList.add(officialTopAdsBannerDataModel)
            } else {
                newList.add(it)
            }
        }
        action.invoke(newList)
    }
}
