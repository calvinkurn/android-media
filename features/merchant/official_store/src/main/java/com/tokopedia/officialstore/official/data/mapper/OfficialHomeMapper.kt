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
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.RemoteConfig

class OfficialHomeMapper (
        private val context: Context,
        private val dispatchers: CoroutineDispatchers
){
    var listOfficialStore = mutableListOf<Visitable<*>>()
    companion object {
        private const val BANNER_POSITION = 0
        private const val BENEFIT_POSITION = 1
        private const val FEATURE_SHOP_POSITION = 2
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
        listOfficialStore = newList
        adapter?.submitList(listOfficialStore)
    }

    fun mappingBenefit(benefits: OfficialStoreBenefits, adapter: OfficialHomeAdapter?) {
        listOfficialStore.run {
            val index = indexOfFirst { it is OfficialBenefitDataModel }

            val benefit = OfficialBenefitDataModel(benefits.benefits)

            if(index == -1) add(BENEFIT_POSITION, benefit)
            else set(index, benefit)

            adapter?.submitList(this.toMutableList())
        }
    }

    //this is old featured brand from external api
    //now doubles with featured brand on dynamic channel
    fun mappingFeaturedShop(featuredShop: OfficialStoreFeaturedShop, adapter: OfficialHomeAdapter?, categoryName: String?, listener: FeaturedShopListener) {
        listOfficialStore.run {
            val index = indexOfFirst { it is OfficialFeaturedShopDataModel }

            val officialFeaturedShop = OfficialFeaturedShopDataModel(
                    featuredShop.featuredShops,
                    featuredShop.header,
                    categoryName.toEmptyStringIfNull(),
                    listener
            )
            if(index == -1) {
                if(size < FEATURE_SHOP_POSITION) add(officialFeaturedShop)
                else add(FEATURE_SHOP_POSITION, officialFeaturedShop)
            } else {
                set(index, officialFeaturedShop)
            }

            adapter?.submitList(this.toMutableList())
        }
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
                    else -> views.add(DynamicChannelDataModel(officialStore))
                }
            }
            listOfficialStore.removeAll { it is DynamicChannelDataModel || it is DynamicLegoBannerDataModel || it is HomeComponentVisitable }
            listOfficialStore.addAll(views)
            adapter?.submitList(listOfficialStore.toMutableList())
        }
    }

    fun mappingProductRecommendationTitle(title: String, adapter: OfficialHomeAdapter?) {
        listOfficialStore.add(ProductRecommendationTitleDataModel(title))
        adapter?.submitList(listOfficialStore.toMutableList())
    }

    fun mappingProductRecommendation(productRecommendation: RecommendationWidget, adapter: OfficialHomeAdapter?, listener: RecommendationListener) {
        productRecommendation.recommendationItemList.forEach {
            listOfficialStore.add(ProductRecommendationDataModel(it, listener))
        }
        listOfficialStore.removeAll { it is OfficialLoadingDataModel || it is OfficialLoadingMoreDataModel }
        adapter?.submitList(listOfficialStore.toMutableList())
    }

    fun removeRecommendation(adapter: OfficialHomeAdapter?){
        listOfficialStore.run {
            removeAll { it is ProductRecommendationDataModel || it is ProductRecommendationTitleDataModel }
            adapter?.submitList(this.toMutableList())
        }
    }

    fun showLoadingMore(adapter: OfficialHomeAdapter?){
        listOfficialStore.run {
            this.add(OfficialLoadingMoreDataModel())
            adapter?.submitList(this.toMutableList())
        }
    }

    fun removeFlashSale(adapter: OfficialHomeAdapter?){
        listOfficialStore.run {
            removeAll {
                it is DynamicChannelDataModel || it is ProductRecommendationDataModel
            }
            adapter?.submitList(this.toMutableList())
        }
    }

    fun updateWishlist(wishlist: Boolean, position: Int, adapter: OfficialHomeAdapter?) {
        listOfficialStore.run {
            (getOrNull(position) as? ProductRecommendationDataModel)?.let { recom ->
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
        listOfficialStore.add(BANNER_POSITION, OfficialLoadingDataModel())
        adapter?.submitList(listOfficialStore.toMutableList())
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

    inline fun <reified T> findWidgetList(predicate: (T?) -> Boolean = {true}, actionOnFound: (List<IndexedValue<T>>) -> Unit) {
        val listFound = mutableListOf<IndexedValue<T>>()
        listOfficialStore.withIndex().filter { it.value is T && predicate.invoke(it.value as? T) }.let {
            it.forEach { indexedValue ->
                if (indexedValue.value is T) {
                    (indexedValue as? IndexedValue<T>)?.let { findValue ->
                        listFound.add(findValue)
                    }
                }
            }
        }
        actionOnFound.invoke(listFound)
    }

    fun updateFeaturedShopDC(newData: FeaturedShopDataModel, action: (listSubmitted: MutableList<Visitable<*>>) -> Unit) {
        val newList = mutableListOf<Visitable<*>>()
        listOfficialStore.toMutableList().forEach {
            if (it is FeaturedShopDataModel && it.channelModel.id == newData.channelModel.id) {
                newData.channelModel.verticalPosition = it.channelModel.verticalPosition
                newData.channelModel.channelHeader = it.channelModel.channelHeader
//                val isFeaturedShopEmpty = newList.filter { model -> model is FeaturedShopDataModel}.isEmpty()
//                if (isFeaturedShopEmpty)
                    newList.add(newData)
            }
            else {
                newList.add(it)
            }
        }
        listOfficialStore = newList
        action.invoke(listOfficialStore.toMutableList())
    }

    fun removeFeaturedShopDC(newData: FeaturedShopDataModel, action: (listSubmitted: MutableList<Visitable<*>>) -> Unit) {
        val newList = mutableListOf<Visitable<*>>()
        listOfficialStore.toMutableList().forEach {
            if (it !is FeaturedShopDataModel || ((it is FeaturedShopDataModel && it.channelModel.id != newData.channelModel.id)))  {
                newList.add(it)
            }
        }
        listOfficialStore = newList
        action.invoke(listOfficialStore.toMutableList())
    }

    fun mappingRecomWidget(data: BestSellerDataModel, action: (listSubmitted: MutableList<Visitable<*>>) -> Unit) {
        val newList = mutableListOf<Visitable<*>>()
        val copyListOfficialStore = listOfficialStore.toMutableList()
        copyListOfficialStore.forEach {
            if (it is BestSellerDataModel && it.channelId == data.channelId) {
                newList.add(data)
            }
            else {
                newList.add(it)
            }
        }
        val isBestSellerWidgetNotExist = copyListOfficialStore.indexOfFirst { it is BestSellerDataModel } == WIDGET_NOT_FOUND
        if (isBestSellerWidgetNotExist) {
            newList.add(RECOM_WIDGET_POSITION, data)
        }
        listOfficialStore = newList
        action.invoke(listOfficialStore.toMutableList())
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
        listOfficialStore = newList
        action.invoke(listOfficialStore.toMutableList())
    }
}
