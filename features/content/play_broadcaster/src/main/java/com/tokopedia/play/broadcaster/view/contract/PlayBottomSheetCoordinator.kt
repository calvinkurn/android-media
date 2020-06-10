package com.tokopedia.play.broadcaster.view.contract

/**
 * Created by jegul on 26/05/20
 */
interface PlayBottomSheetCoordinator : PlayBaseCoordinator {
    fun saveCoverAndTitle(coverUrl: String, liveTitle: String)
    fun showBottomAction(shouldShow: Boolean)
}