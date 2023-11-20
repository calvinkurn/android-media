package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 27/10/20
 */

data class MultiLineGraphWidgetUiModel(
    override val id: String,
    override val sectionId: String,
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
    override var data: MultiLineGraphDataUiModel?,
    override var impressHolder: ImpressHolder = ImpressHolder(),
    override var isLoaded: Boolean,
    override var isLoading: Boolean,
    override var isFromCache: Boolean,
    override var isNeedToBeRemoved: Boolean = false,
    override var showLoadingState: Boolean = false,
    override var emptyState: WidgetEmptyStateUiModel,
    override var useRealtime: Boolean = false,
    val isComparePeriodOnly: Boolean,
    var isMultiComponentWidget: Boolean = false
) : BaseWidgetUiModel<MultiLineGraphDataUiModel> {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun copyWidget(): BaseWidgetUiModel<MultiLineGraphDataUiModel> {
        return this.copy()
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<MultiLineGraphDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }
}
