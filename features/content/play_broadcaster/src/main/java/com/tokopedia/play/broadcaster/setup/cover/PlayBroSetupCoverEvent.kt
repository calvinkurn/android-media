package com.tokopedia.play.broadcaster.setup.cover

sealed interface PlayBroSetupCoverEvent {

    data class SubmitCoverLoading(
        val isLoading: Boolean,
        val throwable: Throwable? = null,
    ) : PlayBroSetupCoverEvent

    data class ButtonState(
        val isEnabled: Boolean,
        val showDelete: Boolean = false
    ) : PlayBroSetupCoverEvent
}
