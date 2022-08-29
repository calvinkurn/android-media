package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

data class MilestoneWidgetUiModel(
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
    override var data: MilestoneDataUiModel?,
    override var impressHolder: ImpressHolder = ImpressHolder(),
    override var isLoaded: Boolean,
    override var isLoading: Boolean,
    override var isFromCache: Boolean,
    override var isNeedToBeRemoved: Boolean = false,
    override var showLoadingState: Boolean = false,
    override var emptyState: WidgetEmptyStateUiModel,
    var isAlreadyMinimized: Boolean = false
) : BaseWidgetUiModel<MilestoneDataUiModel> {

    override fun copyWidget(): BaseWidgetUiModel<MilestoneDataUiModel> {
        return this.copy()
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<MilestoneDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun getSeeMoreCtaApplink(): String {
        var applink = data?.milestoneCta?.appLink.orEmpty()
        if (applink.isBlank()) {
            applink = this.appLink
        }
        return applink
    }

    fun getSeeMoreCtaText(): String {
        var ctaText = data?.milestoneCta?.text.orEmpty()
        if (ctaText.isBlank()) {
            ctaText = this.ctaText
        }
        return ctaText
    }
}