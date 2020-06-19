package com.tokopedia.play.broadcaster.ui.model

import android.net.Uri
import com.tokopedia.play.broadcaster.view.state.SetupDataState

/**
 * @author by furqan on 12/06/2020
 */
data class PlayCoverUiModel(
        val coverImage: Uri?,
        val title: String,
        val state: SetupDataState,
        val source: CoverSourceEnum
)