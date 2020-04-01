package com.tokopedia.recommendation_widget_common.data.mapper

import com.tokopedia.kotlin.util.throwIfNull
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
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
        private const val LABEL_POSITION_OFFERS = "offers"
        private const val LABEL_POSITION_PROMO = "promo"
        private const val LABEL_POSITION_CREDIBILITY = "credibility"

        fun mappingToRecommendationModel(recommendations: List<RecomendationEntity.RecomendationData>): List<RecommendationWidget> {
            val recommendationWidgetList = arrayListOf<RecommendationWidget>()

            recommendationWidgetList.addAll(
                    recommendations.map { convertToRecommendationWidget(it) }
            )

            return recommendationWidgetList
        }

        fun convertToRecommendationWidget(recomendationData: RecomendationEntity.RecomendationData): RecommendationWidget {
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
                    recomendationData.seeMoreAppLink ?: "",
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

            val labelCredibility = RecommendationLabel()
            val labelPromo = RecommendationLabel()
            val labelOffers = RecommendationLabel()

            data.labelGroups?.let {
                for (label: RecomendationEntity.Recommendation.LabelGroup in it){
                    when(label.position){
                        LABEL_POSITION_CREDIBILITY -> {
                            labelCredibility.title = label.title?:""
                            labelCredibility.title = label.type?:""
                        }
                        LABEL_POSITION_PROMO -> {
                            labelPromo.title = label.title?:""
                            labelPromo.title = label.type?:""
                        }
                        LABEL_POSITION_OFFERS -> {
                            labelOffers.title = label.title?:""
                            labelOffers.title = label.type?:""
                        }
                    }
                }
            }

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
                    if (isLabelDiscountVisible(data)) "${data.discountPercentage}%" else "",
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
                    layoutType,
                    data.freeOngkirInformation?.isActive?:false,
                    data.freeOngkirInformation?.imageUrl?:"",
                    labelPromo,
                    labelOffers,
                    labelCredibility,
                    data.shop?.isGold ?: false
            )

        }

        private fun isLabelDiscountVisible(productItem: RecomendationEntity.Recommendation): Boolean {
            return productItem.discountPercentage > 0
        }
    }
}
