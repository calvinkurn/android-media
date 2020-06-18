package com.tokopedia.play.broadcaster.view.fragment.base

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.view.contract.PlayBottomSheetCoordinator

/**
 * Created by jegul on 26/05/20
 */
abstract class PlayBaseSetupFragment : TkpdBaseV4Fragment() {

    /**
     * @return true means the back pressed has been intercepted, false otherwise
     */
    abstract fun onInterceptBackPressed(): Boolean

    protected open val bottomSheetCoordinator: PlayBottomSheetCoordinator
        get() = requireParentFragment() as PlayBottomSheetCoordinator
}