package com.tokopedia.recommendation_widget_common.data.mapper

import com.tokopedia.kotlin.util.throwIfNull
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import rx.functions.Func1

/**
 * Created by devara fikry on 16/04/19.
 */

class RecommendationEntityMapper : Func1<List<RecomendationEntity.RecomendationData>,
        List<RecommendationWidget>> {
    override fun call(recommendations: List<RecomendationEntity.RecomendationData>): List<RecommendationWidget> {
        throwIfNull(recommendations, RecommendationEntityMapper::class.java)
        return mappingToRecommendationModel(recommendations)
    }

    companion object {
        fun mappingToRecommendationModel(recommendations: List<RecomendationEntity.RecomendationData>): List<RecommendationWidget> {
            val recommendationWidgetList = arrayListOf<RecommendationWidget>()

            recommendationWidgetList.addAll(
                    recommendations.map { convertToRecommendationWidget(it) }
            )

            return recommendationWidgetList
        }

        private fun convertToRecommendationWidget(recomendationData: RecomendationEntity.RecomendationData): RecommendationWidget {
            val recommendationItemList = arrayListOf<RecommendationItem>()
            recommendationItemList.addAll(
                    recomendationData.recommendation?.mapIndexed { index, recommendation ->
                        convertToRecommendationItem(
                                recommendation,
                                recomendationData.title ?: "",
                                recomendationData.pageName ?: "",
                                index + 1,
                                recomendationData.layoutType ?: "")
                    } ?: emptyList())
            return RecommendationWidget(
                    recommendationItemList,
                    recomendationData.title ?: "",
                    recomendationData.foreignTitle ?: "",
                    recomendationData.source ?: "",
                    recomendationData.tid ?: "",
                    recomendationData.widgetUrl ?: "",
                    recomendationData.layoutType?:"",
                    recomendationData.pagination.currentPage,
                    recomendationData.pagination.nextPage,
                    recomendationData.pagination.prevPage,
                    recomendationData.pagination.hasNext,
                    recomendationData.pageName?:"")
        }

        private fun convertToRecommendationItem(
                data: RecomendationEntity.Recommendation,
                title: String,
                pageName: String,
                position: Int,
                layoutType: String): RecommendationItem {
            return RecommendationItem(
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
                    position,
                    data.shop?.id ?: -1,
                    "",
                    data.shop?.name ?: "",
                    -1,
                    1,
                    title,
                    pageName,
                    data.minOrder ?: 1,
                    data.shop?.city ?: "",
                    data.badges?.map { it.imageUrl } ?: emptyList(),
                    layoutType

            )

        }

    }
}
