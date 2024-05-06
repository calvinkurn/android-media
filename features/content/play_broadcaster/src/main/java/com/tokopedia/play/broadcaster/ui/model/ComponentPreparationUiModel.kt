package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by Jonathan Darwin on 19 March 2024
 */
data class ComponentPreparationUiModel(
    val gameIcon: State,
    val statisticIcon: State,
    val hasBeenHandled: Boolean,
) {

    val areAllComponentsReady: Boolean
        get() = gameIcon.isReady && statisticIcon.isReady

    companion object {
        val Empty: ComponentPreparationUiModel
            get() = ComponentPreparationUiModel(
                gameIcon = State.NotDetermined,
                statisticIcon = State.NotDetermined,
                hasBeenHandled = false,
            )
    }

    enum class State {
        NotDetermined,
        Ready;

        val isReady: Boolean
            get() = this != NotDetermined
    }
}
