package com.tokopedia.play.broadcaster.ui.model.game.quiz

import java.util.*

sealed class BroadcastQuizState {
    data class Ongoing(val endTime: Calendar, val question:String) : BroadcastQuizState()
    object Finished : BroadcastQuizState()
}