package com.tokopedia.play.broadcaster.ui.model.title

/**
 * Created By : Jonathan Darwin on January 25, 2022
 */
data class PlayTitleFormUiModel(
    val title: String = "",
    val status: PlayTitleFormState = PlayTitleFormState.Nothing
)

enum class PlayTitleFormState {
    Input, Submit, Nothing
}