package com.tokopedia.play.broadcaster.ui.model

import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState

/**
 * @author by furqan on 12/06/2020
 */
data class PlayCoverUiModel(
        val croppedCover: CoverSetupState,
        val state: SetupDataState
) {

    companion object {
        fun empty() = PlayCoverUiModel(CoverSetupState.Blank, SetupDataState.Draft)
    }
}