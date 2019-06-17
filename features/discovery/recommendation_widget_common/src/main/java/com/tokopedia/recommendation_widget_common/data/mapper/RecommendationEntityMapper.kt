package com.tokopedia.recommendation_widget_common.data.mapper

import com.crashlytics.android.Crashlytics
import com.tokopedia.kotlin.util.ContainNullException
import com.tokopedia.kotlin.util.isContainNull
import com.tokopedia.recommendation_widget_common.BuildConfig

import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationModel

import java.util.ArrayList

import rx.functions.Func1

/**
 * Created by devara fikry on 16/04/19.
 */

class RecommendationEntityMapper : Func1<RecomendationEntity.RecomendationData, RecommendationModel> {
    override fun call(recomendationData: RecomendationEntity.RecomendationData): RecommendationModel {
        isContainNull(recomendationData) {
            val exception = ContainNullException("Found $it in ${RecommendationEntityMapper::class.java.simpleName}")
            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(exception)
            }
            throw exception
        }

        return mappingToRecommendationModel(recomendationData)
    }

    companion object {
        fun mappingToRecommendationModel(recomendationData: RecomendationEntity.RecomendationData): RecommendationModel {
            val modelList = ArrayList<RecommendationItem>()
            modelList.addAll(recomendationData.recommendation?.map { convertToRecommendationItem(it) } ?: emptyList())

            return RecommendationModel(modelList,
                    recomendationData.title ?: "",
                    recomendationData.foreignTitle ?: "",
                    recomendationData.source ?: "",
                    recomendationData.tid ?: "",
                    recomendationData.widgetUrl ?: "")
        }

        private fun convertToRecommendationItem(data: RecomendationEntity.Recommendation): RecommendationItem {
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
                    data.discountPercentage
            )

        }

    }
}
