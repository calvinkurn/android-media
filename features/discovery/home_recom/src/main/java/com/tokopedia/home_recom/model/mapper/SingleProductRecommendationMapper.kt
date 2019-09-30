package com.tokopedia.home_recom.model.mapper

import com.tokopedia.home_recom.model.entity.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

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
                    index,
                    data.shop?.id ?: -1,
                    "",
                    data.shop?.name ?: "",
                    -1,
                    1,
                    title ?: "",
                    pageName ?: "",
                    data.minOrder ?: 1,
                    data.shop?.city ?: "",
                    data.badges?.map { it.imageUrl } ?: emptyList(),
                    layoutType ?: ""

            )
         } ?: emptyList()
    }
}