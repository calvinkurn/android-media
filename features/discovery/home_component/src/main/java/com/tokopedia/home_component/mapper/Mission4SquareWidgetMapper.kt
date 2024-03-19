package com.tokopedia.home_component.mapper

import com.tokopedia.home_component.visitable.Mission4SquareUiModel
import com.tokopedia.home_component.visitable.MissionWidgetDataModel

object Mission4SquareWidgetMapper {

    fun map(data: MissionWidgetDataModel): Mission4SquareUiModel {
        val content = data.labelGroup.associateBy { it.position }

        val titleLabelGroup = content[Const.TITLE]
            ?: MissionWidgetDataModel.LabelGroup(title = data.title)

        val subtitleLabelGroup = content[Const.SUBTITLE]
            ?: MissionWidgetDataModel.LabelGroup(title = data.subTitle)

        return Mission4SquareUiModel(
            data = data,
            title = Pair(titleLabelGroup.title, titleLabelGroup.extract()),
            subtitle = Pair(subtitleLabelGroup.title, subtitleLabelGroup.extract())
        )
    }

    private fun MissionWidgetDataModel.LabelGroup.extract(): Mission4SquareUiModel.TextStyle {
        if (styles.isEmpty()) return Mission4SquareUiModel.TextStyle()
        val style = styles.associateBy { it.key }

        return Mission4SquareUiModel.TextStyle(
            isBold = style[Const.TEXT_WEIGHT]?.value == Const.TEXT_WEIGHT_BOLD,
            textColor = style[Const.TEXT_COLOR]?.value.orEmpty()
        )
    }

    internal object Const {
        // position
        const val TITLE = "home-title"
        const val SUBTITLE = "home-subtitle"

        // styles
        const val TEXT_COLOR = "text-color"
        const val TEXT_WEIGHT = "text-format" // normal or bold

        // default
        const val TEXT_WEIGHT_BOLD = "bold"
    }
}
