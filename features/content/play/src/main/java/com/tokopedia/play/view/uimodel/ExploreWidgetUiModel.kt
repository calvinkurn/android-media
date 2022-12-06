package com.tokopedia.play.view.uimodel

import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * @author by astidhiyaa on 28/11/22
 */
data class ExploreWidgetUiModel(
    val param: WidgetParamUiModel,
    val chips: List<ChipWidgetUiModel>,
    val widgets: List<WidgetItemUiModel>,
) {
    companion object {
        val Empty: ExploreWidgetUiModel
            get() = ExploreWidgetUiModel(
                chips = emptyList(),
                param = WidgetParamUiModel.Empty,
                widgets = emptyList(),
            )
    }
}

data class WidgetParamUiModel(
    val group: String,
    val sourceType: String,
    val sourceId: String,
    val cursor: String,
) {
    companion object {
        val Empty : WidgetParamUiModel
            get() = WidgetParamUiModel(
                group = "", sourceId = "", sourceType = "", cursor = "",
            )
    }
}

data class ChipWidgetUiModel(
    val isSelected: Boolean = false,
    val group: String,
    val sourceType: String,
    val sourceId: String,
    val text: String,
)

sealed class WidgetUiModel
    data class TabMenuUiModel(
        val items: List<ChipWidgetUiModel>,
    ) : WidgetUiModel() {
        companion object {
            val Empty : TabMenuUiModel
                get() = TabMenuUiModel(items = emptyList())
        }
    }

    data class WidgetItemUiModel(
        val item: PlayWidgetUiModel,
    ) : WidgetUiModel() {
        companion object {
            val Empty : WidgetItemUiModel
                get() = WidgetItemUiModel(item = PlayWidgetUiModel.Empty)
        }
    }

    object SubSlotUiModel : WidgetUiModel()

    data class PageConfig(val cursor: String, val isAutoPlay: Boolean) :
        WidgetUiModel() {
        companion object {
            val Empty : PageConfig
                get() = PageConfig(cursor = "", isAutoPlay = false)
        }
    }

val List<WidgetUiModel>.isSubSlotAvailable : Boolean
    get() {
        return this.any {
            it is SubSlotUiModel
        }
    }

val List<WidgetUiModel>.hasNextPage : Boolean
    get() {
        return this.any {
            if (it is PageConfig) it.cursor.isNotBlank() else false
        }
    }

val List<WidgetUiModel>.getChannelBlock : WidgetItemUiModel
    get() {
        return this.filterIsInstance<WidgetItemUiModel>().firstOrNull() ?: WidgetItemUiModel.Empty
    }

val List<WidgetUiModel>.getChannelBlocks : List<WidgetItemUiModel>
    get() {
        return this.filterIsInstance<WidgetItemUiModel>()
    }

val List<WidgetUiModel>.getChips : TabMenuUiModel
    get() {
        return this.filterIsInstance<TabMenuUiModel>().firstOrNull() ?: TabMenuUiModel.Empty
    }

val List<WidgetUiModel>.getConfig : PageConfig
    get() {
        return this.filterIsInstance<PageConfig>().firstOrNull() ?: PageConfig.Empty
    }

