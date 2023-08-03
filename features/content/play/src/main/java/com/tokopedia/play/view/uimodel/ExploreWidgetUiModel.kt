package com.tokopedia.play.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.analytic.TrackingField
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetShimmerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play_common.model.result.ResultState

/**
 * @author by astidhiyaa on 28/11/22
 */
data class ExploreWidgetUiModel(
    val chips: TabMenuUiModel,
    val widgets: List<ExploreWidgetItemUiModel>,
    val state: ExploreWidgetState
) {
    companion object {
        val Empty: ExploreWidgetUiModel
            get() = ExploreWidgetUiModel(
                chips = TabMenuUiModel.Empty,
                widgets = emptyList(),
                state = ExploreWidgetState.Loading
            )
    }
}

data class WidgetParamUiModel(
    val group: String,
    val sourceType: String,
    val sourceId: String,
    val cursor: String = "",
    val isRefresh: Boolean = false,
) {
    companion object {
        val Empty: WidgetParamUiModel
            get() = WidgetParamUiModel(
                group = "",
                sourceId = "",
                sourceType = "",
                cursor = "",
                isRefresh = false,
            )
    }
}

sealed class ChipWidgetsUiModel
data class ChipWidgetUiModel(
    val isSelected: Boolean = false,
    val group: String,
    val sourceType: String,
    val sourceId: String,
    val text: String,
    @TrackingField val impressHolder: ImpressHolder = ImpressHolder()
) : ChipWidgetsUiModel()

object ChipsShimmering : ChipWidgetsUiModel()

sealed class WidgetUiModel
data class TabMenuUiModel(
    val items: List<ChipWidgetUiModel>,
    val state: ResultState
) : WidgetUiModel() {
    companion object {
        val Empty: TabMenuUiModel
            get() = TabMenuUiModel(
                items = emptyList(),
                state = ResultState.Loading
            )
    }
}

data class ExploreWidgetItemUiModel(
    val id: Long,
    val item: PlayWidgetUiModel
) : WidgetUiModel() {
    companion object {
        val Empty: ExploreWidgetItemUiModel
            get() = ExploreWidgetItemUiModel(item = PlayWidgetUiModel.Empty, id = 0L)
    }
}

object SubSlotUiModel : WidgetUiModel()
object ExploreWidgetPlaceholder : WidgetUiModel()

data class PageConfig(val cursor: String, val isAutoPlay: Boolean) :
    WidgetUiModel() {
    companion object {
        val Empty: PageConfig
            get() = PageConfig(cursor = "", isAutoPlay = false)
    }
}

val List<WidgetUiModel>.isSubSlotAvailable: Boolean
    get() {
        return this.any {
            it is SubSlotUiModel
        }
    }

val WidgetParamUiModel.hasNextPage: Boolean
    get() {
        return this.cursor.isNotEmpty()
    }

val List<WidgetUiModel>.getChannelBlocks: List<ExploreWidgetItemUiModel>
    get() {
        return this.filterIsInstance<ExploreWidgetItemUiModel>().distinctBy { it.item.items }
    }

val List<ExploreWidgetItemUiModel>.getChannelCards: List<PlayWidgetItemUiModel>
    get() {
        val list = this
        return list.flatMap { it.item.items }
    }

val List<WidgetUiModel>.getChips: TabMenuUiModel
    get() {
        return this.filterIsInstance<TabMenuUiModel>().firstOrNull() ?: TabMenuUiModel.Empty
    }

internal val List<WidgetUiModel>.getConfig: PageConfig
    get() {
        return this.filterIsInstance<PageConfig>().firstOrNull() ?: PageConfig.Empty
    }

internal val getWidgetShimmering: List<ExploreWidgetPlaceholder>
    get() {
        return List(6) {
            ExploreWidgetPlaceholder
        }
    }

internal val getChipsShimmering: List<ChipsShimmering>
    get() {
        return List(6) {
            ChipsShimmering
        }
    }

internal val getCategoryShimmering: List<PlayWidgetShimmerUiModel>
    get() = List(6) {
        PlayWidgetShimmerUiModel
    }

sealed class ExploreWidgetState {
    object Success : ExploreWidgetState()
    object Loading : ExploreWidgetState()
    object Empty : ExploreWidgetState()
    data class Fail(val error: Throwable, val onRetry: () -> Unit = {}) : ExploreWidgetState()

    val isSuccess: Boolean
        get() = this == Success

    val isLoading: Boolean
        get() = this == Loading

    val isFail: Boolean
        get() = this is Fail

    val isEmpty: Boolean
        get() = this is Empty
}

enum class ExploreWidgetType {
    Default,
    Category;
}

data class CategoryWidgetUiModel(
    val data: List<PlayWidgetItemUiModel>,
    val state: ExploreWidgetState,
) {
    companion object {
        val Empty get() = CategoryWidgetUiModel(data = emptyList(), state = ExploreWidgetState.Loading)
    }
}
