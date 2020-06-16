package com.tokopedia.play.broadcaster.view.contract

import android.net.Uri

/**
 * Created by jegul on 26/05/20
 */
interface PlayBottomSheetCoordinator : PlayBaseCoordinator {
    fun saveCoverAndTitle(coverUri: Uri, coverUrl: String, liveTitle: String)
}