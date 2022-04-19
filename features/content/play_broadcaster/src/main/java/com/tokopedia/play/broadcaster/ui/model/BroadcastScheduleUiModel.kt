package com.tokopedia.play.broadcaster.ui.model

import java.util.*

/**
 * Created by jegul on 30/11/20
 */
sealed class BroadcastScheduleUiModel {

    object NoSchedule : BroadcastScheduleUiModel()
    data class Scheduled(val time: Date, val formattedTime: String) : BroadcastScheduleUiModel()
}