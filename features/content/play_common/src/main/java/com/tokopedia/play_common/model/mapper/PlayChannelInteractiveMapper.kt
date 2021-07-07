package com.tokopedia.play_common.model.mapper

import com.tokopedia.play_common.domain.model.interactive.ChannelInteractive
import com.tokopedia.play_common.model.dto.InteractiveType
import com.tokopedia.play_common.model.dto.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.PlayInteractiveTimeStatus
import javax.inject.Inject

/**
 * Created by jegul on 07/07/21
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