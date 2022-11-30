package com.tokopedia.play.view.uimodel

import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * @author by astidhiyaa on 28/11/22
 */
data class ExploreWidgetUiModel(
    val param: WidgetParamUiModel,
    val chips: List<ChipWidgetUiModel>, //check whether to use from WidgetUiModel
    val widgets: List<WidgetUiModel.WidgetItemUiModel>
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

sealed class WidgetUiModel {
    data class TabMenuUiModel(
        val items: List<ChipWidgetUiModel>,
    ) : WidgetUiModel()

    data class WidgetItemUiModel(
        val item: PlayWidgetUiModel,
    ) : WidgetUiModel()

    object SubSlotUiModel : WidgetUiModel()

    //cursor
}
