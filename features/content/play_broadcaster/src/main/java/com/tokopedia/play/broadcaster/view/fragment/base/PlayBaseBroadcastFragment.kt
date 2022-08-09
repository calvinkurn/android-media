package com.tokopedia.play.broadcaster.view.fragment.base

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.view.contract.PlayBaseCoordinator


/**
 * Created by mzennis on 29/05/20.
 */
abstract class PlayBaseBroadcastFragment: TkpdBaseV4Fragment() {

    protected open val broadcastCoordinator: PlayBaseCoordinator
        get() = activity as PlayBaseCoordinator

    /**
     * set true to override
     */
    open fun onBackPressed(): Boolean = false
}