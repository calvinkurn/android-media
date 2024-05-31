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
    ) = Mission4SquareUiModel(
        data = data,
        card = SmallProductModel(
            bannerImageUrl = data.imageURL,
            labelGroupList = data.labelGroup.map {
                SmallProductModel.LabelGroup(
                    position = it.position,
                    title = it.title,
                    type = it.type,
                    url = it.url,
                    styles = it.styles.map { style ->
                        SmallProductModel.LabelGroup.Styles(style.key, style.value)
                    }
                )
            }
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
