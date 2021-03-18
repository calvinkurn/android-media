package com.tokopedia.recommendation_widget_common.data.mapper

import com.tokopedia.recommendation_widget_common.data.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationSpecificationLabels
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * Created by Lukas on 29/08/19
 */
object SingleProductRecommendationMapper {

    fun convertIntoRecommendationList(
            recommendations: List<SingleProductRecommendationEntity.Recommendation>?,
            title: String?,
            pageName: String?,
            layoutType: String?
    ): List<RecommendationItem>{
        return recommendations?.mapIndexed { index, data ->
            val labelGroupList = data.labelGroups?.map {
                RecommendationLabel(title = it.title ?: "", type = it.type ?: "", position = it.position)
            } ?: listOf()

            RecommendationItem(
                    productId = data.id,
                    name = data.name ?: "",
                    categoryBreadcrumbs = data.categoryBreadcrumbs ?: "",
                    url = data.url ?: "",
                    appUrl = data.appUrl ?: "",
                    clickUrl = data.clickUrl ?: "",
                    wishlistUrl = data.wishlistUrl ?: "",
                    trackerImageUrl = data.trackerImageUrl ?: "",
                    imageUrl = data.imageUrl ?: "",
                    price = data.price ?: "",
                    priceInt = data.priceInt,
                    departmentId = data.departmentId,
                    rating = data.rating,
                    ratingAverage = data.ratingAverage,
                    countReview = data.countReview,
                    stock = data.stock,
                    recommendationType = data.recommendationType ?: "",
                    isTopAds = data.isIsTopads,
                    isWishlist = data.isWishlist,
                    slashedPrice = data.slashedPrice?:"",
                    slashedPriceInt = data.slashedPriceInt,
                    discountPercentageInt = data.discountPercentage,
                    discountPercentage = if (isLabelDiscountVisible(data)) "${data.discountPercentage}%" else "",
                    position = index + 1,
                    shopId = data.shop.id ?: -1,
                    shopType = "",
                    shopName = data.shop.name ?: "",
                    cartId = "",
                    quantity = 1,
                    header = title ?: "",
                    pageName = pageName ?: "",
                    minOrder = data.minOrder ?: 1,
                    location = data.shop.city ?: "",
                    badgesUrl = data.badges.map { it.imageUrl } ?: emptyList(),
                    type = layoutType ?: "",
                    isFreeOngkirActive = data.freeOngkirInformation.isActive?:false,
                    freeOngkirImageUrl = data.freeOngkirInformation.imageUrl?:"",
                    labelGroupList = labelGroupList,
                    isGold = data.shop.isGold ?: false,
                    isOfficial = data.shop.isOfficial ?: false,
                    specs = data.specificationsLabels.map {
                        RecommendationSpecificationLabels(
                                specTitle = it.key,
                                specSummary = it.value
                        )
                    }
            )
         } ?: emptyList()
    }

    fun convertIntoRecommendationWidget(
            singleWidget: SingleProductRecommendationEntity.RecommendationData?
    ): RecommendationWidget{
        return RecommendationWidget(
                recommendationItemList = convertIntoRecommendationList(
                        singleWidget?.recommendation ?: listOf(),
                        singleWidget?.title, singleWidget?.pageName, singleWidget?.layoutType
                ),
                title = singleWidget?.title ?: "",
                pageName = singleWidget?.pageName ?: "",
                seeMoreAppLink = singleWidget?.seeMoreAppLink ?: "",
                foreignTitle = singleWidget?.foreignTitle ?: "",
                tid = singleWidget?.tid ?: ""
        )
    }

    fun isLabelDiscountVisible(productItem: SingleProductRecommendationEntity.Recommendation): Boolean {
        return productItem.discountPercentage > 0
    }
}