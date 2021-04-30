package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 29/07/20
 */

class WhiteSpaceUiModel(
        override val id: String = "",
        override val widgetType: String = "",
        override val title: String = "",
        override val subtitle: String = "",
        override val tooltip: TooltipUiModel? = null,
        override val appLink: String = "",
        override val dataKey: String = "",
        override val ctaText: String = "",
        override val isShowEmpty: Boolean = false,
        override var data: BaseDataUiModel? = null,
        override var impressHolder: ImpressHolder = ImpressHolder(),
        override var isLoaded: Boolean = true,
        override var isLoading: Boolean = false,
        override var isFromCache: Boolean = false,
        override var isNeedToBeRemoved: Boolean = false,
        override var emptyState: WidgetEmptyStateUiModel = WidgetEmptyStateUiModel()
) : BaseWidgetUiModel<BaseDataUiModel> {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun copy(): BaseWidgetUiModel<BaseDataUiModel> {
        return WhiteSpaceUiModel(id, widgetType, title, subtitle, tooltip, appLink, dataKey, ctaText, isShowEmpty, data, impressHolder, isLoaded, isLoading, isFromCache, isNeedToBeRemoved, emptyState)
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<BaseDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }
}