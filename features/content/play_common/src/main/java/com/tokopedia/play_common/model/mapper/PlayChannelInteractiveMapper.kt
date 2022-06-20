package com.tokopedia.play_common.model.mapper

import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.model.dto.interactive.InteractiveType
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by jegul on 07/07/21
 */
class PlayChannelInteractiveMapper @Inject constructor() {

    fun mapInteractive(input: GiveawayResponse) = PlayCurrentInteractiveModel(
        id = input.interactiveID,
        type = InteractiveType.getByValue(input.interactiveType),
        title = input.title,
        timeStatus = PlayInteractiveTimeStatus.getByValue(
            status = input.status,
            countdownStartInSec = input.countdownStart,
            countdownEndInSec = input.countdownEnd,
        ),
        endGameDelayInMs = TimeUnit.SECONDS.toMillis(input.waitingDuration.toLong())
    )
}