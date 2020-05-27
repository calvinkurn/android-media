package com.tokopedia.play.broadcaster.view.fragment.base

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastCoordinator

/**
 * Created by jegul on 26/05/20
 */
abstract class PlayBaseSetupFragment : BaseDaggerFragment() {

    abstract fun getTitle(): String

    abstract fun isRootFragment(): Boolean

    protected open val broadcastCoordinator: PlayBroadcastCoordinator
        get() = requireParentFragment() as PlayBroadcastCoordinator
}