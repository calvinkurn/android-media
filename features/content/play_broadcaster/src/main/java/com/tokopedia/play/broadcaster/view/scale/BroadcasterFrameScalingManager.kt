package com.tokopedia.play.broadcaster.view.scale

import android.view.View

/**
 * Created By : Jonathan Darwin on March 02, 2023
 */
interface BroadcasterFrameScalingManager {

    fun scaleDown(view: View, bottomSheetHeight: Int, fullPageHeight: Int)

    fun scaleUp(view: View)
}
