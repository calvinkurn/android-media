package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 05/04/21
 */

data class RecommendationDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel(),
    val ticker: RecommendationTickerUiModel? = null,
    val progressLevel: RecommendationProgressUiModel? = null,
    val progressBar: RecommendationProgressUiModel? = null,
    val recommendation: RecommendationUiModel? = null
) : BaseDataUiModel, LastUpdatedDataInterface {

    override fun isWidgetEmpty(): Boolean {
        return recommendation?.recommendations.isNullOrEmpty()
    }
}

data class RecommendationTickerUiModel(
    val type: String,
    val text: String
) {

    companion object {
        const val TYPE_INFO = "info"
        const val TYPE_WARNING = "warning"
        const val TYPE_ERROR = "error"
    }
}

data class RecommendationProgressUiModel(
    val isShown: Boolean,
    val text: String,
    val bar: ProgressBar
) {

    data class ProgressBar(
        val value: Int,
        val maxValue: Int,
        val valueToDisplay: String
    )
}

data class RecommendationUiModel(
    val title: String,
    val recommendations: List<RecommendationItemUiModel>
)

data class RecommendationItemUiModel(
    val text: String,
    val appLink: String,
    val type: String
) {

    companion object {
        const val TYPE_POSITIVE = "positive"
        const val TYPE_NEGATIVE = "negative"
        const val TYPE_NO_DATA = "no_data"
    }
}