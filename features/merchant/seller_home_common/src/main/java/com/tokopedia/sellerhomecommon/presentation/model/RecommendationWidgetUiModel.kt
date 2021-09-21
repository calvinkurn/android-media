package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 05/04/21
 */

data class RecommendationWidgetUiModel(
        override val id: String,
        override val widgetType: String,
        override val title: String,
        override val subtitle: String,
        override val tooltip: TooltipUiModel?,
        override val appLink: String,
        override val dataKey: String,
        override val ctaText: String,
        override val isShowEmpty: Boolean,
        override var data: RecommendationDataUiModel?,
        override var isLoaded: Boolean,
        override var isLoading: Boolean,
        override var isFromCache: Boolean,
        override var emptyState: WidgetEmptyStateUiModel,
        override var impressHolder: ImpressHolder = ImpressHolder(),
        override var isNeedToBeRemoved: Boolean = false
) : BaseWidgetUiModel<RecommendationDataUiModel> {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun copy(): BaseWidgetUiModel<RecommendationDataUiModel> {
        return RecommendationWidgetUiModel(id, widgetType, title, subtitle, tooltip, appLink, dataKey, ctaText, isShowEmpty, data, isLoaded, isLoading, isFromCache, emptyState, impressHolder, isNeedToBeRemoved)
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<RecommendationDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }
}