package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.dto.interactive.InteractiveType
import com.tokopedia.play.data.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play.data.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play.data.interactive.ChannelInteractive
import javax.inject.Inject

/**
 * Created by jegul on 30/06/21
 */
class PlayChannelInteractiveMapper @Inject constructor() {

    fun mapInteractive(input: ChannelInteractive) = PlayCurrentInteractiveModel(
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