package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.sellerhomecommon.domain.model.RecommendationModel
import com.tokopedia.sellerhomecommon.domain.model.RecommendationProgressModel
import com.tokopedia.sellerhomecommon.domain.model.RecommendationTicker
import com.tokopedia.sellerhomecommon.domain.model.RecommendationWidgetDataModel
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 06/04/21
 */

class RecommendationMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(data: List<RecommendationWidgetDataModel>, isFromCache: Boolean): List<RecommendationDataUiModel> {
        return data.map {
            RecommendationDataUiModel(
                    dataKey = it.dataKey.orEmpty(),
                    error = it.errorMsg.orEmpty(),
                    isFromCache = isFromCache,
                    showWidget = it.shouldShowWidget.orTrue(),
                    ticker = getTicker(it.data?.ticker),
                    progressLevel = getRecommendationProgress(it.data?.progressLevel),
                    progressBar = getRecommendationProgress(it.data?.progressBar),
                    recommendation = getRecommendation(it.data?.recommendation)
            )
        }
    }

    private fun getRecommendation(recommendation: RecommendationModel?): RecommendationUiModel? {
        recommendation?.let {
            return RecommendationUiModel(
                    title = it.title,
                    recommendations = it.list.map { item ->
                        RecommendationItemUiModel(
                                text = item.text,
                                appLink = item.appLink,
                                type = item.type.toString()
                        )
                    }
            )
        }
        return null
    }

    private fun getTicker(ticker: RecommendationTicker?): RecommendationTickerUiModel? {
        ticker?.let {
            return RecommendationTickerUiModel(
                    type = it.type.toString(),
                    text = it.text
            )
        }
        return null
    }

    private fun getRecommendationProgress(progressLevel: RecommendationProgressModel?): RecommendationProgressUiModel? {
        progressLevel?.let {
            return RecommendationProgressUiModel(
                    isShown = it.isShown,
                    text = it.text,
                    bar = RecommendationProgressUiModel.ProgressBar(
                            value = it.bar.value,
                            maxValue = it.bar.maxValue
                    )
            )
        }
        return null
    }
}