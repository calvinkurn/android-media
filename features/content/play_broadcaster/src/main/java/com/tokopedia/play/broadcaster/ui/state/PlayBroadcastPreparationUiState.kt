package com.tokopedia.play.broadcaster.ui.state

import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleFormState

/**
 * Created By : Jonathan Darwin on January 25, 2022
 */
internal data class PlayBroadcastPreparationUiState(
    val titleForm: PlayTitleFormUiState
)

data class PlayTitleFormUiState(
    val title: String,
    val status: PlayTitleFormState
)