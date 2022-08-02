package com.tokopedia.officialstore.official.data.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.visitable.*
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.RemoteConfig

class OfficialHomeMapper (
        private val context: Context,
        private val dispatchers: CoroutineDispatchers
){
    val listOfficialStore : List<Visitable<*>> get() =  _listOfficialStore
    private var _listOfficialStore = mutableListOf<Visitable<*>>()
    companion object {
        private const val BANNER_POSITION = 0
        const val BENEFIT_POSITION = 1
        const val FEATURE_SHOP_POSITION = 2
        const val RECOM_WIDGET_POSITION = 3
        const val WIDGET_NOT_FOUND = -1
    }

    fun mappingBanners(banner: OfficialStoreBanners, adapter: OfficialHomeAdapter?, categoryName: String?, isDisableForMappingBanner: Boolean) {
        val newList = mutableListOf<Visitable<*>>()
        if (isDisableForMappingBanner) {
            listOfficialStore.toMutableList().forEach {
                if (it !is OfficialLoadingMoreDataModel && it !is OfficialLoadingDataModel && it !is OfficialBannerDataModel){
                    newList.add(it)
                }
            }
            newList.add(OfficialBannerDataModel(mutableListOf(), categoryName.toEmptyStringIfNull()))
        }
        else {
            val officialBanner = OfficialBannerDataModel(banner.banners, categoryName.toEmptyStringIfNull())
            listOfficialStore.toMutableList().forEach {
                if (it is OfficialBannerDataModel) {
                    newList.add(officialBanner)
                }
                else if (it !is OfficialLoadingMoreDataModel && it !is OfficialLoadingDataModel){
                    newList.add(it)
                }
            }
            val isOfficialBannerDataNotExist = listOfficialStore.indexOfFirst { it is OfficialBannerDataModel } == WIDGET_NOT_FOUND
            if (isOfficialBannerDataNotExist) {
                newList.add(officialBanner)
            }
        }
        _listOfficialStore = newList
        adapter?.submitList(newList)
    }

    fun mappingBenefit(benefits: OfficialStoreBenefits, adapter: OfficialHomeAdapter?) {
        val newList = mutableListOf<Visitable<*>>()
        val benefit = OfficialBenefitDataModel(benefits.benefits)
        listOfficialStore.toMutableList().forEach {
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
        _listOfficialStore = newList
        adapter?.submitList(newList)
    }

    //this is old featured brand from external api
    //now doubles with featured brand on dynamic channel
    fun mappingFeaturedShop(featuredShop: OfficialStoreFeaturedShop, adapter: OfficialHomeAdapter?, categoryName: String?, listener: FeaturedShopListener) {
        val newList = mutableListOf<Visitable<*>>()
        val officialFeaturedShop = OfficialFeaturedShopDataModel(
            featuredShop.featuredShops,
            featuredShop.header,
            categoryName.toEmptyStringIfNull()
        )
        listOfficialStore.toMutableList().forEach {
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
        _listOfficialStore = newList
        adapter?.submitList(listOfficialStore.toMutableList())
    }

    fun mappingDynamicChannel(officialStoreChannels: List<OfficialStoreChannel>, adapter: OfficialHomeAdapter?, remoteConfig: RemoteConfig?) {
        if (officialStoreChannels.isNotEmpty()) {
            val views = mutableListOf<Visitable<*>>()
            officialStoreChannels.forEachIndexed { position, officialStore ->
                when (officialStore.channel.layout) {
                    DynamicChannelLayout.LAYOUT_MIX_LEFT -> {
                        views.add(
                            MixLeftDataModel(
                                OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                    officialStore.channel,
                                    position
                                )
                            )
                        )
                    }
                    DynamicChannelLayout.LAYOUT_MIX_TOP -> {
                        views.add(
                            MixTopDataModel(
                                OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                    officialStore.channel,
                                    position
                                )
                            )
                        )
                    }
                    DynamicChannelLayout.LAYOUT_FEATURED_BRAND -> {
                        views.add(
                            FeaturedBrandDataModel(
                                OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                    officialStore.channel,
                                    position
                                )
                            )
                        )
                    }
                    DynamicChannelLayout.LAYOUT_FEATURED_SHOP -> {
                        views.add(
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
                        views.add(
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
                        views.add(
                            DynamicLegoBannerDataModel(
                                OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                    officialStore.channel,
                                    position
                                )
                            )
                        )
                    }
                    DynamicChannelLayout.LAYOUT_MERCHANT_VOUCHER -> {
                        views.add(
                            MerchantVoucherDataModel(
                                OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                    officialStore.channel,
                                    position
                                )
                            )
                        )
                    }
                    DynamicChannelLayout.LAYOUT_SPRINT_LEGO,
                    DynamicChannelLayout.LAYOUT_BANNER_CAROUSEL -> views.add(
                        DynamicChannelDataModel(
                            officialStore
                        )
                    )
                    DynamicChannelLayout.LAYOUT_CAMPAIGN_FEATURING -> views.add(
                        SpecialReleaseDataModel(
                            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                                officialStore.channel,
                                position
                            )
                        )
                    )
                }
            }
            _listOfficialStore.removeAll { it is DynamicChannelDataModel || it is DynamicLegoBannerDataModel || it is HomeComponentVisitable }
            _listOfficialStore.addAll(views)
            adapter?.submitList(listOfficialStore.toMutableList())
        }
    }

    fun showLoadingMore(adapter: OfficialHomeAdapter?){
        _listOfficialStore.run {
            this.add(OfficialLoadingMoreDataModel())
            adapter?.submitList(this.toMutableList())
        }
    }

    fun mappingProductCards(grids: List<Grid>): List<ProductCardModel> {
        return grids.map {grid ->
            ProductCardModel(
                    slashedPrice = grid.slashedPrice,
                    productName = grid.name,
                    formattedPrice = grid.price,
                    productImageUrl = grid.imageUrl,
                    discountPercentage = grid.discount,
                    freeOngkir = ProductCardModel.FreeOngkir(grid.freeOngkir?.isActive
                            ?: false, grid.freeOngkir?.imageUrl ?: ""),
                    labelGroupList = grid.labelGroup.map { label ->
                        ProductCardModel.LabelGroup(
                                position = label.position,
                                title = label.title,
                                type = label.type,
                                imageUrl = label.imageUrl
                        )
                    },
                    hasThreeDots = false
            )

        }
    }

    suspend fun getMaxHeightProductCards(productCardModels: List<ProductCardModel>): Int{
        return productCardModels.getMaxHeightForGridView(
                context = context,
                coroutineDispatcher = dispatchers.io,
                productImageWidth = context.resources.getDimensionPixelSize(R.dimen.product_card_carousel_item_width)
        )
    }

    fun updateFeaturedShopDC(newData: FeaturedShopDataModel, action: (listSubmitted: MutableList<Visitable<*>>) -> Unit) {
        val newList = mutableListOf<Visitable<*>>()
        listOfficialStore.toMutableList().forEach {
            if (it is FeaturedShopDataModel && it.channelModel.id == newData.channelModel.id) {
                newData.channelModel.verticalPosition = it.channelModel.verticalPosition
                newData.channelModel.channelHeader = it.channelModel.channelHeader
                newList.add(newData)
            }
            else {
                newList.add(it)
            }
        }
        _listOfficialStore = newList
        action.invoke(newList.toMutableList())
    }

    fun removeFeaturedShopDC(newData: FeaturedShopDataModel, action: (listSubmitted: MutableList<Visitable<*>>) -> Unit) {
        val newList = mutableListOf<Visitable<*>>()
        listOfficialStore.toMutableList().forEach {
            if (it !is FeaturedShopDataModel || ((it is FeaturedShopDataModel && it.channelModel.id != newData.channelModel.id)))  {
                newList.add(it)
            }
        }
        _listOfficialStore = newList
        action.invoke(newList.toMutableList())
    }

    fun mappingRecomWidget(data: BestSellerDataModel, action: (listSubmitted: MutableList<Visitable<*>>) -> Unit) {
        val newList = mutableListOf<Visitable<*>>()
        val copyListOfficialStore = listOfficialStore.toMutableList()
        copyListOfficialStore.forEach {
            if (it is BestSellerDataModel && it.channelId == data.channelId) {
                newList.add(data)
            } else {
                newList.add(it)
            }
        }
        val isBestSellerWidgetNotExist =
            copyListOfficialStore.indexOfFirst { it is BestSellerDataModel } == WIDGET_NOT_FOUND
        if (isBestSellerWidgetNotExist) {
            if (newList.size > RECOM_WIDGET_POSITION) {
                newList.add(RECOM_WIDGET_POSITION, data)
            } else {
                newList.add(data)
            }
        }
        _listOfficialStore = newList
        action.invoke(newList)
    }

    fun removeRecomWidget(
        action: (listSubmitted: MutableList<Visitable<*>>) -> Unit
    ) {
        val newList = mutableListOf<Visitable<*>>()
        listOfficialStore.toMutableList().forEach {
            if (it !is BestSellerDataModel) {
                newList.add(it)
            }
        }
        _listOfficialStore = newList
        action.invoke(newList)
    }
}
