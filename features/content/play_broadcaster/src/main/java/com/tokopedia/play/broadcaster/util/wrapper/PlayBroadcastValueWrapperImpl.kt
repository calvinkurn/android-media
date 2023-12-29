package com.tokopedia.play.broadcaster.util.wrapper

import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
* Created By : Jonathan Darwin on May 04, 2023
*/
class PlayBroadcastValueWrapperImpl @Inject constructor() : PlayBroadcastValueWrapper {

    override val rebindEffectToasterDuration: Int
        get() = Toaster.LENGTH_SHORT
}
