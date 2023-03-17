package com.tokopedia.play.broadcaster.analytic.beautification

import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 17, 2023
 */
@ActivityRetainedScope
class PlayBroadcastBeautificationAnalyticStateHolder @Inject constructor() {
    var isBeautificationMenuShown = false
}
