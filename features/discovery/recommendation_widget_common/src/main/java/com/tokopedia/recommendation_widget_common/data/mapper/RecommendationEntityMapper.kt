package com.tokopedia.recommendation_widget_common.data.mapper

import com.crashlytics.android.Crashlytics
import com.tokopedia.kotlin.util.ContainNullException
import com.tokopedia.kotlin.util.isContainNull
import com.tokopedia.recommendation_widget_common.BuildConfig

import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

import java.util.ArrayList

import rx.functions.Func1

/**
 * Created by devara fikry on 16/04/19.
 */

class RecommendationEntityMapper : Func1<List<RecomendationEntity.RecomendationData>,
        List<RecommendationWidget>> {
    override fun call(recommendations: List<RecomendationEntity.RecomendationData>): List<RecommendationWidget> {
        isContainNull(recommendations) {
            val exception = ContainNullException("Found $it in ${RecommendationEntityMapper::class.java.simpleName}")
            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(exception)
            }
            throw exception
        }

        return mappingToRecommendationModel(recommendations)
    }

    companion object {
        fun mappingToRecommendationModel(recommendations: List<RecomendationEntity.RecomendationData>): List<RecommendationWidget> {
            val recommendationWidgetList = arrayListOf<RecommendationWidget>()
            val itemList = arrayListOf<RecommendationItem>()

            recommendationWidgetList.addAll(
                    recommendations.map { convertToRecommendationWidget(it) }
            )

            return recommendationWidgetList
        }

        private fun convertToRecommendationWidget(recomendationData: RecomendationEntity.RecomendationData): RecommendationWidget {
            val recommendationItemList = arrayListOf<RecommendationItem>()
            recommendationItemList.addAll(
                    recomendationData.recommendation?.mapIndexed { index, recommendation -> convertToRecommendationItem(recommendation, index + 1) } ?: emptyList())
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

        private fun convertToRecommendationItem(data: RecomendationEntity.Recommendation, position: Int): RecommendationItem {
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
                    data.slashedPrice?:"",
                    data.slashedPriceInt,
                    data.discountPercentage,
                    position,
                    data.shop?.id ?: 0,
                    data.minOrder ?: 1
            )

        }

    }
}
