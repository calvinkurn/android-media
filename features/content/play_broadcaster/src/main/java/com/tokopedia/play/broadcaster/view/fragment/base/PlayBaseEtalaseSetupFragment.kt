package com.tokopedia.play.broadcaster.view.fragment.base

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.view.contract.PlayEtalaseSetupCoordinator

abstract class PlayBaseEtalaseSetupFragment : TkpdBaseV4Fragment() {

    abstract fun refresh()

    protected val etalaseSetupCoordinator: PlayEtalaseSetupCoordinator
        get() = requireParentFragment() as PlayEtalaseSetupCoordinator
}