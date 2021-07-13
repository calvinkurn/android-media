package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class ProgressWidgetUiModel(
        override val id: String,
        override val widgetType: String,
        override val title: String,
        override val appLink: String,
        override val subtitle: String,
        override val tooltip: TooltipUiModel?,
        override val dataKey: String,
        override val ctaText: String,
        override val gridSize: Int,
        override val isShowEmpty: Boolean,
        override var data: ProgressDataUiModel?,
        override var impressHolder: ImpressHolder = ImpressHolder(),
        override var isLoaded: Boolean,
        override var isLoading: Boolean,
        override var isFromCache: Boolean,
        override var isNeedToBeRemoved: Boolean = false,
        override var emptyState: WidgetEmptyStateUiModel
) : BaseWidgetUiModel<ProgressDataUiModel> {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun copyWidget(): BaseWidgetUiModel<ProgressDataUiModel> {
        return this.copy()
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<ProgressDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }
}