package com.tokopedia.play.broadcaster.ui.model.interactive

sealed class BroadcastQuizState {
    data class Ongoing(val durationInMs: Long, val question:String) : BroadcastQuizState()
    object Finished : BroadcastQuizState()
}