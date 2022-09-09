package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.common.DismissibleState
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class PostListWidgetUiModel(
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
    override var data: PostListDataUiModel?,
    override var impressHolder: ImpressHolder = ImpressHolder(),
    override var isLoaded: Boolean,
    override var isLoading: Boolean,
    override var isFromCache: Boolean,
    override var isNeedToBeRemoved: Boolean = false,
    override var showLoadingState: Boolean = false,
    override var emptyState: WidgetEmptyStateUiModel,
    override val isDismissible: Boolean = false,
    override val dismissibleState: DismissibleState = DismissibleState.NONE,
    override val dismissToken: String = String.EMPTY,
    override var shouldShowDismissalTimer: Boolean = false,
    val postFilter: List<WidgetFilterUiModel>,
    val maxData: Int,
    val maxDisplay: Int,
    var isCheckingMode: Boolean = false
) : BaseDismissibleWidgetUiModel<PostListDataUiModel> {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun copyWidget(): BaseWidgetUiModel<PostListDataUiModel> {
        return this.copy()
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<PostListDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }
}