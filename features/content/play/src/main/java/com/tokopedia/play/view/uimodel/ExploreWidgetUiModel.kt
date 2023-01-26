package com.tokopedia.play.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.analytic.TrackingField
import com.tokopedia.play.widget.ui.model.PlayCardShimmering
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play_common.model.result.ResultState

/**
 * @author by astidhiyaa on 28/11/22
 */
data class ExploreWidgetUiModel(
    val param: WidgetParamUiModel,
    val chips: TabMenuUiModel,
    val widgets: List<WidgetItemUiModel>,
    val state: WidgetState
) {
    companion object {
        val Empty: ExploreWidgetUiModel
            get() = ExploreWidgetUiModel(
                chips = TabMenuUiModel.Empty,
                param = WidgetParamUiModel.Empty,
                widgets = emptyList(),
                state = WidgetState.Loading
            )
    }
}

data class WidgetParamUiModel(
    val group: String,
    val sourceType: String,
    val sourceId: String,
    val cursor: String
) {
    companion object {
        val Empty: WidgetParamUiModel
            get() = WidgetParamUiModel(
                group = "",
                sourceId = "",
                sourceType = "",
                cursor = ""
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

data class WidgetItemUiModel(
    val item: PlayWidgetUiModel
) : WidgetUiModel() {
    companion object {
        val Empty: WidgetItemUiModel
            get() = WidgetItemUiModel(item = PlayWidgetUiModel.Empty)
    }
}

object SubSlotUiModel : WidgetUiModel()

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

val List<WidgetUiModel>.getChannelBlock: WidgetItemUiModel
    get() {
        val newList = mutableListOf<List<PlayWidgetItemUiModel>>()
        val filteredList = this.filterIsInstance<WidgetItemUiModel>()
        filteredList.forEach {
            newList.add(it.item.items)
        }
        val config = filteredList.firstOrNull() ?: WidgetItemUiModel.Empty
        return WidgetItemUiModel(item = PlayWidgetUiModel(items = newList.flatten(), title = config.item.title, config = config.item.config, actionTitle = config.item.actionTitle, actionAppLink = config.item.actionAppLink, isActionVisible = config.item.isActionVisible, background = config.item.background))
    }

val List<WidgetUiModel>.getChannelBlocks: List<WidgetItemUiModel>
    get() {
        return this.filterIsInstance<WidgetItemUiModel>()
    }

val List<WidgetUiModel>.getChips: TabMenuUiModel
    get() {
        return this.filterIsInstance<TabMenuUiModel>().firstOrNull() ?: TabMenuUiModel.Empty
    }

val List<WidgetUiModel>.getConfig: PageConfig
    get() {
        return this.filterIsInstance<PageConfig>().firstOrNull() ?: PageConfig.Empty
    }

internal val getWidgetShimmering: List<PlayWidgetItemUiModel>
    get() {
        return List(6) {
            PlayCardShimmering
        }
    }

internal val getChipsShimmering: List<ChipsShimmering>
    get() {
        return List(6) {
            ChipsShimmering
        }
    }

sealed class WidgetState {
    object Success : WidgetState()
    object Loading : WidgetState()
    object Empty : WidgetState()
    data class Fail(val error: Throwable) : WidgetState()

    val isSuccess: Boolean
        get() = this == Success

    val isLoading: Boolean
        get() = this == Loading

    val isFail: Boolean
        get() = this is Fail
}
