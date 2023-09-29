package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created by @ilhamsuaib on 06/07/22.
 */

data class UnificationWidgetUiModel(
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
    override var data: UnificationDataUiModel?,
    override var isLoaded: Boolean,
    override var isLoading: Boolean,
    override var isFromCache: Boolean,
    override var emptyState: WidgetEmptyStateUiModel,
    override var impressHolder: ImpressHolder = ImpressHolder(),
    override var isNeedToBeRemoved: Boolean = false,
    override var showLoadingState: Boolean = false,
    override var useRealtime: Boolean = false,
) : BaseWidgetUiModel<UnificationDataUiModel> {

    override fun copyWidget(): BaseWidgetUiModel<UnificationDataUiModel> {
        return this.copy()
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<UnificationDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
