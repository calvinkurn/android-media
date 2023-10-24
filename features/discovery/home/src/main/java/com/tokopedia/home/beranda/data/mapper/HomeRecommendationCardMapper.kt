package com.tokopedia.home.beranda.data.mapper

import com.google.gson.Gson
import com.tokopedia.home.beranda.data.model.AdsBannerItemResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.GetHomeRecommendationCardResponse
import com.tokopedia.home.beranda.domain.gql.recommendationcard.RecommendationCard
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import dagger.Lazy
import javax.inject.Inject

class HomeRecommendationCardMapper @Inject constructor(
    private val gson: Lazy<Gson>
) {

    fun mapToRecommendationCardDataModel(
        getHomeRecommendationCard: GetHomeRecommendationCardResponse.GetHomeRecommendationCard,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val homeRecommendationVisitableList = mutableListOf<HomeRecommendationVisitable>()

        getHomeRecommendationCard.recommendationCards.forEachIndexed { index, card ->
            when (card.layout) {
                TYPE_PRODUCT -> {
                    homeRecommendationVisitableList.add(
                        mapToHomeProductFeedModel(
                            card,
                            getHomeRecommendationCard.pageName,
                            getHomeRecommendationCard.layoutName,
                            pageNumber,
                            index,
                            getHomeRecommendationCard.recommendationCards.size
                        )
                    )
                }

                TYPE_BANNER_ADS -> {
                    // todo mapping topadsImageViewModel
                    val adsBannerItemResponse = convertDataJsonToAdsBannerItem(card.dataStringJson)

                    homeRecommendationVisitableList.add(
                        HomeRecommendationBannerTopAdsDataModel(
                            position = index
                        )
                    )
                }
            }
        }

        return HomeRecommendationDataModel(
            homeRecommendationVisitableList,
            getHomeRecommendationCard.hasNextPage
        )
    }

    private fun convertDataJsonToAdsBannerItem(dataStringJson: String): AdsBannerItemResponse? {
        try {
            return gson.get().fromJson(dataStringJson, AdsBannerItemResponse::class.java)
        } catch (e: Exception) {
            // no op
        }
        return null
    }

    private fun mapToHomeProductFeedModel(
        recommendationCard: RecommendationCard,
        pageName: String,
        layoutName: String,
        pageNumber: Int,
        index: Int,
        cardTotal: Int
    ): HomeRecommendationItemDataModel {
        val productCard = mapToProductCardModel(recommendationCard)

        return HomeRecommendationItemDataModel(
            null,
            recommendationCard,
            productCard,
            pageName,
            layoutName,
            (((pageNumber - Int.ONE) * cardTotal) + index + Int.ONE)
        )
    }

    private fun mapToTopAdsImageViewModel(adsBannerItemResponse: AdsBannerItemResponse): List<TopAdsImageViewModel> {
        return emptyList()
    }

    private fun mapToLabelGroupList(recommendationCard: RecommendationCard): List<ProductCardModel.LabelGroup> {
        return recommendationCard.labelGroup.map {
            ProductCardModel.LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        }
    }

    private fun mapToProductCardModel(recommendationCard: RecommendationCard): ProductCardModel {
        val labelGroups = mapToLabelGroupList(recommendationCard)

        return ProductCardModel(
            slashedPrice = recommendationCard.slashedPrice,
            productName = recommendationCard.name,
            formattedPrice = recommendationCard.price,
            productImageUrl = recommendationCard.imageUrl,
            isTopAds = recommendationCard.isTopads,
            discountPercentage = if (recommendationCard.discountPercentage > 0) "${recommendationCard.discountPercentage}%" else "",
            ratingCount = recommendationCard.rating,
            reviewCount = recommendationCard.countReview,
            countSoldRating = recommendationCard.ratingAverage,
            shopLocation = recommendationCard.shop.city,
            isWishlistVisible = true,
            isWishlisted = recommendationCard.isWishlist,
            shopBadgeList = recommendationCard.badges.map {
                ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                isActive = recommendationCard.freeOngkir.isActive,
                imageUrl = recommendationCard.freeOngkir.imageUrl
            ),
            labelGroupList = labelGroups,
            hasThreeDots = true,
            cardInteraction = true
        )
    }

//    private fun mapToListOfTopAdsImageViewModel(
//        responseBanner: TopAdsBannerResponse,
//        queryParams: MutableMap<String, Any>
//    ): ArrayList<TopAdsImageViewModel> {
//        val list = ArrayList<TopAdsImageViewModel>()
//        responseBanner.topadsDisplayBannerAdsV3.bannerListData?.forEach { data ->
//            val model = TopAdsImageViewModel()
//            val image = getImageById(data.banner?.images, queryParams[DIMEN_ID].toString())
//            with(model) {
//                bannerId = data.id
//                bannerName = data.banner?.name ?: ""
//                position = data.banner?.position ?: 0
//                ImpressHolder = data.banner?.shop?.shopImage
//                layoutType = data.banner?.layoutType ?: TopAdsConstants.TdnBannerConstants.TYPE_SINGLE
//                adClickUrl = data.adClickUrl ?: ""
//                adViewUrl = data.adViewUrl ?: ""
//                applink = data.applinks
//                imageUrl = image.first
//                imageWidth = image.second
//                imageHeight = image.third
//                isAutoScrollEnabled =
//                    responseBanner.topadsDisplayBannerAdsV3.header?.autoScroll?.enable ?: false
//                scrollDuration =
//                    responseBanner.topadsDisplayBannerAdsV3.header?.autoScroll?.timer?.times(1000)
//                        ?: 0
//                nextPageToken =
//                    responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.nextPageToken
//                shopId = data.banner?.shop?.shopID?.toString() ?: ""
//                currentPage =
//                    responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.currentPage ?: ""
//                kind = responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.kind ?: ""
//            }
//            list.add(model)
//        }
//
//        return list
//    }
//
//    private fun getImageById(
//        images: List<TopAdsBannerResponse.TopadsDisplayBannerAdsV3.BannerListData.Banner.Image>?,
//        dimenId: String?
//    ): Triple<String, Int, Int> {
//        var imageUrl = ""
//        var imageWidth = 0
//        var imageHeight = 0
//
//        images?.let {
//            for (image in it) {
//                if (image.dimension?.id == dimenId) {
//                    imageUrl = image.url ?: ""
//                    imageWidth = image.dimension?.width ?: 0
//                    imageHeight = image.dimension?.height ?: 0
//                    break
//                }
//            }
//        }
//        return Triple(imageUrl, imageWidth, imageHeight)
//    }

    companion object {
        private const val TYPE_PRODUCT = "product"
        private const val TYPE_BANNER = "banner"
        private const val TYPE_BANNER_ADS = "banner_ads"
    }
}
