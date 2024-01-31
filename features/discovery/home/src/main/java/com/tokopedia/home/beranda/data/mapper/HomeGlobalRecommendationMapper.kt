package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.home.beranda.domain.gql.feed.Banner
import com.tokopedia.home.beranda.domain.gql.feed.GetHomeRecommendationContent
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeGlobalRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.foryou.banner.BannerRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.utils.RecomTemporary
import java.util.*

@RecomTemporary
class HomeGlobalRecommendationMapper {

    fun mapToHomeRecommendationDataModel(
        graphqlResponse: GetHomeRecommendationContent,
        tabName: String,
        pageNumber: Int
    ): HomeGlobalRecommendationDataModel {
        val recommendationProduct = graphqlResponse.recommendationProduct
        val visitables = mutableListOf<ForYouRecommendationVisitable>()
        val productStack = Stack<HomeRecommendationModel>()
        // reverse stack because to get the first in
        Collections.reverse(productStack)
        productStack.addAll(convertToHomeProductFeedModel(recommendationProduct.product, recommendationProduct.pageName, recommendationProduct.layoutName, tabName, pageNumber))

        val bannerStack = Stack<BannerRecommendationModel>()
        // ignore banner when getting list type
        if (recommendationProduct.layoutName != HomeRecommendationUtil.LAYOUT_NAME_LIST) {
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
                    if (recommendationProduct.layoutName != HomeRecommendationUtil.LAYOUT_NAME_LIST) {
                        visitables.add(bannerStack.pop())
                    }
                }
                // horizontal
                TYPE_BANNER_ADS -> {
                    visitables.add(BannerOldTopAdsModel(position = index, bannerType = TYPE_BANNER_ADS))
                }
                TYPE_VERTICAL_BANNER_ADS -> {
                    if (recommendationProduct.layoutName != HomeRecommendationUtil.LAYOUT_NAME_LIST) {
                        visitables.add(BannerOldTopAdsModel(position = index, bannerType = TYPE_VERTICAL_BANNER_ADS))
                    }
                }
            }
        }
        return HomeGlobalRecommendationDataModel(visitables, recommendationProduct.hasNextPage)
    }

    private fun convertToHomeBannerFeedModel(banners: List<Banner>, tabName: String, pageNumber: Int): List<BannerRecommendationModel> {
        val bannerFeedViewModels = ArrayList<BannerRecommendationModel>()
        for (position in banners.indices) {
            val banner = banners[position]

            bannerFeedViewModels.add(
                BannerRecommendationModel(
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

    private fun convertToHomeProductFeedModel(products: List<Product>, pageName: String, layoutName: String, tabName: String, pageNumber: Int): List<HomeRecommendationModel> {
        val homeFeedViewModels = ArrayList<HomeRecommendationModel>()
        for (position in products.indices) {
            val product = products[position]

            val productCard = mapToProductCardModel(product)
            homeFeedViewModels.add(
                HomeRecommendationModel(
                    productCardModel = productCard,
                    recommendationProductItem = product.mapToHomeGlobalRecommendationProductItem(),
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
                imageUrl = it.imageUrl
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
                ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
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
