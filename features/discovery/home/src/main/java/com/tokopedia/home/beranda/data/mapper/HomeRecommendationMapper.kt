package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.home.beranda.domain.gql.feed.Banner
import com.tokopedia.home.beranda.domain.gql.feed.GetHomeRecommendationContent
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BaseHomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil.LAYOUT_NAME_LIST
import com.tokopedia.productcard.ProductCardModel
import java.util.*

class HomeRecommendationMapper {
    fun mapToHomeRecommendationDataModel(
        graphqlResponse: GetHomeRecommendationContent,
        tabName: String,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val recommendationProduct = graphqlResponse.recommendationProduct
        val visitables = mutableListOf<BaseHomeRecommendationVisitable>()
        val productStack = Stack<HomeRecommendationItemDataModel>()
        // reverse stack because to get the first in
        Collections.reverse(productStack)
        productStack.addAll(convertToHomeProductFeedModel(recommendationProduct.product, recommendationProduct.pageName, recommendationProduct.layoutName, tabName, pageNumber))

        val bannerStack = Stack<BannerRecommendationDataModel>()
        // ignore banner when getting list type
        if (recommendationProduct.layoutName != LAYOUT_NAME_LIST) {
            // reverse stack because to get the first in
            Collections.reverse(productStack)
            bannerStack.addAll(convertToHomeBannerFeedModel(recommendationProduct.banners, tabName, pageNumber))
        }

        recommendationProduct.layoutTypes.forEachIndexed { index, layoutType ->
            when (layoutType.type) {
                TYPE_PRODUCT -> {
                    visitables.add(productStack.pop())
                }
                // vertical
                TYPE_BANNER -> {
                    // ignore banner when getting list type
                    if (recommendationProduct.layoutName != LAYOUT_NAME_LIST) {
                        visitables.add(bannerStack.pop())
                    }
                }
                // horizontal
                TYPE_BANNER_ADS -> {
                    visitables.add(HomeRecommendationBannerTopAdsOldDataModel(position = index, bannerType = TYPE_BANNER_ADS))
                }
                TYPE_VERTICAL_BANNER_ADS -> {
                    if (recommendationProduct.layoutName != LAYOUT_NAME_LIST) {
                        visitables.add(HomeRecommendationBannerTopAdsOldDataModel(position = index, bannerType = TYPE_VERTICAL_BANNER_ADS))
                    }
                }
            }
        }
        return HomeRecommendationDataModel(visitables, recommendationProduct.hasNextPage)
    }

    private fun convertToHomeBannerFeedModel(banners: List<Banner>, tabName: String, pageNumber: Int): List<BannerRecommendationDataModel> {
        val bannerFeedViewModels = ArrayList<BannerRecommendationDataModel>()
        for (position in banners.indices) {
            val banner = banners[position]

            bannerFeedViewModels.add(
                BannerRecommendationDataModel(
                    banner.id,
                    banner.name,
                    banner.imageUrl,
                    banner.url,
                    banner.applink,
                    banner.buAttribution,
                    banner.creativeName,
                    banner.target,
                    (((pageNumber - 1) * banners.size) + position + 1).toInt(),
                    banner.galaxyAttribution,
                    banner.persona,
                    banner.brandId,
                    banner.categoryPersona,
                    tabName
                )
            )
        }
        return bannerFeedViewModels
    }

    private fun convertToHomeProductFeedModel(products: List<Product>, pageName: String, layoutName: String, tabName: String, pageNumber: Int): List<HomeRecommendationItemDataModel> {
        val homeFeedViewModels = ArrayList<HomeRecommendationItemDataModel>()
        for (position in products.indices) {
            val product = products[position]

            val productCard = mapToProductCardModel(product)
            homeFeedViewModels.add(
                HomeRecommendationItemDataModel(
                    productCardModel = productCard,
                    recommendationProductItem = product.mapToHomeRecommendationProductItem(),
                    pageName,
                    layoutName,
                    (((pageNumber - 1) * products.size) + position + 1),
                    tabName
                )
            )
        }
        return homeFeedViewModels
    }

    private fun mapToLabelGroupList(product: Product): List<ProductCardModel.LabelGroup> {
        return product.labelGroup.map {
            ProductCardModel.LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.imageUrl,
                styleList = it.styles.map { ProductCardModel.LabelGroup.Style(it.key, it.value) }
            )
        }
    }

    private fun mapToProductCardModel(product: Product): ProductCardModel {
        val labelGroups = mapToLabelGroupList(product)

        return ProductCardModel(
            slashedPrice = product.slashedPrice,
            productName = product.name,
            formattedPrice = product.price,
            productImageUrl = product.imageUrl,
            isTopAds = product.isTopads,
            discountPercentage = if (product.discountPercentage > 0) "${product.discountPercentage}%" else "",
            ratingCount = product.rating,
            reviewCount = product.countReview,
            countSoldRating = product.ratingFloat,
            shopLocation = product.shop.city,
            isWishlistVisible = true,
            isWishlisted = product.isWishlist,
            shopBadgeList = product.badges.map {
                ProductCardModel.ShopBadge(title = it.title, imageUrl = it.imageUrl)
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                isActive = product.freeOngkirInformation.isActive,
                imageUrl = product.freeOngkirInformation.imageUrl
            ),
            labelGroupList = labelGroups,
            hasThreeDots = true,
            cardInteraction = true
        )
    }

    companion object {
        private const val TYPE_PRODUCT = "product"
        private const val TYPE_BANNER = "banner"
        const val TYPE_BANNER_ADS = "banner_ads"
        const val TYPE_VERTICAL_BANNER_ADS = "banner_ads_vertical"
    }
}
