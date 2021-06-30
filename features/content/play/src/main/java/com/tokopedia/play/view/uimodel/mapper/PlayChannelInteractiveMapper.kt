package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.interactive.ChannelInteractive
import com.tokopedia.play.view.uimodel.interactive.InteractiveType
import com.tokopedia.play.view.uimodel.interactive.PlayCurrentInteractiveUiModel
import com.tokopedia.play.view.uimodel.interactive.PlayInteractiveTimeStatus
import javax.inject.Inject

/**
 * Created by jegul on 30/06/21
 */
class PlayChannelInteractiveMapper @Inject constructor() {

    fun mapInteractive(input: ChannelInteractive) = PlayCurrentInteractiveUiModel(
            id = input.interactiveID,
            type = InteractiveType.getByValue(input.interactiveType),
            title = input.title,
            timeStatus = PlayInteractiveTimeStatus.getByValue(
                    status = input.status,
                    countdownStartInSec = input.countdownStart,
                    countdownEndInSec = input.countdownEnd,
            )
    )
}