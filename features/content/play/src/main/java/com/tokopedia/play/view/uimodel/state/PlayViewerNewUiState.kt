package com.tokopedia.play.view.uimodel.state

import com.tokopedia.play.view.uimodel.interactive.PlayInteractiveTimeStatus

/**
 * Created by jegul on 28/06/21
 */
data class PlayViewerNewUiState(
        val interactive: PlayInteractiveUiState? = null
)

data class PlayInteractiveUiState(
        val title: String,
        val status: PlayInteractiveTimeStatus
)