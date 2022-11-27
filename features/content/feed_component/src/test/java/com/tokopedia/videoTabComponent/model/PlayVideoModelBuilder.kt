package com.tokopedia.videoTabComponent.model

import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.videoTabComponent.domain.model.data.ContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.PlayGetContentSlotResponse

/**
 * Created by shruti agarwal on 24/11/22.
 */

class PlayVideoModelBuilder {

    fun getContentSlotResponse(
        playGetContentSlot: PlayGetContentSlotResponse = PlayGetContentSlotResponse(),
        isDataFromTabClick: Boolean = false
    ) = ContentSlotResponse(
        playGetContentSlot = playGetContentSlot,
        isDataFromTabClick = isDataFromTabClick
    )
    fun getPlaWidgetReminder() = PlayWidgetReminder()
}
