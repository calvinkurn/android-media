package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 10/08/20
 */

data class TickerWidgetUiModel(
        override val id: String = "",
        override val widgetType: String = "",
        override val title: String = "",
        override val subtitle: String = "",
        override val tooltip: TooltipUiModel? = null,
        override val appLink: String = "",
        override val dataKey: String = "",
        override val ctaText: String = "",
        override val isShowEmpty: Boolean = false,
        override var data: TickerDataUiModel? = null,
        override var impressHolder: ImpressHolder = ImpressHolder(),
        override var isLoaded: Boolean = false,
        override var isLoading: Boolean = false,
        override var isFromCache: Boolean = false,
        override var emptyState: WidgetEmptyStateUiModel = WidgetEmptyStateUiModel()
) : BaseWidgetUiModel<TickerDataUiModel> {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun copy(): BaseWidgetUiModel<TickerDataUiModel> {
        return TickerWidgetUiModel(id, widgetType, title, subtitle, tooltip, appLink, dataKey, ctaText, isShowEmpty, data, impressHolder, isLoaded, isLoading, isFromCache, emptyState)
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<TickerDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }
}