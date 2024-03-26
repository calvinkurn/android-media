package com.tokopedia.home_component.mapper

import com.tokopedia.home_component.visitable.Mission4SquareUiModel
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader

object Mission4SquareWidgetMapper {

    fun map(
        data: MissionWidgetDataModel,
        channelName: String,
        channelId: String,
        header: ChannelHeader,
        verticalPosition: Int,
        cardPosition: Int
    ): Mission4SquareUiModel {
        val content = data.labelGroup.associateBy { it.position }

        val titleLabelGroup = content[Const.TITLE]
            ?: MissionWidgetDataModel.LabelGroup(title = data.title)

        val subtitleLabelGroup = content[Const.SUBTITLE]
            ?: MissionWidgetDataModel.LabelGroup(title = data.subTitle)

        return Mission4SquareUiModel(
            data = data,
            card = SmallProductModel(
                bannerImageUrl = data.imageURL,
                title = Pair(titleLabelGroup.title, titleLabelGroup.extract()),
                subtitle = Pair(subtitleLabelGroup.title, subtitleLabelGroup.extract())
            ),
            cardPosition = cardPosition,
            channelName = channelName,
            channelId = channelId,
            header = header,
            verticalPosition = verticalPosition,
            isCache = data.isCache,
            appLog = data.appLog
        )
    }

    private fun MissionWidgetDataModel.LabelGroup.extract(): SmallProductModel.TextStyle {
        if (styles.isEmpty()) return SmallProductModel.TextStyle()
        val style = styles.associate { it.key to it.value }

        return SmallProductModel.TextStyle(
            isBold = style[Const.TEXT_WEIGHT] == Const.TEXT_WEIGHT_BOLD,
            textColor = style[Const.TEXT_COLOR].orEmpty()
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
