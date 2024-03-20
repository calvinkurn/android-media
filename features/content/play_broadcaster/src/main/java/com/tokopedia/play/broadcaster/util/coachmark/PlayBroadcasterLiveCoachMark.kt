package com.tokopedia.play.broadcaster.util.coachmark

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener

/**
 * Created by Jonathan Darwin on 19 March 2024
 */
class PlayBroadcasterLiveCoachMark(
    private val views: List<View>,
) {

    init {
        views.forEach {
            it.addOneTimeGlobalLayoutListener {

            }
        }
    }
}
