package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 19/05/20
 */

interface BaseWidgetUiModel<T : BaseDataUiModel> : Visitable<WidgetAdapterFactory> {
    val id: String
    val widgetType: String
    val title: String
    val subtitle: String
    val tooltip: TooltipUiModel?
    val tag: String
    val appLink: String
    val dataKey: String
    val ctaText: String
    val gridSize: Int
    val isShowEmpty: Boolean
    var data: T?
    var impressHolder: ImpressHolder
    var isLoaded: Boolean
    var isLoading: Boolean
    var isFromCache: Boolean
    var isNeedToBeRemoved: Boolean
    var emptyState: WidgetEmptyStateUiModel

    fun copyWidget(): BaseWidgetUiModel<T>

    fun needToRefreshData(other: BaseWidgetUiModel<T>): Boolean

    fun shouldShowEmptyStateIfEmpty(): Boolean {
        return emptyState.title.isNotBlank() && emptyState.description.isNotBlank()
                && emptyState.ctaText.isNotBlank() && emptyState.appLink.isNotBlank()
    }

    fun isEmpty(): Boolean = data == null || data?.isWidgetEmpty().orFalse()

}

data class WidgetEmptyStateUiModel(
    val imageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val ctaText: String = "",
    val appLink: String = ""
)