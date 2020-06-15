package com.tokopedia.play.broadcaster.ui.model

import android.net.Uri

/**
 * @author by furqan on 12/06/2020
 */
data class PlayCoverUiModel(
        var coverImageUri: Uri?,
        var coverImageUrl: String,
        var liveTitle: String)