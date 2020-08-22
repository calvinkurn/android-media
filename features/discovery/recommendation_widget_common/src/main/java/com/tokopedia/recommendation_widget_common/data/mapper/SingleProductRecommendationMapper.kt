package com.tokopedia.recommendation_widget_common.data.mapper

import com.tokopedia.recommendation_widget_common.data.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
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
                    data.id,
                    data.name ?: "",
                    data.categoryBreadcrumbs ?: "",
                    data.url ?: "",
                    data.appUrl ?: "",
                    data.clickUrl ?: "",
                    data.wishlistUrl ?: "",
                    data.trackerImageUrl ?: "",
                    data.imageUrl ?: "",
                    data.price ?: "",
                    data.priceInt,
                    data.departmentId,
                    data.rating,
                    data.countReview,
                    data.stock,
                    data.recommendationType ?: "",
                    data.isIsTopads,
                    data.isWishlist,
                    data.slashedPrice?:"",
                    data.slashedPriceInt,
                    data.discountPercentage,
                    if (isLabelDiscountVisible(data)) "${data.discountPercentage}%" else "",
                    index,
                    data.shop?.id ?: -1,
                    "",
                    data.shop?.name ?: "",
                    "",
                    1,
                    title ?: "",
                    pageName ?: "",
                    data.minOrder ?: 1,
                    data.shop?.city ?: "",
                    data.badges?.map { it.imageUrl } ?: emptyList(),
                    layoutType ?: "",
                    data.freeOngkirInformation?.isActive?:false,
                    data.freeOngkirInformation?.imageUrl?:"",
                    labelGroupList,
                    false
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