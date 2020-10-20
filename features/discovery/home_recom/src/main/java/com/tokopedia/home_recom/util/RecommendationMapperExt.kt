package com.tokopedia.home_recom.util

import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.recommendation_widget_common.TYPE_CAROUSEL
import com.tokopedia.recommendation_widget_common.TYPE_CUSTOM_HORIZONTAL
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * Created by Lukas on 08/10/20.
 */

/**
 * Function [mapDataModel]
 * It handling mapper pojo into dataModel
 * @return list of dataModel
 */
fun List<RecommendationWidget>.mapDataModel(): List<HomeRecommendationDataModel>{
    val list = ArrayList<HomeRecommendationDataModel>()
    filter { it.recommendationItemList.isNotEmpty() }.map{ recommendationWidget ->
        when(recommendationWidget.layoutType){
            TYPE_CAROUSEL, TYPE_CUSTOM_HORIZONTAL -> list.add(
                    RecommendationCarouselDataModel(
                            recommendationWidget.title,
                            recommendationWidget.seeMoreAppLink,
                            recommendationWidget.recommendationItemList.asSequence().map { RecommendationCarouselItemDataModel(it, list.size) }.toList()
                    )
            )
            else -> {
                list.add(TitleDataModel(recommendationWidget.title, recommendationWidget.pageName, recommendationWidget.seeMoreAppLink))
                recommendationWidget.recommendationItemList.forEach {
                    list.add(RecommendationItemDataModel(it))
                }
            }
        }
    }
    return list
}

fun ProductInfoDataModel.mapToRecommendationTracking(): RecommendationItem{
    return RecommendationItem(
            productId = productDetailData?.id ?: -1,
            position = 0,
            name = productDetailData?.name ?: "",
            appUrl = productDetailData?.appUrl ?: "",
            clickUrl = productDetailData?.clickUrl ?: "",
            categoryBreadcrumbs = productDetailData?.categoryBreadcrumbs ?: "",
            countReview = productDetailData?.countReview ?: -1,
            departmentId = productDetailData?.departmentId ?: -1,
            imageUrl = productDetailData?.imageUrl ?: "",
            isTopAds = productDetailData?.isTopads ?: false,
            isWishlist = productDetailData?.isWishlist ?: false,
            price = productDetailData?.price ?: "",
            priceInt = productDetailData?.priceInt ?: -1,
            rating = productDetailData?.rating ?: -1,
            recommendationType = productDetailData?.recommendationType ?: "",
            stock = productDetailData?.stock ?: -1,
            trackerImageUrl = productDetailData?.trackerImageUrl ?: "",
            url = productDetailData?.url ?: "",
            wishlistUrl = productDetailData?.wishlistUrl ?: "",
            slashedPrice = productDetailData?.slashedPrice ?: "",
            discountPercentageInt = productDetailData?.discountPercentage ?: -1,
            slashedPriceInt = productDetailData?.slashedPriceInt ?: -1,
            cartId = "",
            shopId = productDetailData?.shop?.id ?: -1,
            shopName = productDetailData?.shop?.name ?: "",
            shopType = if(productDetailData?.shop?.isGold == true) "gold_merchant" else "reguler",
            quantity = productDetailData?.minOrder ?: -1,
            header = "",
            pageName = "",
            minOrder = productDetailData?.minOrder ?: -1,
            location = "",
            badgesUrl = listOf(),
            type = "",
            isFreeOngkirActive = false,
            freeOngkirImageUrl = "",
            discountPercentage = "",
            isGold = false
    )
}