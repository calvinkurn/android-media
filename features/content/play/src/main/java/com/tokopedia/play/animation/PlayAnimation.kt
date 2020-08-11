package com.tokopedia.play.animation

import android.view.View

/**
 * Created by jegul on 15/04/20
 */
interface PlayAnimation {

    fun start(targetView: View)

    fun cancel()
}