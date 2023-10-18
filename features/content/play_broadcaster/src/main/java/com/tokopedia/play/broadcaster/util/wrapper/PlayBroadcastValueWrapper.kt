package com.tokopedia.play.broadcaster.util.wrapper

/**
* Created By : Jonathan Darwin on May 04, 2023
*/
interface PlayBroadcastValueWrapper {

    /**
     * We need to change error toaster duration in rebind effect
     * because the production duration is too fast for instrumentation test
     */
    val rebindEffectToasterDuration: Int
}
