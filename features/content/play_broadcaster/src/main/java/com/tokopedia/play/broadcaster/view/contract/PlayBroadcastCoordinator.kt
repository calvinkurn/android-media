package com.tokopedia.play.broadcaster.view.contract


/**
 * Created by mzennis on 03/06/20.
 */
interface PlayBroadcastCoordinator : PlayBaseCoordinator {

    fun setupCloseButton(actionTitle: String = "")

    fun showActionBar(shouldShow: Boolean)
}