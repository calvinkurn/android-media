package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

data class MultiComponentWidgetUiModel(
    override val id: String,
    override val widgetType: String,
    override val title: String,
    override val subtitle: String,
    override val tooltip: TooltipUiModel?,
    override val tag: String,
    override val appLink: String,
    override val dataKey: String,
    override val ctaText: String,
    override val gridSize: Int,
    override val isShowEmpty: Boolean,
    override var data: MultiComponentDataUiModel?,
    override var impressHolder: ImpressHolder = ImpressHolder(),
    override var isLoaded: Boolean,
    override var isLoading: Boolean,
    override var isFromCache: Boolean,
    override var isNeedToBeRemoved: Boolean = false,
    override var showLoadingState: Boolean = false,
    override var emptyState: WidgetEmptyStateUiModel,
    override var useRealtime: Boolean = false,
    override val sectionId: String,
    var isInitialLoad:Boolean = true,
): BaseWidgetUiModel<MultiComponentDataUiModel> {

    override fun copyWidget(): BaseWidgetUiModel<MultiComponentDataUiModel> {
        return this.copy()
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<MultiComponentDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
