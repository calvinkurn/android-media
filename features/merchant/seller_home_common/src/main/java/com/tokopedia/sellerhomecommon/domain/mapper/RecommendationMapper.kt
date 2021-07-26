package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
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

    companion object {
        private const val TICKER_TYPE_INFO = 1
        private const val TICKER_TYPE_WARNING = 2

        private const val RECOMMENDATION_TYPE_POSITIVE = 3
        private const val RECOMMENDATION_TYPE_NEGATIVE = 2
    }

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
                    title = it.title.orEmpty(),
                    recommendations = it.list.map { item ->
                        RecommendationItemUiModel(
                                text = item.text.orEmpty(),
                                appLink = item.appLink.orEmpty(),
                                type = when (item.type) {
                                    RECOMMENDATION_TYPE_POSITIVE -> RecommendationItemUiModel.TYPE_POSITIVE
                                    RECOMMENDATION_TYPE_NEGATIVE -> RecommendationItemUiModel.TYPE_NEGATIVE
                                    else -> RecommendationItemUiModel.TYPE_NO_DATA
                                }
                        )
                    }
            )
        }
        return null
    }

    private fun getTicker(ticker: RecommendationTicker?): RecommendationTickerUiModel? {
        ticker?.let {
            return RecommendationTickerUiModel(
                    text = it.text.orEmpty(),
                    type = when (ticker.type) {
                        TICKER_TYPE_INFO -> RecommendationTickerUiModel.TYPE_INFO
                        TICKER_TYPE_WARNING -> RecommendationTickerUiModel.TYPE_WARNING
                        else -> RecommendationTickerUiModel.TYPE_ERROR
                    }
            )
        }
        return null
    }

    private fun getRecommendationProgress(progressLevel: RecommendationProgressModel?): RecommendationProgressUiModel? {
        progressLevel?.let {
            return RecommendationProgressUiModel(
                    isShown = it.isShown.orFalse(),
                    text = it.text.orEmpty(),
                    bar = RecommendationProgressUiModel.ProgressBar(
                            value = it.bar?.value.orZero(),
                            maxValue = it.bar?.maxValue.orZero(),
                            valueToDisplay = it.bar?.valueToDisplay.orEmpty()
                    )
            )
        }
        return null
    }
}