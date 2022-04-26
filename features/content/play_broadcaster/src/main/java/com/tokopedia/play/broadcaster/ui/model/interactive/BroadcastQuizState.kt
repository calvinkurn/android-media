package com.tokopedia.play.broadcaster.ui.model.interactive

import java.util.*

sealed class BroadcastQuizState {
    data class Ongoing(val endTime: Calendar, val question:String) : BroadcastQuizState()
    object Finished : BroadcastQuizState()
}