package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

data class MilestoneWidgetUiModel(
    override val id: String,
    override val widgetType: String,
    override val title: String,
    override val subtitle: String,
    override val tooltip: TooltipUiModel?,
    override val appLink: String,
    override val dataKey: String,
    override val ctaText: String,
    override val isShowEmpty: Boolean,
    override var data: MilestoneDataUiModel?,
    override var impressHolder: ImpressHolder = ImpressHolder(),
    override var isLoaded: Boolean,
    override var isLoading: Boolean,
    override var isFromCache: Boolean,
    override var isNeedToBeRemoved: Boolean = false,
    override var emptyState: WidgetEmptyStateUiModel
) : BaseWidgetUiModel<MilestoneDataUiModel> {

    override fun copy(): BaseWidgetUiModel<MilestoneDataUiModel> {
        return MilestoneWidgetUiModel(
            id,
            widgetType,
            title,
            subtitle,
            tooltip,
            appLink,
            dataKey,
            ctaText,
            isShowEmpty,
            data,
            impressHolder,
            isLoaded,
            isLoading,
            isFromCache,
            isNeedToBeRemoved,
            emptyState
        )
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<MilestoneDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}