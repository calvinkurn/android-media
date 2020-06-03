package com.tokopedia.play.broadcaster.view.fragment.base

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.view.contract.PlayActionBarInteraction
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastCoordinator


/**
 * Created by mzennis on 29/05/20.
 */
abstract class PlayBaseBroadcastFragment: TkpdBaseV4Fragment(), PlayActionBarInteraction {

    protected open val broadcastCoordinator: PlayBroadcastCoordinator
        get() = activity as PlayBroadcastCoordinator

    /**
     * set return true to override this function
     */
    override fun onCloseActionBar(): Boolean {
        return false
    }
}