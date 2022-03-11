package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by mzennis on 03/07/20.
 */
data class LiveDurationUiModel(
        val date: String,
        val duration: String,
        val isEligiblePostVideo: Boolean,
) {
        companion object {
            fun empty() = LiveDurationUiModel("", "", false)
        }
}