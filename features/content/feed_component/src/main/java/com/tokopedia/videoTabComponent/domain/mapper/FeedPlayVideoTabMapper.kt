package com.tokopedia.videoTabComponent.domain.mapper

import com.tokopedia.play.widget.sample.data.PlayGetContentSlotResponse
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

class FeedPlayVideoTabMapper {
    fun map(playGetContentSlotResponse: PlayGetContentSlotResponse, cursor: String): PlayWidgetUiModel {

        return PlayWidgetUiModel(
                title = "",
                actionTitle = "Lihat Semua",
                actionAppLink = "tokopedia://webview?titlebar=false\\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F",
                isActionVisible = true,
                config = PlayWidgetUiMock.getPlayWidgetConfigUiModel(),
                items = mutableListOf(),
                background = PlayWidgetUiMock.getPlayWidgetBackgroundUiModel()
        )
    }
}