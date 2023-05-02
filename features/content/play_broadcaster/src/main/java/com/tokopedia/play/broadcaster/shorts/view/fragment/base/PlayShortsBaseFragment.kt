package com.tokopedia.play.broadcaster.shorts.view.fragment.base

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment

/**
 * Created By : Jonathan Darwin on November 09, 2022
 */
abstract class PlayShortsBaseFragment : TkpdBaseV4Fragment() {

    open fun onBackPressed(): Boolean = false
}
