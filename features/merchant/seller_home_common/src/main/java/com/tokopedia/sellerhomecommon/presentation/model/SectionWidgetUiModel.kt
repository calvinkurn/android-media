package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class SectionWidgetUiModel(
        override val id: String,
        override val widgetType: String,
        override val title: String,
        override val subtitle: String,
        override val tooltip: TooltipUiModel?,
        override val appLink: String,
        override val dataKey: String,
        override val ctaText: String,
        override val isShowEmpty: Boolean,
        override var data: BaseDataUiModel?,
        override var impressHolder: ImpressHolder = ImpressHolder(),
        override var isLoaded: Boolean,
        override var isLoading: Boolean,
        override var isFromCache: Boolean,
        override var isNeedToBeRemoved: Boolean = false,
        override var emptyState: WidgetEmptyStateUiModel,
        var shouldShow: Boolean = true
) : BaseWidgetUiModel<BaseDataUiModel> {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun copy(): BaseWidgetUiModel<BaseDataUiModel> {
        return SectionWidgetUiModel(id, widgetType, title, subtitle, tooltip, appLink, dataKey, ctaText, isShowEmpty, data, impressHolder, isLoaded, isLoading, isFromCache, isNeedToBeRemoved, emptyState)
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<BaseDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }
}