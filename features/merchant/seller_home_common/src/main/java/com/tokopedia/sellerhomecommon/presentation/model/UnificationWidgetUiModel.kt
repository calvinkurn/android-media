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
    override var data: BaseDataUiModel?,
    override var impressHolder: ImpressHolder,
    override var isLoaded: Boolean,
    override var isLoading: Boolean,
    override var isFromCache: Boolean,
    override var isNeedToBeRemoved: Boolean,
    override var emptyState: WidgetEmptyStateUiModel,
    override var showLoadingState: Boolean

) : BaseWidgetUiModel<BaseDataUiModel> {

    override fun copyWidget(): BaseWidgetUiModel<BaseDataUiModel> {
        return this.copyWidget()
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<BaseDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}