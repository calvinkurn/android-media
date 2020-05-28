package com.tokopedia.play.broadcaster.view.fragment.base

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastCoordinator


/**
 * Created by mzennis on 28/05/20.
 */
abstract class PlayBaseBroadcastFragment: TkpdBaseV4Fragment() {

    protected open val broadcastCoordinator: PlayBroadcastCoordinator
        get() = requireParentFragment() as PlayBroadcastCoordinator
}