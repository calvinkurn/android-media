package com.tokopedia.recommendation_widget_common.data.mapper

import com.tokopedia.kotlin.util.throwIfNull
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import rx.functions.Func1

/**
 * Created by devara fikry on 16/04/19.
 */

class RecommendationEntityMapper : Func1<List<RecommendationEntity.RecomendationData>,
        List<RecommendationWidget>> {

    override fun call(recommendations: List<RecommendationEntity.RecomendationData>): List<RecommendationWidget> {
        throwIfNull(recommendations, RecommendationEntityMapper::class.java)
        return mappingToRecommendationModel(recommendations)
    }

    companion object {

        fun mappingToRecommendationModel(recommendations: List<RecommendationEntity.RecomendationData>): List<RecommendationWidget> {
            val recommendationWidgetList = arrayListOf<RecommendationWidget>()

            recommendationWidgetList.addAll(
                    recommendations.map { convertToRecommendationWidget(it) }
            )

            return recommendationWidgetList
        }

        fun convertToRecommendationWidget(recommendationData: RecommendationEntity.RecomendationData): RecommendationWidget {
            val recommendationItemList = arrayListOf<RecommendationItem>()
            recommendationItemList.addAll(
                    recommendationData.recommendation?.mapIndexed { index, recommendation ->
                        convertToRecommendationItem(
                                recommendation,
                                recommendationData.title ?: "",
                                recommendationData.pageName ?: "",
                                index + 1,
                                recommendationData.layoutType ?: "")
                    } ?: emptyList())
            return RecommendationWidget(
                    recommendationItemList,
                    recommendationData.title ?: "",
                    recommendationData.foreignTitle ?: "",
                    recommendationData.source ?: "",
                    recommendationData.tid ?: "",
                    recommendationData.widgetUrl ?: "",
                    recommendationData.layoutType?:"",
                    recommendationData.seeMoreAppLink ?: "",
                    recommendationData.pagination.currentPage,
                    recommendationData.pagination.nextPage,
                    recommendationData.pagination.prevPage,
                    recommendationData.pagination.hasNext,
                    recommendationData.pageName?:"")
        }

        private fun convertToRecommendationItem(
                data: RecommendationEntity.Recommendation,
                title: String,
                pageName: String,
                position: Int,
                layoutType: String): RecommendationItem {

            val labelGroupList = data.labelGroups?.map {
                RecommendationLabel(title = it.title ?: "", type = it.type ?: "", position = it.position)
            } ?: listOf()

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
                    data.price ?: "0",
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
                    position,
                    data.shop?.id ?: -1,
                    "",
                    data.shop?.name ?: "",
                    "",
                    1,
                    title,
                    pageName,
                    data.minOrder ?: 1,
                    data.shop?.city ?: "",
                    data.badges?.map { it.imageUrl } ?: emptyList(),
                    layoutType,
                    data.freeOngkirInformation?.isActive?:false,
                    data.freeOngkirInformation?.imageUrl?:"",
                    labelGroupList,
                    data.shop?.isGold ?: false
            )

        }

        private fun isLabelDiscountVisible(productItem: RecommendationEntity.Recommendation): Boolean {
            return productItem.discountPercentage > 0
        }
    }
}
